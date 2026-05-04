package busykitchen.domain;

/**
 * 1件の注文を表すレコードです。
 *
 * @param orderNo  注文番号
 * @param menuItem 注文されたメニュー
 */
public record Order(int orderNo, MenuItem menuItem) {

    /**
     * 終了シグナル用の注文番号です。
     */
    public static final int END_ORDER_NO = -1;

    /**
     * この注文が終了シグナルかどうかを返します。
     *
     * @return 終了シグナルなら true
     */
    public boolean isEndSignal() {
        return orderNo == END_ORDER_NO;
    }

    /**
     * 終了シグナル用の注文を作成します。
     *
     * @return 終了シグナル用の注文
     */
    public static Order createEndSignal() {
        return new Order(END_ORDER_NO, MenuItem.END);
    }
}