package main.java.controllers;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.views.ProxyLogPanel;

public class ProxyLogPanelController implements HttpHandler {
    private ProxyLogPanel proxyLogPanel;
    public ProxyLogPanel getProxyLogPanel() {
        return this.proxyLogPanel;
    }
    private MontoyaApi api;
    private ProxyLogTableModel tableModel;
    private OptionsModel optionsModel;
    public ProxyLogPanelController(MontoyaApi api, ProxyLogTableModel tableModel, OptionsModel optionsModel) {
        this.api = api;
        this.tableModel = tableModel;
        this.optionsModel = optionsModel;
        this.proxyLogPanel = new ProxyLogPanel(this.api, this.tableModel);
    }

    @Override
    public RequestToBeSentAction handleHttpRequestToBeSent(HttpRequestToBeSent requestToBeSent) {
        return RequestToBeSentAction.continueWith(requestToBeSent);
    }

    @Override
    public ResponseReceivedAction handleHttpResponseReceived(HttpResponseReceived responseReceived) {
        if (this.optionsModel.isProxying) {
            this.optionsModel.addHistory();
            this.tableModel.add(this.optionsModel.currentRequestName, responseReceived);
        }
        return ResponseReceivedAction.continueWith(responseReceived);
    }
}
