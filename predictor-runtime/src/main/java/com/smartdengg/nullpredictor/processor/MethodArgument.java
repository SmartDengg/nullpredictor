package com.smartdengg.nullpredictor.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 创建时间:  2017/03/19 17:50 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class MethodArgument {

  private final int index;
  private final Class<?> type;
  private final String name;
  private final Annotation[] annotations;
  private final Object rawInstance;

  private MethodArgument(int index, Class<?> type, String name, Annotation[] annotations,
      Object rawInstance) {
    this.index = index;
    this.type = type;
    this.name = name;
    this.annotations = annotations;
    this.rawInstance = rawInstance;
  }

  int getIndex() {
    return index;
  }

  Class<?> getType() {
    return type;
  }

  String getName() {
    return name;
  }

  Annotation[] getAnnotations() {
    return annotations;
  }

  Object getRawInstance() {
    return rawInstance;
  }

  boolean isClass() {
    return !type.isInterface();
  }

  boolean isPrimitive() {
    return type.isPrimitive();
  }

  boolean isArray() {
    return type.isArray();
  }

  private Class<?> getArgumentClass() {
    if (rawInstance != null) return rawInstance.getClass();
    return type;
  }

  ClassLoader getArgumentClassLoader() {
    return getArgumentClass().getClassLoader();
  }

  Class[] getArgumentInterfaces() {

    Class<?> clazz = getArgumentClass();
    Class[] interfaces;

    if (!clazz.isInterface()) {
      interfaces = clazz.getInterfaces();
    } else {
      interfaces = new Class[] { clazz };
    }

    return interfaces;
  }

  boolean validateCallback() {
    return assertCallbackIsInterface(type);
  }

  static List<MethodArgument> flatOf(JoinPoint joinPoint) {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    String[] names = methodSignature.getParameterNames();
    Class<?>[] parameterTypes = method.getParameterTypes();
    Annotation[][] annotations = method.getParameterAnnotations();
    Object[] args = joinPoint.getArgs();
    int length = args.length;

    List<MethodArgument> arguments = new ArrayList<>(length);
    for (int i = 0; i < length; i++)
      arguments.add(new MethodArgument(i, parameterTypes[i], names[i], annotations[i], args[i]));
    return Collections.unmodifiableList(arguments);
  }

  private static <T> boolean assertCallbackIsInterface(Class<T> service) {
    if (!service.isInterface()) {
      throw new IllegalArgumentException("callback declarations must be interfaces.");
    }
    // Prevent API interfaces from extending other interfaces. This not only avoids a bug in
    // Android (http://b.android.com/58753) but it forces composition of API declarations which is
    // the recommended pattern.
    if (service.getInterfaces().length > 0) {
      throw new IllegalArgumentException("callback must not extend other interfaces.");
    }
    return true;
  }
}