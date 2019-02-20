package com.aslan.baselibrary;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.aslan.baselibrary.http.BaseHttpError;
import com.aslan.baselibrary.http.OkHttpManager;
import com.aslan.baselibrary.http.callback.BaseCallback;
import java.io.IOException;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    assertEquals("com.aslan.baselibrary.test", appContext.getPackageName());
    OkHttpManager.getInstance().setLogEnable(true);
    OkHttpManager.get().with(null).host("http://www.baidu.com").build()
        .execute(new BaseCallback(appContext) {
          @Override
          public void onFailure(BaseHttpError baseHttpError) {

          }

          @Override
          public Object handleResponse(@NonNull Response response) throws IOException {
            return null;
          }

          @Override
          public void onResponse(@NonNull Object respone) {

          }
        });
  }
}
