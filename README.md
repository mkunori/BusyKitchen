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
- Swingによるキッチン状態のモニタリング画面
- GUIログ表示
- ログ出力の分離（KitchenLogger）
- コンソールとGUIへの同時ログ出力（CompositeKitchenLogger）

## 実行イメージ

起動すると Swing のモニタリング画面が表示されます。

- 上部にコックの状態一覧を表示
- 下部に注文・調理・完了ログを表示
- コンソールにも同じログを出力

```text
=== BusyKitchen 開店 ===
Customer-1 が注文しました: 注文1 ラーメン
Cook-A が調理開始: 注文1 ラーメン
Cook-B が調理開始: 注文2 カレー
Cook-A が調理完了: 注文1 ラーメン
...
=== BusyKitchen 閉店 ===
```

## マルチスレッド要素

- 生産者（Customer）と消費者（Cook）の分離
- `BlockingQueue` による安全なデータ共有
- `ExecutorService` によるスレッド管理
- `Future.get()` による処理完了待ち
- 複数スレッドからの状態参照（volatile）
- 終了シグナルによるスレッド停止
- `SwingUtilities.invokeLater()` によるGUI更新

## パッケージ構成

```text
busykitchen
├── BusyKitchenMain
├── domain
│   ├── Order
│   ├── MenuItem
│   └── CookStatus
├── kitchen
│   ├── Cook
│   ├── Customer
│   └── OrderQueue
├── snapshot
│   ├── CookSnapshot
│   └── KitchenSnapshot
├── log
│   ├── KitchenLogger
│   ├── ConsoleKitchenLogger
│   ├── GuiKitchenLogger
│   └── CompositeKitchenLogger
├── monitor
│   ├── KitchenMonitor
│   └── GuiKitchenMonitor
└── gui
    ├── BusyKitchenFrame
    └── KitchenTableModel
```

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

### `CookStatus` (enum)
コックの状態を表します。
- WAITING
- COOKING
- STOPPED

### `Cook`
コックを表すクラスです。  
注文を取得して調理を行います。

### `Customer`
お客さんを表すクラスです。  
注文を生成してキューに登録します。

### `OrderQueue`
注文の受け渡しを管理するクラスです。  
スレッド間で安全にデータを共有します。

### `CookSnapshot` (record)
コック1人分の状態をまとめたデータです。  
コック名、状態、現在の注文を保持します。

### `KitchenSnapshot` (record)
キッチン全体の状態をまとめたデータです。  
複数のコック状態を一覧として保持します。

### `KitchenLogger`
ログ出力を担当するインターフェースです。  

### `ConsoleKitchenLogger`
CUIログ出力を担当するクラスです。 

### `GuiKitchenLogger`
GUIのログ欄へログを出力するクラスです。  
Swingの画面更新はイベントディスパッチスレッド上で行います。

### `CompositeKitchenLogger`
複数のログ出力先へ同じログを転送するクラスです。  
現在はコンソールとGUIの両方へログを出力するために使用しています。

### `KitchenMonitor`
キッチンの状態を定期的に監視・表示するクラスです。  
CUIでリアルタイムにコックの状態を確認できます。

### `GuiKitchenMonitor`
GUI用にキッチン状態を定期更新するクラスです。  
`KitchenSnapshot` を取得し、画面に反映します。

### `BusyKitchenFrame`
BusyKitchen のGUI画面です。  
コックの状態テーブルとログ表示欄を持ちます。

### `KitchenTableModel`
`JTable` にキッチン状態を表示するためのテーブルモデルです。  
`KitchenSnapshot` の内容を表形式に変換します。

## 設計ポイント

- データ（Order）と振る舞い（Cook, Customer）を分離
- `OrderQueue` により注文の受け渡し責務を明確化
- `KitchenLogger` をインターフェース化し、出力先を差し替え可能にした
- `CompositeKitchenLogger` により、複数のログ出力先へ同時出力できるようにした
- `ExecutorService` によるスレッド管理
- Snapshotにより、内部状態と表示用データを分離
- Swingの画面更新をイベントディスパッチスレッド上で行うようにした
- アプリ名をルートパッケージにして、役割ごとにディレクトリを整理

## 今後の案（仮）

- GUI画面のレイアウト改善
- 開店・注文開始・閉店ボタンの追加
- 注文数やコック人数を画面から変更できるようにする
- コックの状態を色で表示する
- 注文の優先度制御
- メニュー在庫管理
- Observer パターンの導入
- GUIとロジックをさらに分離するための KitchenService 化
