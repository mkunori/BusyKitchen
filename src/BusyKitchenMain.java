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

        Thread cook1 = new Thread(new Cook("Cook-A", orderQueue));
        Thread cook2 = new Thread(new Cook("Cook-B", orderQueue));

        cook1.start();
        cook2.start();

        System.out.println("=== BusyKitchen 開店 ===");

        Thread customer1 = new Thread(new Customer("Customer-1", orderQueue, new Order(1, MenuItem.RAMEN)));
        Thread customer2 = new Thread(new Customer("Customer-2", orderQueue, new Order(2, MenuItem.GYOZA)));
        Thread customer3 = new Thread(new Customer("Customer-3", orderQueue, new Order(3, MenuItem.FRIED_RICE)));
        Thread customer4 = new Thread(new Customer("Customer-4", orderQueue, new Order(4, MenuItem.CURRY)));
        Thread customer5 = new Thread(new Customer("Customer-5", orderQueue, new Order(5, MenuItem.UDON)));

        customer1.start();
        customer2.start();
        customer3.start();
        customer4.start();
        customer5.start();

        customer1.join();
        customer2.join();
        customer3.join();
        customer4.join();
        customer5.join();

        // コック2人分の終了シグナルを入れる
        orderQueue.put(Order.createEndSignal());
        orderQueue.put(Order.createEndSignal());

        cook1.join();
        cook2.join();

        System.out.println("=== BusyKitchen 閉店 ===");
    }
}