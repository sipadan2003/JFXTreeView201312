package jfxtreeview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class Main extends Application {
	private TreeViewController controller;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) {
		try {
			//ウィンドウのアイコンを設定
			final Image img16 = new Image(getClass().getResourceAsStream("treeviewsample16.jpg"));
			final Image img32 = new Image(getClass().getResourceAsStream("treeviewsample32.jpg"));
			//GUI構築
			final Pane root = (Pane)FXMLLoader.load(this.getClass().getResource("fxml001.fxml"));
			this.controller =new TreeViewController((TreeView<TreeItemData>)root.lookup("#treeview"));
			stage.setScene(new Scene(root));
			stage.setTitle("TreeView Sample");
			stage.getIcons().addAll(img16, img32);
			stage.show();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
