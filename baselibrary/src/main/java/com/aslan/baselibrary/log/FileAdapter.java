package com.aslan.baselibrary.log;

import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.File;

/**
 * 本地文件
 */
public class FileAdapter implements LogAdapter {

  private WriteLogHandler handler;
  private String faterFolder;

  public FileAdapter(String faterFolder) {
    this.faterFolder = faterFolder;

    HandlerThread handlerThread = new HandlerThread("logFile");
    handlerThread.setPriority(HandlerThread.MIN_PRIORITY);
    handlerThread.start();
    handler = new WriteLogHandler(handlerThread.getLooper());
  }

  public void setFaterFolder(String faterFolder) {
    this.faterFolder = faterFolder;
  }

  @Override
  public boolean isLoggable(int priority, @Nullable String tag) {
    return true;
  }

  @Override
  public void log(int priority, @Nullable String tag, @NonNull String message) {
    Bundle bundle = new Bundle();
    bundle.putString(WriteLogHandler.TAG_FOLDER, faterFolder + File.separator + tag);
    bundle.putString(WriteLogHandler.TAG_MESSAGE, message);
    Message messageHandler = Message.obtain();
    messageHandler.setData(bundle);
    handler.handleMessage(messageHandler);
  }
}