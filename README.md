# BusyKitchen

BusyKitchen は、料理の注文と調理を題材にしたアプリケーションです。  
Javaのマルチスレッド学習として作成しました。 

注文はキューに追加され、複数のコックが並行して料理を作ります。  
料理ごとに調理時間が異なるため、注文順と完成順が一致しないことがあります。  

## アプリケーション概要

- お客さん（Customer）が注文を行う
- 注文は `OrderQueue` に登録される
- コック（Cook）が別スレッドで注文を取得して調理する
- 複数のコックが並行して処理するため、同時進行が発生する

## 主な機能

- 複数スレッドによる同時調理
- 注文キューによるスレッド間通信
- ランダムな注文生成
- コックの状態管理（WAITING / COOKING / STOPPED）
- ログ出力の分離（KitchenLogger）
- キッチン状態のリアルタイムモニタリング（KitchenMonitor）

## マルチスレッド要素
- 生産者（Customer）と消費者（Cook）の分離
- BlockingQueue による安全なデータ共有
- Future.get() による処理完了待ち
- 複数スレッドからの状態参照（volatile）
- 終了シグナルによるスレッド停止

## クラス構成

### `BusyKitchenMain`
アプリケーションの起動クラスです。  
スレッドの起動・終了制御を担当します。

### `Order` (record)
注文データを表します。  
注文番号とメニューを保持します。

### `MenuItem` (enum)
料理メニューを表します。  
料理名と調理時間を管理します。

### `OrderQueue`
注文の受け渡しを管理するクラスです。  
スレッド間で安全にデータを共有します。

### `Cook`
コックを表すクラスです。  
注文を取得して調理を行います。

### `Customer`
お客さんを表すクラスです。  
注文を生成してキューに登録します。

### `CookStatus` (enum)
コックの状態を表します。
- WAITING
- COOKING
- STOPPED

### `KitchenLogger`
ログ出力を担当するインターフェースです。  

### `ConsoleKitchenLogger`
CUIログ出力を担当するクラスです。 

### `CookSnapshot` (record)
コック1人分の状態をまとめたデータです。  
コック名、状態、現在の注文を保持します。

### `KitchenSnapshot` (record)
キッチン全体の状態をまとめたデータです。  
複数のコック状態を一覧として保持します。

### `KitchenMonitor`
キッチンの状態を定期的に監視・表示するクラスです。  
CUIでリアルタイムにコックの状態を確認できます。


## 実行例

```text
=== BusyKitchen 開店 ===
Customer-1 が注文しました: 注文1 ラーメン
Customer-2 が注文しました: 注文2 餃子
Cook-A が調理開始: 注文1 ラーメン
Cook-B が調理開始: 注文2 餃子
Cook-B が調理完了: 注文2 餃子
...
=== BusyKitchen 閉店 ===
```

## 設計ポイント
- データ（Order）と振る舞い（Cook, Customer）を分離
- `OrderQueue` によりドメインオブジェクトとして責務を明確化
- ログ処理を `KitchenLogger` に集約し、UI変更に対応しやすい設計
- `ExecutorService` によるスレッド管理
- Snapshotパターンにより状態取得と表示処理を分離

## 今後の案（仮）
- GUIによるレストランモニタリング画面の実装
- コックの状態表示（リアルタイム更新）
- 注文の優先度制御
- メニュー在庫管理
- Observer パターンの導入
- ExecutorService の設定最適化
- GUIと連携したSnapshot表示
