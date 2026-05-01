/**
 * コックを表すクラスです。
 *
 * Runnable を実装し、別スレッドで動作します。
 * 注文キューから料理を取り出して調理します。
 */
public class Cook implements Runnable {

    private final String cookName;
    private final OrderQueue orderQueue;
    private volatile CookStatus status = CookStatus.WAITING;

    /**
     * コックを作成します。
     *
     * @param cookName   コック名
     * @param orderQueue 注文キュー
     */
    public Cook(String cookName, OrderQueue orderQueue) {
        this.cookName = cookName;
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                status = CookStatus.WAITING;
                Order order = orderQueue.takeOrder();

                if (order.isEndSignal()) {
                    status = CookStatus.STOPPED;
                    System.out.println(cookName + " は営業終了します。");
                    break;
                }

                status = CookStatus.COOKING;

                System.out.println(cookName + " が調理開始: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());

                Thread.sleep(order.menuItem().getCookTimeMillis());

                System.out.println(cookName + " が調理完了: "
                        + "注文" + order.orderNo() + " "
                        + order.menuItem().getDisplayName());
            }
        } catch (InterruptedException e) {
            status = CookStatus.STOPPED;
            System.out.println(cookName + " の調理が中断されました。");
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