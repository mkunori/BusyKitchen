package domain;

import java.util.concurrent.ThreadLocalRandom;

/**
 * BusyKitchen で扱うメニューを表す列挙型です。
 */
public enum MenuItem {
    RAMEN("ラーメン", 5000),
    GYOZA("餃子", 2000),
    FRIED_RICE("チャーハン", 3000),
    CURRY("カレー", 4000),
    UDON("うどん", 2500),
    END("END", 0);

    private static final MenuItem[] NORMAL_ITEMS = {
            RAMEN,
            GYOZA,
            FRIED_RICE,
            CURRY,
            UDON
    };

    private final String displayName;
    private final int cookTimeMillis;

    MenuItem(String displayName, int cookTimeMillis) {
        this.displayName = displayName;
        this.cookTimeMillis = cookTimeMillis;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getCookTimeMillis() {
        return cookTimeMillis;
    }

    /**
     * ランダムな通常メニューを返します。
     *
     * 終了シグナル用の END は選ばれないようにします。
     *
     * @return ランダムな通常メニュー
     */
    public static MenuItem randomItem() {
        return NORMAL_ITEMS[ThreadLocalRandom.current().nextInt(NORMAL_ITEMS.length)];
    }
}