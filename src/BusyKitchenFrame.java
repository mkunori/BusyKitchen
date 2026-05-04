import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * BusyKitchen のGUI画面です。
 *
 * コックの状態をテーブルで表示します。
 */
public class BusyKitchenFrame extends JFrame {

    private final KitchenTableModel tableModel;

    /**
     * BusyKitchenFrame を作成します。
     *
     * @param tableModel キッチン状態を表示するテーブルモデル
     */
    public BusyKitchenFrame(KitchenTableModel tableModel) {
        this.tableModel = tableModel;

        setTitle("BusyKitchen Monitor");
        setSize(600, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
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
}