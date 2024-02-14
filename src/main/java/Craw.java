package main.java;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import main.java.controllers.MainTabController;
import main.java.controllers.ProxyLogPanelController;
import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;

public class Craw implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("Craw");
        montoyaApi.logging().logToOutput("Craw extension loaded successfully.");
        montoyaApi.logging().logToOutput(String.valueOf(montoyaApi.burpSuite().version()));

        ProxyLogTableModel tableModel = new ProxyLogTableModel();
        OptionsModel optionsModel = new OptionsModel();

        var proxyLogPanelController = new ProxyLogPanelController(montoyaApi, tableModel, optionsModel);
        var mainTabController = new MainTabController(montoyaApi, tableModel, optionsModel, proxyLogPanelController.getProxyLogPanel());
        montoyaApi.http().registerHttpHandler(proxyLogPanelController);
        montoyaApi.userInterface().registerSuiteTab("Craw", mainTabController.getMainTab().$$$getRootComponent$$$());
    }
}
