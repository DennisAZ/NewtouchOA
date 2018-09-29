package yh.core.esb.common.impl;

import org.apache.http.HttpResponse;

public interface MultiThreadRequest extends Runnable {
  public void stopAll();
  public void resume();
  public HttpResponse request() throws Exception;
  public int parseResponse(HttpResponse response);
}
