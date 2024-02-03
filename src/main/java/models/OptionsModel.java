package main.java.models;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class OptionsModel {
    public String currentRequestName;
    public Vector<String> requestNameHistory;

    public boolean isProxying;

    List<String> sendUrls;

    public OptionsModel() {
        this.currentRequestName = "";
        this.requestNameHistory = new Vector<>();
        this.isProxying = false;
        this.sendUrls = new ArrayList<String>();
    }

    public void addHistory() {
        this.requestNameHistory.add(this.currentRequestName);
    }
}
