package main.java.models;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpResponseReceived;
import com.google.gson.Gson;
import main.java.utils.JsonConverter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class ProxyLogTableModel extends AbstractTableModel
{
    private final ArrayList<ProxyLogItemModel> log;

    public ProxyLogTableModel()
    {
        this.log = new ArrayList<>();
    }

    @Override
    public synchronized int getRowCount()
    {
        return log.size();
    }

    @Override
    public int getColumnCount()
    {
        return 19;
    }

    @Override
    public String getColumnName(int column)
    {
        return switch (column)
                {
                    case 0 -> "#";
                    case 1 -> "Request name";
                    case 2 -> "Host";
                    case 3 -> "Method";
                    case 4 -> "URL";
                    case 5 -> "Param count";
                    case 6 -> "Status code";
                    case 7 -> "Response size";
                    case 8 -> "MIME type";
                    case 9 -> "Extension";
                    case 10 -> "Notes";
                    case 11 -> "TLS";
                    case 12 -> "Cookies";
                    case 13 -> "Time";
                    case 14 -> "Target?";
                    case 15 -> "Commit?";
                    case 16 -> "Dup?";
                    case 17 -> "Sim?";
                    default -> "";
                };
    }

    public int getColumnWidth(int column)
    {
        return switch (column)
                {
                    case 0 -> 40;
                    case 1 -> 200;
                    case 2 -> 100;
                    case 3 -> 50;
                    case 4 -> 300;
                    case 5 -> 50;
                    case 6 -> 30;
                    case 7 -> 40;
                    case 8 -> 50;
                    case 9 -> 50;
                    case 10 -> 50;
                    case 11 -> 100;
                    case 12 -> 50;
                    case 13 -> 30;
                    case 14 -> 50;
                    case 15 -> 100;
                    case 16 -> 75;
                    case 17 -> 50;
                    default -> 50;
                };
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex)
    {
        var proxyLogItem = log.get(rowIndex);

        return switch (columnIndex)
                {
                    case 0 -> proxyLogItem.order;
                    case 1 -> proxyLogItem.requestName;
                    case 2 -> proxyLogItem.host;
                    case 3 -> proxyLogItem.method;
                    case 4 -> proxyLogItem.url;
                    case 5 -> proxyLogItem.paramCount;
                    case 6 -> proxyLogItem.statusCode;
                    case 7 -> proxyLogItem.responseSize;
                    case 8 -> proxyLogItem.mimeType;
                    case 9 -> proxyLogItem.extension;
                    case 10 -> proxyLogItem.note;
                    case 11 -> proxyLogItem.isSecure;
                    case 12 -> proxyLogItem.cookies;
                    case 13 -> proxyLogItem.time;
                    case 14 -> proxyLogItem.isTarget;
                    case 15 -> proxyLogItem.isCommit;
                    case 16 -> !proxyLogItem.duplicateRequests.isEmpty();
                    case 17 -> !proxyLogItem.similarRequests.isEmpty();
                    default -> "";
                };
    }

    public synchronized void add(String requestName, HttpResponseReceived responseReceived)
    {
        int index = log.size();
        log.add(new ProxyLogItemModel(index+1, requestName, responseReceived));
        fireTableRowsInserted(index, index);
    }

    public synchronized ProxyLogItemModel getRow(int rowIndex)
    {
        return log.get(rowIndex);
    }

    public synchronized List<ProxyLogItemModel> getRows(int[] rowIndexes)
    {
        return log.stream().filter(l ->
                Arrays.stream(rowIndexes).anyMatch(r -> r == l.order))
                .collect(Collectors.toList());
    }

    public void exportToFile() {
        var fileChooser = new JFileChooser();
        var selected = fileChooser.showSaveDialog(null);

        if (selected != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var selectedFile = fileChooser.getSelectedFile();
        var bin = JsonConverter.convertO2z(new ProxyLogJsonModel(this));

        try (var w = new FileOutputStream(selectedFile)) {
            w.write(bin);
        } catch (IOException e) {
            //api.logging().logToError(e.getMessage());
        }
    }

    public void importFromFile() {
        // TODO
    }
}

