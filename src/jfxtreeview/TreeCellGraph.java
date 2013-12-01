package jfxtreeview;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

/**
 *TreeCellが編集モードのときに使用するGraphです。
 */
final class TreeCellGraph extends GridPane {
	private final TreeViewController controller;
	private final TreeCellImpl cell;
	private final TextField textField = new TextField();
	private TextFocusHandler focusHandler;

	/**
	 *コンストラクタ。
	 *@param controller TreeViewコントローラ
	 *@param cell TreeCellオブジェクト
	 *@param imageView 編集時のアイコン用ImageView
	 */
	TreeCellGraph(TreeViewController controller, TreeCellImpl cell, ImageView imageView){
		this.controller = controller;
		this.cell = cell;
		this.textField.setOnKeyPressed(new TextFieldKeyPressedHandler());
		add(imageView, 0, 0);
		add(this.textField, 1, 0);
	}

	/**
	 *TreeCellが編集モードになるときに、TreeCellから呼び出される。 
	 */
	void startEdit(){
		final TextField textField = this.textField;
		//フォーカスハンドラの登録
		this.focusHandler = new TextFocusHandler();
		textField.focusedProperty().addListener(this.focusHandler);
		//TextFieldに文字列を設定
		textField.setText(this.cell.getItem().getName());
		//遅延してselectAll->requestFocusの順で呼び出す
		Platform.runLater(new Runnable(){
			@Override
			public void run(){
				textField.selectAll();
				textField.requestFocus();
			}
		});
	}

	/**
	 *[Enter]または[ESC]押下時に呼び出される。 
	 */
	private void endingEditByKeyboard(){
		//名前が空なら、編集を継続
		final TextField textField = this.textField;
		final String currentText = textField.getText();
		if(currentText.isEmpty()){
			return;
		}
		//名前に変更が無ければ、編集をキャンセル
		final TreeCellImpl cell = this.cell;
		final String prevText = cell.getItem().getName();
		if(currentText.equals(prevText) == true){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		//兄弟に同じ名前が無いかどうかチェックする
		final TreeItem<TreeItemData> treeItem = cell.getTreeItem();
		final Iterator<TreeItem<TreeItemData>> children = treeItem.getParent().getChildren().iterator();
		while(children.hasNext()){
			final TreeItem<TreeItemData> child = children.next();
			//同じ名前が存在する場合、編集を継続
			if(treeItem != child && currentText.equals(child.getValue().getName())){
				return;
			}
		}
		//Controllerのコールバックを呼び出す
		final TreeItemData newTreeItemData = this.controller.treeItemDataRenamed(treeItem, currentText);
		//コールバックの復帰値がnullの場合、新しい名前が受け入れなかったと判断し、編集をキャンセルする
		if(newTreeItemData == null){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		//編集を確定する
		removeFocusHandler();
		cell.commitEdit(newTreeItemData);
	}

	/**
	 *フォーカスを失ったときに呼び出される。 
	 */
	private void endingEditByLostingFocus(){
		//名前が空なら、編集をキャンセル
		final TextField textField = this.textField;
		final String currentText = textField.getText();
		if(currentText.isEmpty()){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		//名前に変更が無ければ、編集をキャンセル
		final TreeCellImpl cell = this.cell;
		final String prevText = cell.getItem().getName();
		if(currentText.equals(prevText) == true){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		//兄弟に同じ名前が無いかどうかチェックする
		final TreeItem<TreeItemData> treeItem = cell.getTreeItem();
		final Iterator<TreeItem<TreeItemData>> children = treeItem.getParent().getChildren().iterator();
		while(children.hasNext()){
			final TreeItem<TreeItemData> child = children.next();
			//同じ名前が存在する場合、編集をキャンセル（暫定処理）
			if(treeItem != child && currentText.equals(child.getValue().getName())){
				removeFocusHandler();
				cell.cancelEdit();
				return;
			}
		}
		//Controllerのコールバックを呼び出す
		final TreeItemData newTreeItemData = this.controller.treeItemDataRenamed(treeItem, currentText);
		//コールバックの復帰値がnullの場合、新しい名前が受け入れなかったと判断し、編集をキャンセルする
		if(newTreeItemData == null){
			removeFocusHandler();
			cell.cancelEdit();
			return;
		}
		//編集を確定する
		removeFocusHandler();
		cell.commitEdit(newTreeItemData);
	}

	/**
	 *フォーカスハンドラを削除する。
	 *ENTER/ESC押下時にフォーカスを失っても、endingEdit()を呼び出さないようにするため。
	 */
	private void removeFocusHandler(){
		this.textField.focusedProperty().removeListener(this.focusHandler);
		this.focusHandler = null;
	}

	/**
	 *編集中にENTER/ESCが押下されたら、編集終了。
	 */
	final class TextFieldKeyPressedHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			switch(e.getCode()){
			case ENTER:
				TreeCellGraph.this.endingEditByKeyboard();
				break;
			case ESCAPE:
				TreeCellGraph.this.cell.cancelEdit();
				break;
			default:
				//NOP
			}
		}
	}

	/**
	 *編集中にフォーカスを失ったら、編集終了。
	 */
	final class TextFocusHandler implements ChangeListener<Boolean>{
		@Override
		public void changed(ObservableValue<? extends Boolean> o, Boolean b1, Boolean b2) {
			if(b2==false){
				TreeCellGraph.this.endingEditByLostingFocus();
			}
		}
	}
}
