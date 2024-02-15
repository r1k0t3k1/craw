package main.java;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import main.java.controllers.MainTabController;
import main.java.controllers.OptionsPanelController;
import main.java.controllers.ProxyLogPanelController;
import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.utils.InfoDialog;
import main.java.utils.event.CrawEvent;
import main.java.utils.event.ICrawEventListener;

public class Craw implements BurpExtension, ICrawEventListener {
    @Override
    public void initialize(MontoyaApi montoyaApi) {
        montoyaApi.extension().setName("Craw");
        montoyaApi.logging().logToOutput("Craw extension loaded successfully.");
        montoyaApi.logging().logToOutput(String.valueOf(montoyaApi.burpSuite().version()));

        ProxyLogTableModel tableModel = new ProxyLogTableModel();
        OptionsModel optionsModel = new OptionsModel();

        var proxyLogPanelController = new ProxyLogPanelController(montoyaApi, tableModel, optionsModel);

        var optionsPanelController = new OptionsPanelController(montoyaApi, optionsModel, tableModel);
        optionsPanelController.addEventListener(new ICrawEventListener() {
            @Override
            public void onCrawEvent(CrawEvent crawEvent) {
                InfoDialog.showDialog(montoyaApi.userInterface().swingUtils().suiteFrame(), "onCrawEvent fired");
            }
        });
        var mainTabController = new MainTabController(
                montoyaApi,
                tableModel,
                optionsPanelController.getOptionsPanel(),
                proxyLogPanelController.getProxyLogPanel()
        );

        montoyaApi.http().registerHttpHandler(proxyLogPanelController);
        montoyaApi.userInterface().registerSuiteTab("Craw", mainTabController.getMainTab().$$$getRootComponent$$$());
    }

    @Override
    public void onCrawEvent(CrawEvent crawEvent) {

    }
}
