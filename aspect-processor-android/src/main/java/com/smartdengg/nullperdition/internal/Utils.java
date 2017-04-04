package com.smartdengg.nullperdition.internal;

import com.smartdengg.nullperdition.processor.MaybeNullAspect;
import com.smartdengg.nullperdition.processor.NotNullAspect;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建时间:  2017/03/19 17:56 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class Utils {

  public static boolean hasAnnotation(final Annotation[] annotations,
      Class<? extends Annotation> type) {
    if (annotations == null || annotations.length == 0) return false;
    for (Annotation annotation : annotations)
      if (type.isAssignableFrom(annotation.getClass())) return true;
    return false;
  }

  public static <T extends Annotation> T getAnnotationByType(final Annotation[] annotations,
      final Class<T> type) {
    if (annotations == null) return null;
    T result = null;
    for (final Annotation annotation : annotations) {
      if (type.isAssignableFrom(annotation.getClass())) {
        result = type.cast(annotation);
        break;
      }
    }
    return result;
  }

  public static <T extends Annotation> T getAnnotationByType(Method method, Class<T> type) {
    return method.getAnnotation(type);
  }

  public static String getStackTraceString(Throwable tr) {
    if (tr == null) return "";

    StringWriter sw = new StringWriter(256);
    PrintWriter pw = new PrintWriter(sw, false);
    tr.printStackTrace(pw);
    pw.flush();
    return sw.toString();
  }

  public static StackTraceElement[] getExceptionStackTrace(StackTraceElement[] stackTraceElements) {

    int index = 0;
    final StackTraceElement[] reversedElements = Utils.reverse(new Throwable().getStackTrace());
    for (int i = 0, n = reversedElements.length; i < n; i++) {
      if (MaybeNullAspect.class.getName().equals(reversedElements[i].getClassName())) {
        index = i;
        break;
      }
    }

    int length = reversedElements.length - index;
    final StackTraceElement[] innerStackTrace = new StackTraceElement[length];
    System.arraycopy(reversedElements, index, innerStackTrace, 0, length);

    final List<StackTraceElement> stackTraceElementList =
        new LinkedList<>(Arrays.asList(stackTraceElements));

    //noinspection ForLoopReplaceableByForEach
    for (int i = 0, n = innerStackTrace.length; i < n; i++) {
      final StackTraceElement removedElement = innerStackTrace[i];
      for (Iterator<StackTraceElement> iterator = stackTraceElementList.iterator();
          iterator.hasNext(); ) {
        if (iterator.next().getMethodName().equals(removedElement.getMethodName())) {
          iterator.remove();
        }
      }
    }

    return stackTraceElementList.toArray(new StackTraceElement[stackTraceElementList.size()]);
  }

  public static StackTraceElement[] getCurrentStackTrace() {

    int length = 0;
    StackTraceElement[] reversedElements = Utils.reverse(new Throwable().getStackTrace());
    for (int i = 0, n = reversedElements.length; i < n; i++) {
      String className = reversedElements[i].getClassName();
      if (MaybeNullAspect.class.getName().equals(className) || NotNullAspect.class.getName()
          .equals(className)) {
        length = i;
        break;
      }
    }

    StackTraceElement[] stackTrace = new StackTraceElement[length];
    System.arraycopy(reversedElements, 0, stackTrace, 0, length);
    return Utils.reverse(stackTrace);
  }

  @SuppressWarnings("unchecked") private static <T> T[] reverse(T[] arr) {
    List<T> list = Arrays.asList(arr);
    Collections.reverse(list);
    return (T[]) list.toArray();
  }
}
