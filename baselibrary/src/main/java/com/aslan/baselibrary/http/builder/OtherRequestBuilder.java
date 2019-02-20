package com.aslan.baselibrary.http.builder;

import com.aslan.baselibrary.http.request.OtherRequest;
import com.aslan.baselibrary.http.request.RequestCall;
import okhttp3.RequestBody;

/**
 * DELETE、PUT、PATCH等其他方法
 */
public class OtherRequestBuilder extends OkHttpRequestBuilder<OtherRequestBuilder> {

  private RequestBody requestBody;
  private String method;
  private String content;

  public OtherRequestBuilder(String method) {
    this.method = method;
  }

  @Override
  public RequestCall buildRequestCall() {
    url = iniUrl(url, path, paramsUrl);
    return new OtherRequest(requestBody, content, method, url, tag, headers)
        .build();
  }

  public OtherRequestBuilder requestBody(RequestBody requestBody) {
    this.requestBody = requestBody;
    return this;
  }

  public OtherRequestBuilder requestBody(String content) {
    this.content = content;
    return this;
  }
}
