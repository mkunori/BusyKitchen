package log;

/**
 * コンソールへログを出力するクラスです。
 */
public class ConsoleKitchenLogger implements KitchenLogger {

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}