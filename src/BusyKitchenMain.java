import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BusyKitchen の起動クラスです。
 *
 * 注文キューを作成し、コックとお客さんを起動します。
 */
public class BusyKitchenMain {

    /**
     * アプリケーションの起動点です。
     *
     * @param args コマンドライン引数
     * @throws InterruptedException スレッド待機中に割り込まれた場合
     */
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

        List<Thread> cooks = List.of(
                new Thread(new Cook("Cook-A", orderQueue)),
                new Thread(new Cook("Cook-B", orderQueue)));

        List<Order> orders = List.of(
                new Order(1, MenuItem.RAMEN),
                new Order(2, MenuItem.GYOZA),
                new Order(3, MenuItem.FRIED_RICE),
                new Order(4, MenuItem.CURRY),
                new Order(5, MenuItem.UDON));

        List<Thread> customers = createCustomerThreads(orders, orderQueue);

        System.out.println("=== BusyKitchen 開店 ===");

        for (Thread cook : cooks) {
            cook.start();
        }

        for (Thread customer : customers) {
            customer.start();
        }

        for (Thread customer : customers) {
            customer.join();
        }

        for (int i = 0; i < cooks.size(); i++) {
            orderQueue.put(Order.createEndSignal());
        }

        for (Thread cook : cooks) {
            cook.join();
        }

        System.out.println("=== BusyKitchen 閉店 ===");
    }

    /**
     * 注文リストからお客さんスレッドのリストを作成します。
     *
     * @param orders     注文リスト
     * @param orderQueue 注文キュー
     * @return お客さんスレッドのリスト
     */
    private static List<Thread> createCustomerThreads(
            List<Order> orders, BlockingQueue<Order> orderQueue) {
        List<Thread> customers = new ArrayList<>();

        for (int i = 0; i < orders.size(); i++) {
            String customerName = "Customer-" + (i + 1);
            customers.add(new Thread(new Customer(customerName, orderQueue, orders.get(i))));
        }

        return customers;
    }
}