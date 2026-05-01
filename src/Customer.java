/**
 * お客さんを表すクラスです。
 *
 * Runnable を実装し、別スレッドで注文を行います。
 */
public class Customer implements Runnable {

    private final String customerName;
    private final OrderQueue orderQueue;
    private final Order order;
    private final KitchenLogger logger;

    /**
     * お客さんを作成します。
     *
     * @param customerName お客さんの名前
     * @param orderQueue   注文キュー
     * @param order        注文する内容
     * @param logger       ロガー
     */
    public Customer(String customerName, OrderQueue orderQueue, Order order, KitchenLogger logger) {
        this.customerName = customerName;
        this.orderQueue = orderQueue;
        this.order = order;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            logger.log(customerName + " が注文しました: "
                    + "注文" + order.orderNo() + " "
                    + order.menuItem().getDisplayName());

            orderQueue.addOrder(order);
        } catch (InterruptedException e) {
            logger.log(customerName + " の注文が中断されました。");
            Thread.currentThread().interrupt();
        }
    }
}