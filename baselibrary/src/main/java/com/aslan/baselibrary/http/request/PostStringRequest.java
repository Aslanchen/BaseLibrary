package com.aslan.baselibrary.http.request;

import java.util.Map;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostStringRequest extends OkHttpRequest {

  private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("application/json;charset=utf-8");

  private String content;
  private MediaType mediaType;

  public PostStringRequest(String url, Object tag, Map<String, String> headers, String content,
      MediaType mediaType) {
    super(url, tag, headers);
    this.content = content;
    this.mediaType = mediaType;

    if (this.content == null) {
      throw new IllegalArgumentException("the content can not be null !");
    }
    if (this.mediaType == null) {
      this.mediaType = MEDIA_TYPE_PLAIN;
    }
  }

  @Override
  protected RequestBody buildRequestBody() {
    return RequestBody.create(mediaType, content);
  }

  @Override
  protected Request buildRequest(RequestBody requestBody) {
    return builder.post(requestBody).build();
  }
}
