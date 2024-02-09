package main.java.utils;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.core.ByteArray;
import burp.api.montoya.http.message.HttpHeader;
import burp.api.montoya.http.message.requests.HttpRequest;
import main.java.models.ProxyLogItemModel;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class MultiPartHttpRequestBuilder extends Thread {
    private final String boundary = UUID.randomUUID().toString();
    private List<HttpRequest> requests;
    private MontoyaApi api;

    public MultiPartHttpRequestBuilder(MontoyaApi api) {
        this.api = api;
        this.requests = new ArrayList<>();
    }

    public MultiPartHttpRequestBuilder setParameter(List<ProxyLogItemModel> itemModels) {
        for (ProxyLogItemModel itemModel : itemModels) {
            var byteArray = ByteArray.byteArray();
            byteArray = byteArray.withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"host\"\r\n").getBytes())
                    .withAppended(("Content-Type: text/plain;\r\n\r\n").getBytes())
                    .withAppended((itemModel.getHost() + "\r\n").getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"port\"\r\n").getBytes())
                    .withAppended(("Content-Type: text/plain;\r\n\r\n").getBytes())
                    .withAppended((itemModel.getPort() + "\r\n").getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"protocol\"\r\n").getBytes())
                    .withAppended(("Content-Type: text/plain;\r\n\r\n").getBytes())
                    .withAppended((itemModel.isSecure() ? "https" : "http" + "\r\n").getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"url\"\r\n").getBytes())
                    .withAppended(("Content-Type: text/plain;\r\n\r\n").getBytes())
                    .withAppended((itemModel.getUrl() + "\r\n").getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"request\"; filename=\"request\"\r\n").getBytes())
                    .withAppended(("Content-Type: application/octet-stream;\r\n\r\n").getBytes())
                    .withAppended(itemModel.getHttpRequest().toByteArray())
                    .withAppended("\r\n".getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"response\"; filename=\"response\"\r\n").getBytes())
                    .withAppended(("Content-Type: application/octet-stream;\r\n\r\n").getBytes())
                    .withAppended(itemModel.getHttpResponse().toByteArray())
                    .withAppended("\r\n".getBytes())

                    .withAppended(("--" + boundary + "\r\n").getBytes())
                    .withAppended(("Content-Disposition: form-data; name=\"reqName\"\r\n").getBytes())
                    .withAppended(("Content-Type: text/plain;\r\n\r\n").getBytes())
                    .withAppended((itemModel.getRequestName() + "\r\n").getBytes())

                    .withAppended("--" + boundary + "--\r\n\r\n");

            this.requests.add(HttpRequest.httpRequestFromUrl("https://neri-tera02.va.mbsd.jp/terascan/tiwasa/1.php/65b72a9a4eaaea002?action=importBurpData")
                    .withMethod("POST")
                    .withAddedHeader(HttpHeader.httpHeader("User-Agent", "BurpSuite-Craw-Extension"))
                    .withAddedHeader(HttpHeader.httpHeader("Content-Type", "multipart/form-data;boundary="+ boundary))
                    .withAddedHeader(HttpHeader.httpHeader("Content-Length", String.valueOf(byteArray.length())))
                    .withBody(byteArray)
            );
        }
        return this;
    }

    public void run() {
        try {
            for (HttpRequest request: this.requests) {
                api.http().sendRequest(request);
            }
        } catch (Exception e) {
            InfoDialog.showDialog(null, e.getMessage());
        }
    }
}
