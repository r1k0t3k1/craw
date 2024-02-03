package main.java.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ProxyLogJsonModel {
    private final List<ProxyLogItemJsonModel> proxyLog;

    public ProxyLogJsonModel(ProxyLogTableModel tableModel) {
        this.proxyLog = new ArrayList<>();
        IntStream.range(0, tableModel.getRowCount()).forEach(rowIndex -> {
            proxyLog.add(new ProxyLogItemJsonModel(tableModel.getRow(rowIndex)));
        });
    }

    public List<ProxyLogItemJsonModel> getProxyLog() {
        return proxyLog;
    }
}
