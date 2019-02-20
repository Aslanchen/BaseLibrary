package com.aslan.baselibrary.http.builder;

import com.aslan.baselibrary.http.request.PostStringRequest;
import com.aslan.baselibrary.http.request.RequestCall;
import okhttp3.MediaType;

public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {

  private String content;
  private MediaType mediaType;

  public PostStringBuilder content(String content) {
    this.content = content;
    return this;
  }

  public PostStringBuilder mediaType(MediaType mediaType) {
    this.mediaType = mediaType;
    return this;
  }

  @Override
  public RequestCall buildRequestCall() {
    url = iniUrl(url, path, paramsUrl);
    return new PostStringRequest(url, tag, headers, content, mediaType).build();
  }
}
