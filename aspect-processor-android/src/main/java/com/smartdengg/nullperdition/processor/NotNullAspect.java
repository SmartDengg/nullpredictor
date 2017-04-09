package com.smartdengg.nullperdition.processor;

import com.smartdengg.nullperdition.error.NullError;
import com.smartdengg.nullpredictor.NotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import static com.smartdengg.nullperdition.internal.Utils.getCurrentStackTrace;

/**
 * 创建时间:  2017/03/13 15:19 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
@Aspect public class NotNullAspect {

  private static final String TAG = "TAG-NotNull";

  private NotNullAspect() {
  }

  @Pointcut("@annotation(notnullAnno)") public void withNotNullAnnotation(NotNull notnullAnno) {
  }

  @Pointcut("execution(!synthetic *.new(..))") private void atConstructorExecution() {
  }

  @Pointcut("execution(* *(..))") private void atMethodExecution() {
  }

  @Before(value = "(atConstructorExecution() || atMethodExecution()) && withNotNullAnnotation(notNullAnno)", argNames = "notNullAnno")
  public void beforeMethodExecute(JoinPoint joinPoint, NotNull notNullAnno) {

    final Signature signature = joinPoint.getSignature();
    if (signature instanceof CodeSignature) {
      final String[] parameterNames = ((CodeSignature) signature).getParameterNames();

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
