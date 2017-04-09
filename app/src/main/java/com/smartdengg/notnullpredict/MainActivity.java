package com.smartdengg.notnullpredict;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.smartdengg.nullperdition.annotation.MaybeNull;
import com.smartdengg.nullperdition.annotation.NotNull;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Callback callback = new Callback() {
      @Override public Object[] onResult(Entity entity) {
        entity.printNewLine();

        reThrow();

        return new Object[1];
      }

      private void reThrow() {
        throw new IllegalStateException("---------");
      }
    };
    //testNotNull(1, "hello world", null, null, null, null, null, null, new Entity());

    //checkNullBeforeMethod(null, null);

    Inner inner = new Inner(null);
  }

  //@NotNull(debug = true)
  private void testNotNull(int i, String text, @MaybeNull(loggable = true) Callback callback,
      @MaybeNull(loggable = true) Callback1 callback1,
      @MaybeNull(loggable = true) Callback2 callback2,
      @MaybeNull(loggable = true) Callback3<String> callback3,
      @MaybeNull(loggable = true) Callback4 callback4,
      @MaybeNull(loggable = true) Callback5 callback5, Entity entity) {
    Log.d(TAG, "testNotNull: " + text);

    boolean proxyClass0 = Proxy.isProxyClass(callback.getClass());
    boolean proxyClass1 = Proxy.isProxyClass(callback1.getClass());
    boolean proxyClass2 = Proxy.isProxyClass(entity.getClass());

    Object[] objects = callback.onResult(new Entity());
    Integer[] integers = callback1.onResult(new Entity());
    int[] ints = callback2.onResult(new Entity());
    Object object = callback3.onResult(new Entity());
    String[][] strings = callback4.onResult(new Entity());
    String[][][] strings1 = callback5.onResult(new Entity());

    Log.d(TAG, "objects = " + Arrays.toString(objects));
    Log.d(TAG, "integers = " + Arrays.toString(integers));
    Log.d(TAG, "ints = " + Arrays.toString(ints));
    Log.d(TAG, object != null ? object.toString() : "object = null");
    Log.d(TAG, "strings = " + Arrays.toString(strings));
    Log.d(TAG, "strings1 = " + Arrays.toString(strings1));
    String[][][] a = new String[0][0][0];
    Log.d(TAG, "strings2 = " + Arrays.toString(a));
  }

  @NotNull(debug = true) private void checkNullBeforeMethod(String s, Callback callback) {
  }

  interface Callback {

    Object[] onResult(Entity entity);
  }

  interface Callback1 {

    Integer[] onResult(Entity entity);
  }

  interface Callback2 {

    int[] onResult(Entity entity);
  }

  interface Callback3<T> {

    T onResult(Entity entity);
  }

  interface Callback4 {

    String[][] onResult(Entity entity);
  }

  interface Callback5 {

    String[][][] onResult(Entity entity);
  }

  private static class Entity implements View.OnClickListener, Callback {

    private static final String TAG = Entity.class.getSimpleName();

    void printNewLine() {

      Log.d(TAG, "this is from Entity \n\r");
    }

    @Override public void onClick(View v) {

    }

    @Override public Object[] onResult(Entity entity) {

      return new Object[1];
    }
  }

  private static class Inner {

    private Callback callback;

    @NotNull(debug = true) public Inner(Callback callback) {
      this.callback = callback;
    }
  }
}
