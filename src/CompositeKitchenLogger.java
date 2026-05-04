import java.util.Arrays;
import java.util.List;

/**
 * 複数のログ出力先へログを転送するクラスです。
 */
public class CompositeKitchenLogger implements KitchenLogger {

    private final List<KitchenLogger> loggers;

    /**
     * 複数のログ出力先をまとめたLoggerを作成します。
     *
     * @param loggers 転送先のLogger
     */
    public CompositeKitchenLogger(KitchenLogger... loggers) {
        this.loggers = Arrays.asList(loggers);
    }

    @Override
    public void log(String message) {
        for (KitchenLogger logger : loggers) {
            logger.log(message);
        }
    }
}