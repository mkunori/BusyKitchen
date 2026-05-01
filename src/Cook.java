/**
 * コックを表すクラスです。
 *
 * Runnable を実装し、別スレッドで動作します。
 * 注文キューから料理を取り出して調理します。
 */
public class Cook implements Runnable {

    private final String cookName;
    private final OrderQueue orderQueue;

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
                Order order = orderQueue.takeOrder();

                if (order.isEndSignal()) {
                    System.out.println(cookName + " は営業終了します。");
                    break;
                }

                MenuItem menuItem = order.menuItem();

                System.out.println(cookName + " が調理開始: "
                        + "注文" + order.orderNo() + " "
                        + menuItem.getDisplayName());

                Thread.sleep(menuItem.getCookTimeMillis());

                System.out.println(cookName + " が調理完了: "
                        + "注文" + order.orderNo() + " "
                        + menuItem.getDisplayName());
            }
        } catch (InterruptedException e) {
            System.out.println(cookName + " の調理が中断されました。");
            Thread.currentThread().interrupt();
        }
    }
}