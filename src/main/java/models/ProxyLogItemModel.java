package main.java.models;

import java.awt.*;
import java.util.UUID;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import burp.api.montoya.http.message.Cookie;
import burp.api.montoya.http.message.MimeType;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;
import burp.api.montoya.http.handler.HttpResponseReceived;


public class ProxyLogItemModel {
  public HttpResponseReceived httpResponse;
  public UUID id;

  public int order;

  public String requestName;

  public String host;

  public String method;

  public String url;

  public int paramCount;

  public short statusCode;

  public int responseSize;

  public MimeType mimeType;

  public String extension;

  public String note;

  public boolean isSecure;

  public List<Cookie> cookies;

  public Date time;

  public boolean isTarget;

  public boolean isCommit;

  public LinkedList<Integer> duplicateRequests;

  public LinkedList<Integer> similarRequests;

  public Color color = null;

  public ProxyLogItemModel(int order) {
    this.id = UUID.randomUUID();
    this.order = order;
    this.requestName = "";
    this.host = "";
    this.method = "";
    this.url = "https://";
    this.paramCount = 0;
    this.statusCode = 0;
    this.responseSize = 0;
    this.mimeType = MimeType.NONE;
    this.extension = "";
    this.note = "";
    this.isSecure = false;
    this.cookies = new ArrayList();
    this.time = new Date();
    this.isTarget = false;
    this.isCommit = false;
    this.duplicateRequests = new LinkedList<Integer>();
    this.similarRequests = new LinkedList<Integer>();
  }

  public ProxyLogItemModel(int order, String requestName, HttpResponseReceived response) {
    this.httpResponse = response;
    var request = this.httpResponse.initiatingRequest();
    this.id = UUID.randomUUID();
    this.order = order;
    this.requestName = requestName;
    this.host = request.httpService().host();
    this.method = request.method();
    this.url = request.url();
    this.paramCount = request.parameters().size();
    this.statusCode = response.statusCode();
    this.responseSize = response.toByteArray().length();
    this.mimeType = response.mimeType();
    this.extension = request.url(); //request.fileExtension();
    this.note = "";
    this.isSecure = request.httpService().secure();
    this.cookies = response.cookies();
    this.time = new Date();
    this.isTarget = false;
    this.isCommit = false;
    this.duplicateRequests = new LinkedList<Integer>();
    this.similarRequests = new LinkedList<Integer>();
  }

  public HttpRequest getHttpRequest() {
    return this.httpResponse.initiatingRequest();
  }

  public HttpResponse getHttpResponse() {
    return this.httpResponse;
  }
}
