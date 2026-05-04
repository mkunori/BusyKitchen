package gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import snapshot.KitchenSnapshot;

/**
 * BusyKitchen のGUI画面です。
 *
 * コックの状態とログを表示します。
 */
public class BusyKitchenFrame extends JFrame {

    private final KitchenTableModel tableModel;
    private final JTextArea logArea;

    /**
     * BusyKitchenFrame を作成します。
     *
     * @param tableModel キッチン状態を表示するテーブルモデル
     */
    public BusyKitchenFrame(KitchenTableModel tableModel) {
        this.tableModel = tableModel;
        this.logArea = new JTextArea(8, 40);

        setTitle("BusyKitchen Monitor");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        add(tableScrollPane, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.SOUTH);
    }

    /**
     * キッチン状態を画面に反映します。
     *
     * Swingの画面更新はイベントディスパッチスレッド上で行います。
     *
     * @param kitchenSnapshot キッチン全体の状態
     */
    public void updateSnapshot(KitchenSnapshot kitchenSnapshot) {
        SwingUtilities.invokeLater(() -> tableModel.updateSnapshot(kitchenSnapshot));
    }

    /**
     * ログ表示用のテキストエリアを返します。
     *
     * @return ログ表示用テキストエリア
     */
    public JTextArea getLogArea() {
        return logArea;
    }
}