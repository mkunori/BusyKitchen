package busykitchen.log;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * GUIのログ欄へログを出力するクラスです。
 */
public class GuiKitchenLogger implements KitchenLogger {

    private final JTextArea logArea;

    /**
     * GUIログ出力を作成します。
     *
     * @param logArea ログを表示するテキストエリア
     */
    public GuiKitchenLogger(JTextArea logArea) {
        this.logArea = logArea;
    }

    @Override
    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + System.lineSeparator());
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}