package main.java.controllers;

import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.views.MainTab;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import main.java.views.OptionsPanel;
import main.java.views.ProxyLogPanel;

public class MainTabController {
  private final MontoyaApi api;
  private final ProxyLogTableModel tableModel;
  private final MainTab mainTab;

  public MainTabController(
          MontoyaApi api,
          ProxyLogTableModel tableModel,
          OptionsPanel optionsPanel,
          ProxyLogPanel proxyLogPanel
  ) {
    this.api = api;
    this.tableModel = tableModel;
    this.mainTab = new MainTab(api, tableModel, optionsPanel, proxyLogPanel);
  }

  public MainTab getMainTab() {
    return this.mainTab;
  }
}
