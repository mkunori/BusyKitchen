/**
 * お客さんを表すクラスです。
 *
 * Runnable を実装し、別スレッドで注文を行います。
 */
public class Customer implements Runnable {

    private final String customerName;
    private final OrderQueue orderQueue;
    private final Order order;

    /**
     * お客さんを作成します。
     *
     * @param customerName お客さんの名前
     * @param orderQueue   注文キュー
     * @param order        注文する内容
     */
    public Customer(String customerName, OrderQueue orderQueue, Order order) {
        this.customerName = customerName;
        this.orderQueue = orderQueue;
        this.order = order;
    }

    @Override
    public void run() {
        try {
            System.out.println(customerName + " が注文しました: "
                    + "注文" + order.orderNo() + " "
                    + order.menuItem().getDisplayName());

            orderQueue.addOrder(order);
        } catch (InterruptedException e) {
            System.out.println(customerName + " の注文が中断されました。");
            Thread.currentThread().interrupt();
        }
    }
}