package com.smartdengg.notnullpredict;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 创建时间:  2017/03/12 17:50 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */

public class Test {

  public static void main(String[] args)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
      InstantiationException {

    //Callback callback = new CallbackImpl();
    //callback.onResult("callback instance");
    //callback.onResult(callback.toString());
    //
    //CallbackProxyHandler callbackProxyHandler = new CallbackProxyHandler(callback);
    //Class<?>[] interfaces = callback.getClass().getInterfaces();
    //Callback instance = (Callback) Proxy.newProxyInstance(Callback.class.getClassLoader(),
    //    interfaces, callbackProxyHandler);
    //
    //instance.onResult("callback Proxy");
    //
    //Object newInstance = Proxy.getProxyClass(callback.getClass().getClassLoader(),
    //    interfaces)
    //    .getConstructor(InvocationHandler.class)
    //    .newInstance(callbackProxyHandler);
    //
    //boolean proxyClass = Proxy.isProxyClass(instance.getClass());
    //
    //System.out.println(proxyClass);

    float f = 0.123456789f;
    float f1 = 12345.123456789f;
    float f2 = 12345.1f;
    DecimalFormat decimalFormat = new DecimalFormat("#.#####");
    decimalFormat.setRoundingMode(RoundingMode.CEILING);

    double d = f;
    double d1 = f1;
    double d2 = f2;
    String d3 = "1.1234567";

    System.err.println(decimalFormat.format(d));
    System.err.println(decimalFormat.format(d1));
    System.err.println(decimalFormat.format(d2));
    System.err.println(decimalFormat.format(d3));

    for (; ; ) ;
  }

  public static String subZeroAndDot(String s) {
    if (s.indexOf(".") > 0) {
      s = s.replaceAll("0+?$", "");//去掉多余的0
      s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
    }
    return s;
  }

  interface Callback {

    void onResult(String result);
  }

  interface Callback1 extends Callback {
  }

  private static class CallbackImpl implements Callback, Callback1 {
    @Override public void onResult(String result) {
      System.out.println(result + " in CallbackImpl");
    }
  }

  private static class CallbackProxyHandler implements InvocationHandler {

    private Object instance;

    CallbackProxyHandler(Object bird) {
      this.instance = bird;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

      //System.out.println(proxy.toString());
      System.out.println(method.toString());
      System.out.println(Arrays.toString(args));

      System.out.println("before proxy...");
      //Object obj = method.invoke(proxy, args);
      Object obj = method.invoke(this.instance, args);
      System.out.println("after proxy...");
      return null;
    }
  }
}
