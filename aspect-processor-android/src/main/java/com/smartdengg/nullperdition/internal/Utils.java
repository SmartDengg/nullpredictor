package com.smartdengg.nullperdition.internal;

import com.smartdengg.nullperdition.processor.MaybeNullAspect;
import com.smartdengg.nullperdition.processor.NotNullAspect;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
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
    if (annotations == null || annotations.length == 0) return null;
    for (final Annotation annotation : annotations) {
      if (type.isAssignableFrom(annotation.getClass())) {
        return type.cast(annotation);
      }
    }
    return null;
  }

  public static Class<?> getRawType(Type type) {
    if (type == null) throw new NullPointerException("type == null");

    if (type instanceof Class<?>) {
      // Type is a normal class.
      return (Class<?>) type;
    }
    if (type instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) type;

      // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
      // suspects some pathological case related to nested classes exists.
      Type rawType = parameterizedType.getRawType();
      if (!(rawType instanceof Class)) throw new IllegalArgumentException();
      return (Class<?>) rawType;
    }
    if (type instanceof GenericArrayType) {
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      return Array.newInstance(getRawType(componentType), 0).getClass();
    }
    if (type instanceof TypeVariable) {
      // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
      // type that's more general than necessary is okay.
      return Object.class;
    }
    if (type instanceof WildcardType) {
      return getRawType(((WildcardType) type).getUpperBounds()[0]);
    }

    throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
        + "GenericArrayType, but <"
        + type
        + "> is of type "
        + type.getClass().getName());
  }

  public static Object arrayToInstance(Class<?> returnType) {
    Class<?> componentType = returnType.getComponentType();
    if (componentType.isArray()) {
      componentType = arrayToInstance(componentType).getClass();
    }
    return Array.newInstance(componentType, new int[] { 0 });
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
