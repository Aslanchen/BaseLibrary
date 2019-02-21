package com.aslan.baselibrary.http.callback;

import android.content.Context;
import android.support.annotation.NonNull;
import com.aslan.baselibrary.http.BaseHttpError;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 文件
 *
 * @author Aslan
 * @date 2019/1/7
 */
public abstract class FileCallBack implements Callback {

  private Context context;
  /**
   * 目标文件存储的文件夹路径
   */
  private String destFileDir;
  /**
   * 目标文件存储的文件名
   */
  private String destFileName;

  public void saveFile(Response response) throws IOException {
    InputStream is = null;
    byte[] buf = new byte[2048];
    int len = 0;
    FileOutputStream fos = null;
    ResponseBody body = response.body();
    if (body == null) {
      return;
    }

    try {
      is = body.byteStream();
      long total = body.contentLength();
      long sum = 0;

      File dir = new File(destFileDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      File file = new File(dir, destFileName);
      fos = new FileOutputStream(file);
      while ((len = is.read(buf)) != -1) {
        sum += len;
        fos.write(buf, 0, len);
        inProgress(sum, total, (int) (sum * 100 / total));
      }
      fos.flush();
      onResponse(file);
    } finally {
      try {
        body.close();
        if (is != null) {
          is.close();
        }
      } catch (IOException e) {
      }
      try {
        if (fos != null) {
          fos.close();
        }
      } catch (IOException e) {
      }
    }
  }

  public FileCallBack(Context context, String destFileDir, String destFileName) {
    this.context = context;
    this.destFileDir = destFileDir;
    this.destFileName = destFileName;
  }

  @Override
  public void onFailure(@NonNull Call call, @NonNull IOException e) {
    onFailure(new BaseHttpError(context, e));
  }

  @Override
  public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
    saveFile(response);
  }

  public abstract void inProgress(long sum, long total, int progress);

  public abstract void onFailure(@NonNull BaseHttpError baseHttpError);

  public abstract void onResponse(@NonNull File respone);
}