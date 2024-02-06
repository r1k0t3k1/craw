package main.java.views;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import main.java.models.ProxyLogTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ProxyLogPanel {
    private final MontoyaApi api;
    private final ProxyLogTableModel tableModel;
    private JPanel rootPanel;
    private JSplitPane verticalPanel;
    private JTable proxyLogTable;
    private JSplitPane requestResponsePanel;
    private HttpRequestEditor requestEditor;
    private HttpResponseEditor responseEditor;

    private JPopupMenu popupMenu;

    public ProxyLogPanel(MontoyaApi api, ProxyLogTableModel tableModel) {
        this.api = api;
        this.tableModel = tableModel;
        $$$setupUI$$$();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        this.requestResponsePanel = new JSplitPane();
        this.requestEditor = this.api.userInterface().createHttpRequestEditor();
        this.responseEditor = this.api.userInterface().createHttpResponseEditor();
        this.requestResponsePanel.setLeftComponent(this.requestEditor.uiComponent());
        this.requestResponsePanel.setRightComponent(this.responseEditor.uiComponent());
        this.verticalPanel = new JSplitPane();
        this.popupMenu = new JPopupMenu();
        this.popupMenu.add(new JMenuItem("Mark as target"));
        this.popupMenu.add(new JMenuItem("Mark as commit"));
        this.popupMenu.add(new JMenuItem("Copy URL(scheme://domain)"));
        this.popupMenu.add(new JMenuItem("Copy URL(scheme://domain/path)"));
        this.popupMenu.add(new JMenuItem("Copy URL(scheme://domain/path?q=***)"));
        this.popupMenu.add(new JMenuItem("Copy as CSV"));
        this.popupMenu.add(new JMenuItem("Send to ..."));

        var color = new Color[]{
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.BLUE,
                Color.PINK,
                Color.MAGENTA,
                Color.GRAY,
                null,
        };

        Arrays.stream(color).forEach(c -> {
            var item = new JMenuItem("");
            item.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (actionEvent.getSource() == item) {
                        Arrays.stream(proxyLogTable.getSelectedRows()).forEach(x -> {
                            tableModel.getRow(x).color = c;
                        });
                    }
                }
            });
            item.setBackground(c);
            this.popupMenu.add(item);
            item.setOpaque(true);
        });

        this.proxyLogTable = new JTable(this.tableModel) {
            @Override
            public Component prepareRenderer(
                    TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                var v = tableModel.getRow(row);
                var selected = getSelectedRows();
                var selectionColor = v.color == null ? getSelectionBackground() : v.color;
                var color = v.color == null ? getBackground() : v.color;
                if (Arrays.stream(selected).anyMatch(r -> r == row)) {
                    c.setBackground(selectionColor.darker());
                    return c;
                }
                c.setBackground(color);
                return c;
            }

            @Override
            public Class getColumnClass(int column) {
                return switch (column) {
                    case 2, 3, 7, 8, 15 -> Boolean.class;
                    default -> String.class;
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return IntStream.of(2, 3, 7, 8, 15).anyMatch(x -> x == column);
            }

            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                super.changeSelection(rowIndex, columnIndex, toggle, extend);
                var proxyLogItem = tableModel.getRow(rowIndex);
                requestEditor.setRequest(proxyLogItem.getHttpRequest());
                responseEditor.setResponse(proxyLogItem.getHttpResponse());
            }
        };

        for (int i = 0; i < proxyLogTable.getColumnCount(); i++) {
            proxyLogTable.getColumnModel().getColumn(i).setPreferredWidth(tableModel.getColumnWidth(i));
        }


        proxyLogTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1, true, true));
        verticalPanel.setOrientation(0);
        rootPanel.add(verticalPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        verticalPanel.setLeftComponent(scrollPane1);
        scrollPane1.setViewportView(proxyLogTable);
        verticalPanel.setRightComponent(requestResponsePanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
