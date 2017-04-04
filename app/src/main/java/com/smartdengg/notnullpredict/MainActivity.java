package com.smartdengg.notnullpredict;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.smartdengg.nullperdition.annotation.MaybeNull;
import com.smartdengg.nullperdition.annotation.NotNull;
import java.lang.reflect.Proxy;

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
    testNotNull(1, "hello world", null, null, new Entity());

    //checkNullBeforeMethod(null, null);
  }

  @NotNull(debug = true)
  private void testNotNull(int i, String text, @MaybeNull(loggable = true) Callback callback,
      @MaybeNull(loggable = true) Callback callback1, Entity entity) {
    Log.d(TAG, "testNotNull: " + text);

    boolean proxyClass0 = Proxy.isProxyClass(callback.getClass());
    boolean proxyClass1 = Proxy.isProxyClass(callback1.getClass());
    boolean proxyClass2 = Proxy.isProxyClass(entity.getClass());

    callback.onResult(new Entity());
    callback1.onResult(new Entity());
  }

  @NotNull(debug = true) private void checkNullBeforeMethod(String s, Callback callback) {
  }

  interface Callback {

    Object[] onResult(Entity entity);
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
}
