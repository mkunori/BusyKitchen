/**
 * BusyKitchen のログ出力を担当するクラスです。
 *
 * 現在はコンソールへ出力します。
 * 将来的には GUI のログ表示へ差し替えることも想定しています。
 */
public class KitchenLogger {

    /**
     * メッセージをログとして出力します。
     *
     * @param message 出力するメッセージ
     */
    public void log(String message) {
        System.out.println(message);
    }
}