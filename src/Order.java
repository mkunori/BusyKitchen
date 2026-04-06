/**
 * 1件の注文を表すレコードクラス
 * 
 * @param orderNo        注文番号
 * @param dishName       料理名
 * @param cookTimeMillis 調理時間（ミリ秒）
 */
public record Order(int orderNo, String dishName, int cookTimeMillis) {

    /** 終了シグナル用の注文番号 */
    public static final int END_ORDER_NO = -1;

    /**
     * この注文が終了シグナルかどうかを返す。
     * 
     * @return 終了シグナルなら true
     */
    public boolean isEndSignal() {
        return orderNo == END_ORDER_NO;
    }

    /**
     * 終了シグナル用の注文を生成する。
     * 
     * @return 終了シグナル用の注文
     */
    public static Order createEndSignal() {
        return new Order(END_ORDER_NO, "END", 0);
    }
}