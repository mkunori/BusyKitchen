/**
 * コックを表すクラスです。
 *
 * Runnable を実装し、別スレッドで動作します。
 * 注文キューから料理を取り出して調理します。
 */
public class Cook implements Runnable {

    private final String cookName;
    private final OrderQueue orderQueue;
    private final KitchenLogger logger;
    private volatile CookStatus status = CookStatus.WAITING;

    /**
     * コックを作成します。
     *
     * @param cookName   コック名
     * @param orderQueue 注文キュー
     * @param logger     ロガー
     */
    public Cook(String cookName, OrderQueue orderQueue, KitchenLogger logger) {
        this.cookName = cookName;
        this.orderQueue = orderQueue;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            while (true) {
                status = CookStatus.WAITING;
                Order order = orderQueue.takeOrder();

                if (order.isEndSignal()) {
                    status = CookStatus.STOPPED;
                    logger.log(cookName + " は営業終了します。");
                    break;
                }

                status = CookStatus.COOKING;

                logger.log(cookName + " が調理開始: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());

                Thread.sleep(order.menuItem().getCookTimeMillis());

                logger.log(cookName + " が調理完了: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());
            }
        } catch (InterruptedException e) {
            status = CookStatus.STOPPED;
            logger.log(cookName + " の調理が中断されました。");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * コック名を返します。
     *
     * @return コック名
     */
    public String getCookName() {
        return cookName;
    }

    /**
     * 現在の状態を返します。
     *
     * @return コックの現在状態
     */
    public CookStatus getStatus() {
        return status;
    }
}