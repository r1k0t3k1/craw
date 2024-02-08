package main.java.models;

import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import burp.api.montoya.http.HttpService;
import burp.api.montoya.http.message.Cookie;
import burp.api.montoya.http.message.MimeType;
import burp.api.montoya.http.message.params.HttpParameterType;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.http.handler.HttpResponseReceived;
import org.apache.commons.compress.utils.FileNameUtils;


public class ProxyLogItemModel {
  private final HttpRequest request;
  private final HttpResponse response;
  private final HttpService service;
  private final String url;
  public UUID id;
  public int order;
  public String requestName;
  public String note;
  public Date time;
  public boolean isTarget;
  public boolean isCommit;
  public List<Integer> duplicateRequests;
  public List<Integer> similarRequests;
  public Color color;

  public ProxyLogItemModel(int order) {
    this.id = UUID.randomUUID();
    this.order = order;
    this.request = HttpRequest.httpRequest();
    this.response = HttpResponse.httpResponse();
    this.service = HttpService.httpService("http://", 0, false);
    this.url = "http://";
    this.requestName = "";
    this.note = "";
    this.time = new Date();
    this.isTarget = false;
    this.isCommit = false;
    this.duplicateRequests = new ArrayList<Integer>();
    this.similarRequests = new ArrayList<Integer>();
    this.color = null;
  }

  public ProxyLogItemModel(int order, String requestName, HttpResponseReceived response) {
    this.response = response;
    this.request = response.initiatingRequest();
    this.service = response.initiatingRequest().httpService();
    this.url = response.initiatingRequest().url();
    this.id = UUID.randomUUID();
    this.order = order;
    this.requestName = requestName;
    this.note = "";
    this.time = new Date();
    this.isTarget = false;
    this.isCommit = false;
    this.duplicateRequests = new LinkedList<Integer>();
    this.similarRequests = new LinkedList<Integer>();
    this.color = null;
  }

  public ProxyLogItemModel(
          HttpRequest request,
          HttpResponse response,
          HttpService service,
          String url,
          String id,
          int order,
          String requestName,
          String note,
          String time,
          boolean isTarget,
          boolean isCommit,
          Color color,
          List<Integer> duplicateRequests,
          List<Integer> similarRequests

  ) {
    this.request = request;
    this.response = response;
    this.service = service;
    this.url = url;
    this.id = UUID.fromString(id);
    this.order = order;
    this.requestName = requestName;
    this.note = note;
    try {
      this.time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time);
    } catch (Exception e) {
      this.time = new Date();
    }
    this.isTarget = isTarget;
    this.isCommit = isCommit;
    this.duplicateRequests = duplicateRequests;
    this.similarRequests = similarRequests;
    this.color = color;

  }

  public UUID getId() {
    return id;
  }
  public int getOrder() {
    return order;
  }
  public String getRequestName() {
    return requestName;
  }
  public String getHost() {
    return this.service.host();
  }
  public int getPort() {
    return this.service.port();
  }
  public String getUrl() {
    return url;
  }
  public String getUrlExcludeParameters() {
    var sb = new StringBuilder();
    try  {
      var url = new URL(this.url);
      return url.getProtocol() + "://" + url.getAuthority() + this.request.pathWithoutQuery();
    }catch (Exception ignored) {};
      return this.getUrl();
  }
  public String getMethod() {
    return this.request.method();
  }
  public String getPath() {
      return this.request.path();
  }
  public int getParamCount() {
    return this.request.parameters()
            .stream()
            .filter(p -> !p.type().equals(HttpParameterType.COOKIE))
            .toList()
            .size();
  }
  public short getStatusCode() {
    return this.response.statusCode();
  }
  public int getResponseSize() {
    return this.response.toByteArray().length();
  }

  public MimeType getMimeType() {
    return this.response.mimeType();
  }
  public String getExtension() {
    try {
      URL url = new URL(this.request.url());
      return FileNameUtils.getExtension(url.getPath());
    } catch (Exception e) {
      return "";
    }
  }
  public String getNote() {
    return this.note;
  }
  public boolean isSecure() {
    return this.service.secure();
  }
  public List<Cookie> getCookies() {
    return this.response.cookies();
  }
  public Date getTime() {
    return this.time;
  }
  public boolean isTarget() {
    return this.isTarget;
  }
  public void setTarget(boolean target) {
    isTarget = target;
  }
  public boolean isCommit() {
    return this.isCommit;
  }
  public void setCommit(boolean commit) {
    isCommit = commit;
  }
  public List<Integer> getDuplicateRequests() {
    return this.duplicateRequests;
  }
  public List<Integer> getSimilarRequests() {
    return this.similarRequests;
  }
  public Color getColor() {
    return this.color;
  }
  public HttpRequest getHttpRequest() {
    return this.request;
  }
  public HttpResponse getHttpResponse() {
    return this.response;
  }
}
