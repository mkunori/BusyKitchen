import java.util.concurrent.BlockingQueue;

/**
 * コックを表すクラス
 * 
 * Runnable を実装し、別スレッドで動作する。
 * 注文キューから料理を取り出して調理する。
 */
public class Cook implements Runnable {

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

                if (order.isEndSignal()) {
                    System.out.println(cookName + " は営業終了します。");
                    break;
                }

                System.out.println(cookName + " が調理開始: "
                        + "注文" + order.orderNo() + " "
                        + order.dishName());

                Thread.sleep(order.cookTimeMillis());

                System.out.println(cookName + " が調理完了: "
                        + "注文" + order.orderNo() + " "
                        + order.dishName());
            }
        } catch (InterruptedException e) {
            System.out.println(cookName + " の調理が中断されました。");
            Thread.currentThread().interrupt();
        }
    }
}