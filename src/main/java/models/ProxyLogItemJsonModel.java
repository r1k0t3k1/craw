package main.java.models;

import burp.api.montoya.http.handler.HttpResponseReceived;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.MimeType;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.http.message.responses.HttpResponse;

import java.awt.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class ProxyLogItemJsonModel {
    private final HttpResponseReceived httpResponse;
    private final UUID id;

    private final int order;

    private final String requestName;

    private final String host;

    private final String method;

    private final String url;

    private final int paramCount;

    private final short statusCode;

    private final int responseSize;

    private final MimeType mimeType;

    private final String extension;

    private final String note;

    private final boolean isSecure;

    //private List<Cookie> cookies;

    private final Date time;

    private final boolean isTarget;

    private final boolean isCommit;

    //private LinkedList<Integer> duplicateRequests;

    //private LinkedList<Integer> similarRequests;

    private Color color = null;

    public ProxyLogItemJsonModel(ProxyLogItemModel itemModel) {
        this.httpResponse = itemModel.httpResponse;
        this.id = itemModel.id;
        this.order = itemModel.order;
        this.requestName = itemModel.requestName;
        this.host = itemModel.host;
        this.method = itemModel.method;
        this.url = itemModel.url;
        this.paramCount = itemModel.paramCount;
        this.statusCode = itemModel.statusCode;
        this.responseSize = itemModel.responseSize;
        this.mimeType = itemModel.mimeType;
        this.extension = itemModel.extension;
        this.note = itemModel.note;
        this.isSecure = itemModel.isSecure;
        this.time = itemModel.time;
        this.isTarget = itemModel.isTarget;
        this.isCommit = itemModel.isCommit;
        this.color = itemModel.color;
    }

    public HttpResponseReceived getHttpResponse() {
        return httpResponse;
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
        return host;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public int getParamCount() {
        return paramCount;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public MimeType getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getNote() {
        return note;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public boolean isCommit() {
        return isCommit;
    }

    public Date getTime() {
        return time;
    }

    public Color getColor() {
        return color;
    }
}
