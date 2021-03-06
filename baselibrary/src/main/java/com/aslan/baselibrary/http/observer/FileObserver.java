package com.aslan.baselibrary.http.observer;

import android.content.Context;
import androidx.annotation.NonNull;
import com.aslan.baselibrary.utils.LogUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;

/**
 * 下载文件，保存文件到本地路径
 *
 * @author Aslan
 * @date 2019/9/23
 */
public abstract class FileObserver implements Observer<ResponseBody> {

  private File mFile;
  private Context context;

  public FileObserver(@NonNull Context context, @NonNull File mFile) {
    this.context = context;
    this.mFile = mFile;
  }

  @Override
  public void onSubscribe(Disposable d) {

  }

  @Override
  public void onNext(ResponseBody responseBody) {
    InputStream is = null;
    FileOutputStream os = null;
    try {
      File dir = mFile.getParentFile();
      if (dir == null) {
        return;
      }

      if (!dir.exists()) {
        boolean ret = dir.mkdirs();
        if (!ret) {
          LogUtils.e("FileObserver", "create file dir failed: " + dir.getPath());
        }
      }

      is = responseBody.byteStream();
      os = new FileOutputStream(mFile);
      byte[] buffer = new byte[1024];
      int read;
      while ((read = is.read(buffer)) > 0) {
        os.write(buffer, 0, read);
      }
    } catch (IOException e) {
      e.printStackTrace();
      LogUtils.e("FileObserver", e);
    } finally {
      if (os != null) {
        try {
          os.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}