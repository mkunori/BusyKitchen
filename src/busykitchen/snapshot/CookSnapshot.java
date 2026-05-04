package busykitchen.snapshot;

import busykitchen.domain.CookStatus;
import busykitchen.domain.Order;

/**
 * コックの状態スナップショットです。
 *
 * GUIやモニタリング用に状態をまとめて扱います。
 *
 * @param cookName     コック名
 * @param status       コック状態
 * @param currentOrder 現在の注文（なければ null）
 */
public record CookSnapshot(
                String cookName,
                CookStatus status,
                Order currentOrder) {
}