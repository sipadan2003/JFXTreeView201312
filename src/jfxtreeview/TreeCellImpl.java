package jfxtreeview;

import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *TreeCellです。
 */
final class TreeCellImpl extends TreeCell<TreeItemData> {
	private static final Image GROUP_IMAGE = createImage("folder.gif");
	private static final Image ITEM_IMAGE = createImage("file.gif");
	//TreeViewコントローラ
	private final TreeViewController controller;
	//編集用グラフィック
	private TreeCellGraph graph;

	/**
	 *コンストラクタです。
	 */
	TreeCellImpl(TreeViewController controller){
		this.controller = controller;
	}

	@Override
	public void startEdit() {
		super.startEdit();
		//編集用Graphicを生成する
		if(this.graph == null){
			this.graph = new TreeCellGraph(this.controller, this, createImageView(getItem()));
		}
		//編集開始前の処理をTreeCellGraphに委譲
		this.graph.startEdit();
		//編集時はLabeledTextにラベルを表示させない
		setText(null);
		setGraphic(this.graph);
	}

	@Override
	public void cancelEdit(){
		super.cancelEdit();
		//アイコンとラベルを元に戻す
		setText(getItem().getName());
		setGraphic(createImageView(getItem()));
	}

	@Override
	public void updateItem(TreeItemData data, boolean empty){
		super.updateItem(data, empty);
		if(empty){
			//空の場合は、ラベルもアイコンも表示させない
			setText(null);
			setGraphic(null);
		}else if(isEditing()){
			//編集時はLabeledTextにラベルを表示させない
			setText(null);
			setGraphic(this.graph);
		} else {
			//通常の表示
			setText(data.getName());
			setGraphic(createImageView(data));
		}
	}

	/**
	 *画像をロードしてImageを生成します。
	 */
	private static Image createImage(String fname){
		return new Image(TreeCellImpl.class.getResourceAsStream(fname));
	}

	/**
	 *データの種類に応じたImageViewを生成します。
	 */
	private static ImageView createImageView(TreeItemData data){
		final Image img = (data.getType() == TreeItemData.Type.GROUP) ?
			GROUP_IMAGE : ITEM_IMAGE;
		return new ImageView(img);
	}
}
