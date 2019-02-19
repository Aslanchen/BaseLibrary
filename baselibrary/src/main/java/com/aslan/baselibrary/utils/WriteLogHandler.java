package com.aslan.baselibrary.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 写本地日志
 */
public class WriteLogHandler extends Handler {

  @NonNull
  private final String folder;
  private final int maxFileSize;

  public WriteLogHandler(@NonNull Looper looper, @NonNull String folder) {
    super(looper);
    this.folder = folder;
    this.maxFileSize = 10 * 1024 * 1024;
  }

  @Override
  public void handleMessage(@NonNull Message msg) {
    String content = (String) msg.obj;

    FileWriter fileWriter = null;
    File logFile = getLogFile(folder, "logs");

    try {
      fileWriter = new FileWriter(logFile, true);

      writeLog(fileWriter, content);

      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      if (fileWriter != null) {
        try {
          fileWriter.flush();
          fileWriter.close();
        } catch (IOException e1) { /* fail silently */ }
      }
    }
  }

  /**
   * This is always called on a single background thread. Implementing classes must ONLY write to
   * the fileWriter and nothing more. The abstract class takes care of everything else including
   * close the stream and catching IOException
   *
   * @param fileWriter an instance of FileWriter already initialised to the correct file
   */
  private void writeLog(@NonNull FileWriter fileWriter, @NonNull String content)
      throws IOException {
    fileWriter.append(content + System.lineSeparator());
  }

  private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
    File folder = new File(folderName);
    if (!folder.exists()) {
      //TODO: What if folder is not created, what happens then?
      folder.mkdirs();
    }

    int newFileCount = 0;
    File newFile;
    File existingFile = null;

    newFile = new File(folder, String.format("%s_%s.txt", fileName, newFileCount));
    while (newFile.exists()) {
      existingFile = newFile;
      newFileCount++;
      newFile = new File(folder, String.format("%s_%s.txt", fileName, newFileCount));
    }

    if (existingFile != null) {
      if (existingFile.length() >= maxFileSize) {
        return newFile;
      }
      return existingFile;
    }

    return newFile;
  }
}