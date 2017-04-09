package com.smartdengg.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.smartdengg.nullpredictor.MaybeNull;
import com.smartdengg.nullpredictor.NotNull;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    testNotNull(0, null, null);

    Inner inner = new Inner(null);
  }

  @NotNull(debug = false)
  private void testNotNull(int i, String text, @MaybeNull(loggable = true) Callback callback) {
    callback.test();
  }

  private static class Inner {

    Callback callback;

    @com.smartdengg.nullpredictor.NotNull Inner(Callback callback) {
      this.callback = callback;
    }
  }
}
