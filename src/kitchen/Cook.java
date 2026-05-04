package kitchen;

import domain.CookStatus;
import domain.Order;
import log.KitchenLogger;
import snapshot.CookSnapshot;

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
    private volatile Order currentOrder;

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
                    currentOrder = null;
                    logger.log(cookName + " は営業終了します。");
                    break;
                }

                status = CookStatus.COOKING;
                currentOrder = order;

                logger.log(cookName + " が調理開始: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());

                Thread.sleep(order.menuItem().getCookTimeMillis());

                logger.log(cookName + " が調理完了: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());

                currentOrder = null;
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

    /**
     * 現在調理中の注文を返します。
     *
     * 調理中でない場合は null を返します。
     *
     * @return 現在調理中の注文
     */
    public Order getCurrentOrder() {
        return currentOrder;
    }

    /**
     * 現在の状態をスナップショットとして取得します。
     *
     * @return コックスナップショット
     */
    public CookSnapshot snapshot() {
        return new CookSnapshot(cookName, status, currentOrder);
    }
}