package monitor;

import java.util.List;

import kitchen.Cook;
import gui.BusyKitchenFrame;
import snapshot.CookSnapshot;
import snapshot.KitchenSnapshot;

/**
 * GUI用にキッチン状態を定期更新するモニターです。
 *
 * コックの状態を一定間隔で取得し、BusyKitchenFrame に反映します。
 */
public class GuiKitchenMonitor implements Runnable {

    private final List<Cook> cooks;
    private final BusyKitchenFrame frame;
    private volatile boolean running = true;

    /**
     * GUI用モニターを作成します。
     *
     * @param cooks 監視対象のコック一覧
     * @param frame 表示を更新するGUI画面
     */
    public GuiKitchenMonitor(List<Cook> cooks, BusyKitchenFrame frame) {
        this.cooks = cooks;
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            while (running) {
                frame.updateSnapshot(createSnapshot());
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        frame.updateSnapshot(createSnapshot());
    }

    /**
     * モニターを停止します。
     */
    public void stop() {
        running = false;
    }

    /**
     * 現在のキッチン状態をスナップショットとして作成します。
     *
     * @return キッチン全体の状態
     */
    private KitchenSnapshot createSnapshot() {
        List<CookSnapshot> cookSnapshots = cooks.stream()
                .map(Cook::snapshot)
                .toList();

        return new KitchenSnapshot(cookSnapshots);
    }
}