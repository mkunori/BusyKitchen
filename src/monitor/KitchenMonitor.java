package monitor;

import java.util.List;

import kitchen.Cook;
import domain.Order;
import log.KitchenLogger;
import snapshot.CookSnapshot;
import snapshot.KitchenSnapshot;

/**
 * キッチンの状態を定期的に表示するモニターです。
 *
 * 現在はCUIにコックの状態を表示します。
 * 将来的にはGUIのモニタリング画面へ発展させることを想定しています。
 */
public class KitchenMonitor implements Runnable {

    private final List<Cook> cooks;
    private final KitchenLogger logger;
    private volatile boolean running = true;

    /**
     * キッチンモニターを作成します。
     *
     * @param cooks  監視対象のコック一覧
     * @param logger ログ出力
     */
    public KitchenMonitor(List<Cook> cooks, KitchenLogger logger) {
        this.cooks = cooks;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            while (running) {
                printStatus();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        printStatus();
        logger.log("=== モニター終了 ===");
    }

    /**
     * モニターを停止します。
     */
    public void stop() {
        running = false;
    }

    /**
     * 現在のコック状態を表示します。
     */
    private void printStatus() {
        KitchenSnapshot kitchenSnapshot = snapshot();

        logger.log("----- Kitchen Monitor -----");

        for (CookSnapshot cookSnapshot : kitchenSnapshot.cookSnapshots()) {
            Order currentOrder = cookSnapshot.currentOrder();

            if (currentOrder == null) {
                logger.log(cookSnapshot.cookName() + " : " + cookSnapshot.status());
            } else {
                logger.log(cookSnapshot.cookName() + " : "
                        + cookSnapshot.status()
                        + " / 注文" + currentOrder.orderNo()
                        + " "
                        + currentOrder.menuItem().getDisplayName());
            }
        }
    }

    /**
     * 現在のキッチン状態をスナップショットとして取得します。
     *
     * @return キッチン全体の状態スナップショット
     */
    private KitchenSnapshot snapshot() {
        List<CookSnapshot> cookSnapshots = cooks.stream()
                .map(Cook::snapshot)
                .toList();

        return new KitchenSnapshot(cookSnapshots);
    }
}