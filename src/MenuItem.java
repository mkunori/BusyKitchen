/**
 * BusyKitchen で扱うメニューを表す列挙型です。
 *
 * 各メニューは、表示名と調理時間を持ちます。
 */
public enum MenuItem {
    RAMEN("ラーメン", 5000),
    GYOZA("餃子", 2000),
    FRIED_RICE("チャーハン", 3000),
    CURRY("カレー", 4000),
    UDON("うどん", 2500),
    END("END", 0);

    private final String displayName;
    private final int cookTimeMillis;

    /**
     * メニューを作成します。
     *
     * @param displayName    表示名
     * @param cookTimeMillis 調理時間（ミリ秒）
     */
    MenuItem(String displayName, int cookTimeMillis) {
        this.displayName = displayName;
        this.cookTimeMillis = cookTimeMillis;
    }

    /**
     * 表示名を返します。
     *
     * @return 表示名
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 調理時間を返します。
     *
     * @return 調理時間（ミリ秒）
     */
    public int getCookTimeMillis() {
        return cookTimeMillis;
    }
}