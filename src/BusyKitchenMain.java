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
        KitchenLogger logger = new KitchenLogger();

        OrderQueue orderQueue = new OrderQueue();

        List<Cook> cooks = List.of(
                new Cook("Cook-A", orderQueue, logger),
                new Cook("Cook-B", orderQueue, logger));

        List<Order> orders = createRandomOrders(10);

        List<Customer> customers = createCustomers(orders, orderQueue, logger);

        int threadPoolSize = cooks.size() + customers.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        KitchenMonitor monitor = new KitchenMonitor(cooks, logger);
        Thread monitorThread = new Thread(monitor, "KitchenMonitor");

        logger.log("=== BusyKitchen 開店 ===");

        monitorThread.start();

        for (Cook cook : cooks) {
            executor.submit(cook);
        }

        List<Future<?>> customerFutures = new ArrayList<>();

        for (Customer customer : customers) {
            Future<?> future = executor.submit(customer);
            customerFutures.add(future);
        }

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

        monitor.stop();
        monitorThread.interrupt();
        monitorThread.join();

        logger.log("=== BusyKitchen 閉店 ===");
    }

    /**
     * 注文リストからお客さんのリストを作成します。
     *
     * @param orders     注文リスト
     * @param orderQueue 注文キュー
     * @param logger     ロガー
     * @return お客さんのリスト
     */
    private static List<Customer> createCustomers(
            List<Order> orders, OrderQueue orderQueue, KitchenLogger logger) {
        return orders.stream()
                .map(order -> new Customer(
                        "Customer-" + order.orderNo(),
                        orderQueue,
                        order,
                        logger))
                .toList();
    }

    /**
     * 指定された件数のランダム注文を作成します。
     *
     * @param orderCount 作成する注文数
     * @return ランダム注文のリスト
     */
    private static List<Order> createRandomOrders(int orderCount) {
        List<Order> orders = new ArrayList<>();

        for (int i = 1; i <= orderCount; i++) {
            orders.add(new Order(i, MenuItem.randomItem()));
        }

        return orders;
    }
}