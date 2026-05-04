package snapshot;

import java.util.List;

/**
 * キッチン全体の状態スナップショットです。
 *
 * GUIやモニタリング用に、複数のコック状態をまとめて扱います。
 *
 * @param cookSnapshots コック状態の一覧
 */
public record KitchenSnapshot(List<CookSnapshot> cookSnapshots) {
}