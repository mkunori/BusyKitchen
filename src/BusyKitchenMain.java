import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BusyKitchen の起動クラス
 *
 * 注文キューを作成し、コックを起動し、注文を登録する
 */
public class BusyKitchenMain {

    /**
     * アプリケーションのエントリポイント
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

        orderQueue.put(new Order(1, "ラーメン", 5000));
        orderQueue.put(new Order(2, "餃子", 2000));
        orderQueue.put(new Order(3, "チャーハン", 3000));
        orderQueue.put(new Order(4, "カレー", 4000));
        orderQueue.put(new Order(5, "うどん", 2500));

        // コック2人分の終了シグナルを入れる。
        orderQueue.put(Order.createEndSignal());
        orderQueue.put(Order.createEndSignal());

        cook1.join();
        cook2.join();

        System.out.println("=== BusyKitchen 閉店 ===");
    }
}