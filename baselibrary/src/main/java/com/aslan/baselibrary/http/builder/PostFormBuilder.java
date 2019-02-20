package com.aslan.baselibrary.http.builder;

import com.aslan.baselibrary.http.request.PostFormRequest;
import com.aslan.baselibrary.http.request.RequestCall;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> {

  private List<FileInput> files = new ArrayList<>();
  private Map<String, String> paramsData;

  @Override
  public RequestCall buildRequestCall() {
    url = iniUrl(url, path, paramsUrl);
    return new PostFormRequest(url, tag, paramsData, headers, files).build();
  }

  public PostFormBuilder files(String key, Map<String, File> files) {
    for (String filename : files.keySet()) {
      this.files.add(new FileInput(key, filename, files.get(filename)));
    }
    return this;
  }

  public PostFormBuilder addFile(String name, String filename, File file) {
    files.add(new FileInput(name, filename, file));
    return this;
  }

  public static class FileInput {

    public String key;
    public String filename;
    public File file;

    public FileInput(String name, String filename, File file) {
      this.key = name;
      this.filename = filename;
      this.file = file;
    }

    @Override
    public String toString() {
      return "FileInput{" +
          "key='" + key + '\'' +
          ", filename='" + filename + '\'' +
          ", file=" + file +
          '}';
    }
  }

  public PostFormBuilder addParams(String key, String value) {
    if (this.paramsData == null) {
      this.paramsData = new HashMap<>();
    }
    this.paramsData.put(key, value);
    return this;
  }

  public PostFormBuilder params(Map<String, String> params) {
    this.paramsData = params;
    return this;
  }
}
