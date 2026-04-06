import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BusyKitchen
 * 
 * 注文キューに入った料理を、複数のコックが並行して調理する
 * マルチスレッド学習用アプリ
 */
public class BKMain {

    /**
     * 注文を表すクラス
     */
    static class Order {

        /** 注文番号 */
        private final int orderNo;

        /** 料理名 */
        private final String dishName;

        /** 調理時間（ミリ秒） */
        private final int cookTimeMillis;

        /**
         * 注文を生成する。
         * 
         * @param orderNo        注文番号
         * @param dishName       料理名
         * @param cookTimeMillis 調理時間（ミリ秒）
         */
        public Order(int orderNo, String dishName, int cookTimeMillis) {
            this.orderNo = orderNo;
            this.dishName = dishName;
            this.cookTimeMillis = cookTimeMillis;
        }

        /**
         * 注文番号を返す。
         * 
         * @return 注文番号
         */
        public int getOrderNo() {
            return orderNo;
        }

        /**
         * 調理名を返す。
         * 
         * @return 料理名
         */
        public String getDishName() {
            return dishName;
        }

        /**
         * 調理時間を返す。
         * 
         * @return 調理時間（ミリ秒）
         */
        public int getCookTimeMillis() {
            return cookTimeMillis;
        }

        @Override
        public String toString() {
            return "Order{"
                    + "orderNo=" + orderNo
                    + ", dishName='" + dishName + '\''
                    + ", cookTimeMillis=" + cookTimeMillis
                    + '}';
        }
    }

    /**
     * コックを表すクラス
     */
    static class Cook implements Runnable {

        /** コック名 */
        private final String cookName;

        /** 注文キュー */
        private final BlockingQueue<Order> orderQueue;

        /**
         * コックを生成する。
         * 
         * @param cookName   コック名
         * @param orderQueue 注文キュー
         */
        public Cook(String cookName, BlockingQueue<Order> orderQueue) {
            this.cookName = cookName;
            this.orderQueue = orderQueue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Order order = orderQueue.take();

                    // 終了用の特別な注文を受け取ったら終了する。
                    if (order.getOrderNo() == -1) {
                        System.out.println(cookName + " は営業終了します。");
                        break;
                    }

                    System.out.println(cookName + " が調理開始: "
                            + "注文" + order.getOrderNo() + " "
                            + order.getDishName());

                    Thread.sleep(order.getCookTimeMillis());

                    System.out.println(cookName + " が調理完了: "
                            + "注文" + order.getOrderNo() + " "
                            + order.getDishName());
                }
            } catch (InterruptedException e) {
                System.out.println(cookName + " の料理が中断されました。");
                Thread.currentThread().interrupt();
            }
        }

    }

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

        // コック2人分の終了シグナルを入れる
        orderQueue.put(new Order(-1, "END", 0));
        orderQueue.put(new Order(-1, "END", 0));

        cook1.join();
        cook2.join();

        System.out.println("=== BusyKitchen 閉店 ===");
    }
}
