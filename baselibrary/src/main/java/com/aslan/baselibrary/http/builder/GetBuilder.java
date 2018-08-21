package com.aslan.baselibrary.http.builder;

import com.aslan.baselibrary.http.request.GetRequest;
import com.aslan.baselibrary.http.request.RequestCall;

public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> {

  @Override
  public RequestCall build() {
    url = iniUrl(url, path, paramsUrl);
    return new GetRequest(url, tag, headers, id).build();
  }
}
