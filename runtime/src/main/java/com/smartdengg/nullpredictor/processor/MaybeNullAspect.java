package com.smartdengg.nullpredictor.processor;

import android.util.Log;
import com.smartdengg.nullpredictor.MaybeNull;
import com.smartdengg.nullpredictor.error.NullError;
import com.smartdengg.nullpredictor.error.UnSupportReturnTypeError;
import com.smartdengg.nullpredictor.internal.Utils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import static com.smartdengg.nullpredictor.internal.Utils.arrayToInstance;
import static com.smartdengg.nullpredictor.internal.Utils.getCurrentStackTrace;
import static com.smartdengg.nullpredictor.internal.Utils.getExceptionStackTrace;

/**
 * 创建时间:  2017/03/13 15:19 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Aspect public class MaybeNullAspect {

  private static final String TAG = "TAG-MaybeNull";

  private MaybeNullAspect() {
  }

  @Pointcut("execution(* *(.., @com.smartdengg.nullpredictor.MaybeNull (*), ..))")
  public void parameterWithMaybeNull() {
  }

  @Around("parameterWithMaybeNull()") public Object aroundAdvice(ProceedingJoinPoint joinPoint)
      throws Throwable {

    Object[] args = joinPoint.getArgs();

    List<MethodArgument> methodArguments = MethodArgument.flatOf(joinPoint);
    for (int i = 0, n = methodArguments.size(); i < n; i++) {

      MethodArgument methodArgument = methodArguments.get(i);

      MaybeNull maybeNull =
          Utils.getAnnotationByType(methodArgument.getAnnotations(), MaybeNull.class);
      if (maybeNull != null && methodArgument.validateCallback()) {

        ClassLoader classLoader = methodArgument.getArgumentClassLoader();
        Class<?>[] interfaces = methodArgument.getArgumentInterfaces();
        Object proxyCallback = Proxy.newProxyInstance(classLoader, interfaces,
            new InvocationHandlerWrapper(methodArgument, maybeNull));
        args[i] = proxyCallback;
      }
    }

    return joinPoint.proceed(args);
  }

  private static class InvocationHandlerWrapper implements InvocationHandler {

    private MethodArgument methodArgument;
    private MaybeNull maybeNull;
    private Object instance;

    InvocationHandlerWrapper(MethodArgument methodArgument, MaybeNull maybeNull) {
      this.methodArgument = methodArgument;
      this.maybeNull = maybeNull;
      this.instance = methodArgument.getRawInstance();
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

      final Class<?> returnType = method.getReturnType();
      final String instanceName = methodArgument.getName();

      if (instance == null) {

        if (maybeNull.loggable()) {
          NullError error = new NullError(instanceName + " = null", null, getCurrentStackTrace());
          Log.e(TAG, Utils.getStackTraceString(error));
        }

        if (returnType.isArray()) return arrayToInstance(returnType);

        if (returnType.isPrimitive() && returnType != Void.TYPE) {
          throw new UnSupportReturnTypeError(
              MessageFormat.format("{0} return type is primitive", instanceName), null,
              getCurrentStackTrace());
        }

        return null;
      }

      try {
        return method.invoke(instance, args);
      } catch (InvocationTargetException ex) {
        Throwable cause = ex.getCause();
        cause.setStackTrace(getExceptionStackTrace(cause.getStackTrace()));
        throw cause;
      }
    }
  }
}
