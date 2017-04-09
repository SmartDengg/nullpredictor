package com.smartdengg.nullpredictor.processor;

import com.smartdengg.nullpredictor.annotation.NotNull;
import com.smartdengg.nullpredictor.error.NullError;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import static com.smartdengg.nullpredictor.internal.Utils.getCurrentStackTrace;

/**
 * 创建时间:  2017/03/13 15:19 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Aspect public class NotNullAspect {

  private static final String TAG = "TAG-NotNull";

  private NotNullAspect() {
  }

  @Pointcut("@annotation(checker)") public void methodWithNotNull(NotNull checker) {
  }

  @Pointcut("execution(* *(..))") private void atExecution() {
  }

  @Before(value = "atExecution() && methodWithNotNull(notNullAnno)", argNames = "notNullAnno")
  public void beforeMethodExecute(JoinPoint joinPoint, NotNull notNullAnno) {

    final Signature signature = joinPoint.getSignature();
    if (signature instanceof MethodSignature) {
      final MethodSignature methodSignature = (MethodSignature) signature;
      final String[] parameterNames = methodSignature.getParameterNames();

      Object[] args = joinPoint.getArgs();
      for (int i = 0, n = args.length; i < n; i++) {
        final Object arg = args[i];
        if (arg == null) {
          if (notNullAnno.debug()) {
            StackTraceElement[] stackTrace = getCurrentStackTrace();
            //noinspection UnnecessaryLocalVariable
            final NullError error = new NullError(parameterNames[i] + " = null", null, stackTrace);
            //Log.e(TAG, Utils.getStackTraceString(error));
            throw error;
          }
        }
      }
    }
  }
}
