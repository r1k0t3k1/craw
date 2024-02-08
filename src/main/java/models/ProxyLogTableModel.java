package main.java.models;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.handler.HttpResponseReceived;
import main.java.utils.InfoDialog;
import main.java.utils.JsonConverter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class ProxyLogTableModel extends AbstractTableModel
{
    private final List<ProxyLogItemModel> log;

    public ProxyLogTableModel(){
        this.log = new ArrayList<>();
    }
    public ProxyLogTableModel(ProxyLogItemModel[] logs)
    {
        this.log = Arrays.stream(logs).toList();
    }

    @Override
    public synchronized int getRowCount()
    {
        return log.size();
    }

    @Override
    public int getColumnCount()
    {
        return 18;
    }

    @Override
    public String getColumnName(int column)
    {
        return switch (column)
                {
                    case 0 -> "#";
                    case 1 -> "Name";
                    case 2 -> "Target?";
                    case 3 -> "Commit?";
                    case 4 -> "Host";
                    case 5 -> "Method";
                    case 6 -> "Path";
                    case 7 -> "Dup?";
                    case 8 -> "Sim?";
                    case 9 -> "Params";
                    case 10 -> "Status";
                    case 11 -> "Size";
                    case 12 -> "MIME";
                    case 13 -> "Ext";
                    case 14 -> "Notes";
                    case 15 -> "TLS";
                    case 16 -> "Cookies";
                    case 17 -> "Time";
                    default -> "";
                };
    }

    public int getColumnWidth(int column)
    {
        return switch (column)
                {
                    case 0, 14, 15, 16, 17 -> 40;
                    case 1, 4 -> 200;
                    case 2, 3, 5 -> 80;
                    case 6 -> 300;
                    case 9, 13 -> 70;
                    case 12 -> 100;
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
                    case 2 -> proxyLogItem.isTarget();
                    case 3 -> proxyLogItem.isCommit();
                    case 4 -> proxyLogItem.getHost();
                    case 5 -> proxyLogItem.getMethod();
                    case 6 -> proxyLogItem.getPath();
                    case 7 -> !proxyLogItem.getDuplicateRequests().isEmpty();
                    case 8 -> !proxyLogItem.getSimilarRequests().isEmpty();
                    case 9 -> proxyLogItem.getParamCount();
                    case 10 -> proxyLogItem.getStatusCode();
                    case 11 -> proxyLogItem.getResponseSize();
                    case 12 -> proxyLogItem.getMimeType();
                    case 13 -> proxyLogItem.getExtension();
                    case 14 -> proxyLogItem.getNote();
                    case 15 -> proxyLogItem.isSecure();
                    case 16 -> proxyLogItem.getCookies().stream().map(c -> c.name()+"="+c.value()).collect(Collectors.joining("; "));
                    case 17 -> proxyLogItem.getTime();
                    default -> "";
                };
    }

    @Override
    public synchronized void setValueAt(Object value, int row, int column) {
        switch (column) {
            case 2 -> getRow(row).setTarget((boolean)value);
            case 3 -> getRow(row).setCommit((boolean)value);
        }
    }

    public synchronized void add(String requestName, HttpResponseReceived responseReceived)
    {
        int index = log.size();
        log.add(new ProxyLogItemModel(index, requestName, responseReceived));
        fireTableRowsInserted(index, index);
    }

    public synchronized void add(ProxyLogItemModel itemModel) {
        log.add(itemModel);
        fireTableRowsInserted(this.getRowCount(), this.getRowCount());
    }

    public synchronized ProxyLogItemModel getRow(int rowIndex)
    {
        return log.get(rowIndex);
    }

    public synchronized List<ProxyLogItemModel> getAllRows() {
        return this.log;
    }

    public synchronized List<ProxyLogItemModel> getRows(int[] rowIndexes)
    {
        // TODO
        return log.stream().filter(l ->
                Arrays.stream(rowIndexes).anyMatch(r -> r == l.order))
                .collect(Collectors.toList());
    }

    public synchronized void addAllRows(List<ProxyLogItemModel> items) {
        this.log.addAll(0, items);
    }

    public void removeRow(int index) {
        if (index >= 0 && index < log.size()) {
           log.remove(index);
           fireTableRowsDeleted(index, index);
        }
    }
    public void removeRows(int[] removeRowIndices) {
        var items = Arrays.stream(removeRowIndices)
                .distinct()
                .mapToObj(log::get)
                .toList();

        removeRows(items);
    }
    public void removeRows(List<ProxyLogItemModel> items) {
        log.removeAll(items);
        fireTableDataChanged();
    }

    public synchronized void removeAllRows() {
        this.log.clear();
        fireTableDataChanged();
    }

    public void exportToFile() {
        var fileChooser = new JFileChooser();
        var selected = fileChooser.showSaveDialog(null);

        if (selected != JFileChooser.APPROVE_OPTION) {
            return;
        }

        var selectedFile = fileChooser.getSelectedFile();
        var bin = JsonConverter.convertLogToSevenZip(this);

        try (var w = new FileOutputStream(selectedFile)) {
            w.write(bin);
        } catch (IOException e) {
            //api.logging().logToError(e.getMessage());
        }
    }

    public ProxyLogTableModel importFromFile() {
        // TODO
        var fileChooser = new JFileChooser();
        var selected = fileChooser.showOpenDialog(null);

        if (selected != JFileChooser.APPROVE_OPTION) {
            return this;
        }

        var selectedFile = fileChooser.getSelectedFile();

        try (var r = new FileInputStream(selectedFile)) {
            var bin = r.readAllBytes();
            return JsonConverter.convertSevenZipToLog(bin);
        } catch (Exception e) {
            return this;
        }
    }

    public String convertSelectedRowsToCsv(int[] selectedRowsIndices) {
        var items = Arrays.stream(selectedRowsIndices)
                .distinct()
                .mapToObj(log::get)
                .toList();

        StringBuilder sb = new StringBuilder();

        for(ProxyLogItemModel item: items) {
            sb.append("\"").append(item.isTarget() ? "○" : "×").append("\"").append("\t");
            sb.append("\"").append(item.getRequestName()).append("\"").append("\t");
            sb.append("\"").append(item.getMethod()).append("\"").append("\t");
            //sb.append("\"").append(item.getUrl()).append("\"").append("\t");
            sb.append("\"").append(item.getUrlExcludeParameters()).append("\"").append("\t");
            sb.append("\"").append(item.getNote()).append("\"").append("\t");
            sb.append("\"").append(item.getParamCount()).append("\"").append("\t");
            sb.append("\"").append(item.isCommit() ? "○" : "×").append("\"").append("\t");
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}

