package busykitchen;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

import busykitchen.domain.MenuItem;
import busykitchen.domain.Order;
import busykitchen.gui.BusyKitchenFrame;
import busykitchen.gui.KitchenTableModel;
import busykitchen.kitchen.Cook;
import busykitchen.kitchen.Customer;
import busykitchen.kitchen.OrderQueue;
import busykitchen.log.CompositeKitchenLogger;
import busykitchen.log.ConsoleKitchenLogger;
import busykitchen.log.GuiKitchenLogger;
import busykitchen.log.KitchenLogger;
import busykitchen.monitor.GuiKitchenMonitor;

/**
 * BusyKitchen の起動クラスです。
 *
 * 注文キューを作成し、コック・お客さん・GUIモニターを起動します。
 */
public class BusyKitchenMain {

    /**
     * アプリケーションの起動点です。
     *
     * @param args コマンドライン引数
     * @throws InterruptedException      スレッド待機中に割り込まれた場合
     * @throws ExecutionException        別スレッドで例外が発生した場合
     * @throws InvocationTargetException Swingの起動処理中に例外が発生した場合
     */
    public static void main(String[] args)
            throws InterruptedException, ExecutionException, InvocationTargetException {
        OrderQueue orderQueue = new OrderQueue();

        KitchenTableModel tableModel = new KitchenTableModel();
        BusyKitchenFrame frame = new BusyKitchenFrame(tableModel);

        SwingUtilities.invokeAndWait(() -> frame.setVisible(true));

        KitchenLogger logger = new CompositeKitchenLogger(
                new ConsoleKitchenLogger(),
                new GuiKitchenLogger(frame.getLogArea()));

        List<Cook> cooks = List.of(
                new Cook("Cook-A", orderQueue, logger),
                new Cook("Cook-B", orderQueue, logger));

        List<Order> orders = createRandomOrders(10);
        List<Customer> customers = createCustomers(orders, orderQueue, logger);

        GuiKitchenMonitor guiMonitor = new GuiKitchenMonitor(cooks, frame);
        Thread guiMonitorThread = new Thread(guiMonitor, "GuiKitchenMonitor");

        int threadPoolSize = cooks.size() + customers.size();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        logger.log("=== BusyKitchen 開店 ===");

        guiMonitorThread.start();

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

        guiMonitor.stop();
        guiMonitorThread.interrupt();
        guiMonitorThread.join();

        logger.log("=== BusyKitchen 閉店 ===");
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

    /**
     * 注文リストからお客さんのリストを作成します。
     *
     * @param orders     注文リスト
     * @param orderQueue 注文キュー
     * @param logger     ログ出力
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
}