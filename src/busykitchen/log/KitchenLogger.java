package busykitchen.log;

/**
 * BusyKitchen のログ出力を表すインターフェースです。
 *
 * 出力先をコンソールやGUIなどに切り替えられるようにします。
 */
public interface KitchenLogger {

    /**
     * メッセージをログとして出力します。
     *
     * @param message 出力するメッセージ
     */
    void log(String message);
}