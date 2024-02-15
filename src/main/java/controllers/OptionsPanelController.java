package main.java.controllers;

import burp.api.montoya.MontoyaApi;
import main.java.utils.InfoDialog;
import main.java.utils.event.CrawEventHandler;
import main.java.models.OptionsModel;
import main.java.models.ProxyLogTableModel;
import main.java.views.OptionsPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsPanelController extends CrawEventHandler {
    private final OptionsPanel optionsPanel;
    public OptionsPanel getOptionsPanel() {
        return this.optionsPanel;
    }

    private OptionsModel optionsModel;
    private ProxyLogTableModel tableModel;
    private final MontoyaApi api;

    public OptionsPanelController(MontoyaApi api, OptionsModel optionsModel, ProxyLogTableModel tableModel) {
       this.api = api;
       this.optionsModel = optionsModel;
       this.tableModel = tableModel;
       this.optionsPanel = new OptionsPanel(this.tableModel,this.optionsModel);
       this.optionsPanel.button2.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
                fireCrawEvent(e.getSource());
           }
       });
    }


}
