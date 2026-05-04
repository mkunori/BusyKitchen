package gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import domain.Order;
import snapshot.CookSnapshot;
import snapshot.KitchenSnapshot;

/**
 * キッチン状態を JTable に表示するためのテーブルモデルです。
 */
public class KitchenTableModel extends AbstractTableModel {

    private final String[] columnNames = { "コック名", "状態", "現在の注文" };
    private List<CookSnapshot> cookSnapshots = new ArrayList<>();

    /**
     * 表示するスナップショットを更新します。
     *
     * @param kitchenSnapshot キッチン全体の状態
     */
    public void updateSnapshot(KitchenSnapshot kitchenSnapshot) {
        this.cookSnapshots = kitchenSnapshot.cookSnapshots();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return cookSnapshots.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CookSnapshot snapshot = cookSnapshots.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> snapshot.cookName();
            case 1 -> snapshot.status();
            case 2 -> formatCurrentOrder(snapshot.currentOrder());
            default -> "";
        };
    }

    /**
     * 現在の注文を画面表示用の文字列に変換します。
     *
     * @param order 現在の注文
     * @return 表示用文字列
     */
    private String formatCurrentOrder(Order order) {
        if (order == null) {
            return "-";
        }

        return "注文" + order.orderNo() + " " + order.menuItem().getDisplayName();
    }
}