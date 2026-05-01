import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
     * @throws ExecutionException   お客さんスレッドで例外が発生した場合
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        OrderQueue orderQueue = new OrderQueue();

        List<Cook> cooks = List.of(
                new Cook("Cook-A", orderQueue),
                new Cook("Cook-B", orderQueue));

        List<Order> orders = List.of(
                new Order(1, MenuItem.RAMEN),
                new Order(2, MenuItem.GYOZA),
                new Order(3, MenuItem.FRIED_RICE),
                new Order(4, MenuItem.CURRY),
                new Order(5, MenuItem.UDON));

        List<Customer> customers = createCustomers(orders, orderQueue);

        int threadPoolSize = cooks.size() + customers.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        System.out.println("=== BusyKitchen 開店 ===");

        for (Cook cook : cooks) {
            executor.submit(cook);
        }

        List<Future<?>> customerFutures = new ArrayList<>();

        for (Customer customer : customers) {
            Future<?> future = executor.submit(customer);
            customerFutures.add(future);
        }

        // 全てのお客さんが注文し終わるまで待つ
        for (Future<?> future : customerFutures) {
            future.get();
        }

        for (int i = 0; i < cooks.size(); i++) {
            orderQueue.addOrder(Order.createEndSignal());
        }

        executor.shutdown();

        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }

        System.out.println("=== BusyKitchen 閉店 ===");
    }

    /**
     * 注文リストからお客さんのリストを作成します。
     *
     * @param orders     注文リスト
     * @param orderQueue 注文キュー
     * @return お客さんのリスト
     */
    private static List<Customer> createCustomers(List<Order> orders, OrderQueue orderQueue) {
        return orders.stream()
                .map(order -> new Customer(
                        "Customer-" + order.orderNo(),
                        orderQueue,
                        order))
                .toList();
    }
}