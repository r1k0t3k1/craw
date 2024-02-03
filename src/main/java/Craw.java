package main.java;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.sun.tools.javac.Main;
import main.java.controllers.MainTabController;
import main.java.controllers.ProxyLogPanelController;
import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.views.MainTab;
import main.java.views.ProxyLogPanel;

public class Craw implements BurpExtension {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("Craw");
        montoyaApi.logging().logToOutput("Craw extension loaded successfully.");

        ProxyLogTableModel tableModel = new ProxyLogTableModel();
        OptionsModel optionsModel = new OptionsModel();

        var proxyLogPanelController = new ProxyLogPanelController(montoyaApi, tableModel, optionsModel);
        var mainTabController = new MainTabController(montoyaApi, tableModel, optionsModel, proxyLogPanelController.getProxyLogPanel());
        montoyaApi.http().registerHttpHandler(proxyLogPanelController);
        montoyaApi.userInterface().registerSuiteTab("Craw", mainTabController.getMainTab().$$$getRootComponent$$$());
    }
}
