package domain;

/**
 * コックの現在の状態を表す列挙型です。
 */
public enum CookStatus {
    /**
     * 注文を待っている状態です。
     */
    WAITING,

    /**
     * 調理中の状態です。
     */
    COOKING,

    /**
     * 営業終了した状態です。
     */
    STOPPED
}