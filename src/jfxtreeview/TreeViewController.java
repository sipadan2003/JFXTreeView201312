package jfxtreeview;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 *JavaFXの{@link javafx.scene.control.TreeView}を制御するコントローラです。
 */
public class TreeViewController {
	/**
	 *コンストラクタです。
	 *@param treeview {@link javafx.scene.control.TreeView}オブジェクト
	 *@param prop {@link TreeViewConfiguration}オブジェクト
	 */
	public TreeViewController(TreeView<TreeItemData> treeview) {
		//TreeViewのモデル(TreeItem)を構築する
		final TreeItem<TreeItemData> rootNode = new TreeItem<>(new TreeItemData("Root", TreeItemData.Type.GROUP));
		final TreeItem<TreeItemData> node0 = new TreeItem<>(new TreeItemData("Node0", TreeItemData.Type.GROUP));
		final TreeItem<TreeItemData> node1 = new TreeItem<>(new TreeItemData("Node1", TreeItemData.Type.GROUP));
		rootNode.setExpanded(true);
		rootNode.getChildren().add(node0);
		rootNode.getChildren().add(node1);
		for(int i=0; i<3; i++){
			node0.getChildren().add(new TreeItem<>(new TreeItemData("Node0"+i)));
			node1.getChildren().add(new TreeItem<>(new TreeItemData("Node1"+i)));
		}
		//TreeItemのルートをTreeViewに設定する
		treeview.setRoot(rootNode);
		//独自TreeCellを生成するクラスを設定する
		treeview.setCellFactory(new TreeViewCellFactory());
	}

	/**
	 *TreeItemのデータ名が変更されたときに呼び出されます。
	 *@param treeItem データ名が変更されたTreeItem
	 *@param name 新しいデータ名
	 *@return 新しいデータ名を反映したTreeItem (データ名の変更をキャンセルする場合はnullを返す)
	 */
	protected TreeItemData treeItemDataRenamed(TreeItem<TreeItemData> treeItem, String name){
		return new TreeItemData(name, treeItem.getValue().getType());
	}

	/**
	 *独自のTreeCellを生成するファクトリクラスです。
	 */
	final class TreeViewCellFactory implements Callback<TreeView<TreeItemData>,TreeCell<TreeItemData>> {
		@Override
		public TreeCell<TreeItemData> call(TreeView<TreeItemData> treeview){
			return new TreeCellImpl(TreeViewController.this);
		}
	}
}
