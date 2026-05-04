package busykitchen.kitchen;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import busykitchen.domain.Order;

/**
 * 注文を管理するキュークラスです。
 *
 * スレッド間で安全に注文を受け渡します。
 */
public class OrderQueue {

    private final BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    /**
     * 注文をキューに追加します。
     *
     * @param order 注文
     * @throws InterruptedException 割り込み時
     */
    public void addOrder(Order order) throws InterruptedException {
        queue.put(order);
    }

    /**
     * 注文をキューから取り出します。
     *
     * 注文がない場合は、注文が追加されるまで待機します。
     *
     * @return 注文
     * @throws InterruptedException 割り込み時
     */
    public Order takeOrder() throws InterruptedException {
        return queue.take();
    }
}