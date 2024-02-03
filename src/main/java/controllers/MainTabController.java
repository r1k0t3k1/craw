package main.java.controllers;

import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.views.MainTab;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.*;
import main.java.views.ProxyLogPanel;

public class MainTabController {
  private final MontoyaApi api;
  private final ProxyLogTableModel tableModel;
  private final OptionsModel optionsModel;
  private final MainTab mainTab;

  public MainTabController(
          MontoyaApi api,
          ProxyLogTableModel tableModel,
          OptionsModel optionsModel,
          ProxyLogPanel proxyLogPanel
  ) {
    this.api = api;
    this.tableModel = tableModel;
    this.optionsModel = optionsModel;
    this.mainTab = new MainTab(api, tableModel, optionsModel, proxyLogPanel);
  }

  public MainTab getMainTab() {
    return this.mainTab;
  }
}
