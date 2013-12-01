package jfxtreeview;

/**
 *{@link javafx.scene.control.TreeItem}用のデータです。
 */
public class TreeItemData {
	private final String name;	//名前
	private final Type type;	//種類(グループ/アイテム)

	/**
	 *コンストラクタです。
	 *@param name 名前
	 */
	public TreeItemData(String name) {
		this(name, Type.ITEM);
	}

	/**
	 *コンストラクタです。
	 *@param name 名前
	 *@param type 種類
	 */
	public TreeItemData(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	/**
	 *名前を取得します。
	 *@return 名前
	 */
	public String getName(){
		return this.name;
	}

	/**
	 *種類を取得します。
	 *@return 種類
	 */
	public Type getType(){
		return this.type;
	}

	/**
	 *文字列表現を返します。
	 *{@link #getName()}と同じ文字列を返します。
	 *@see #getName()
	 */
	@Override
	public String toString(){
		return this.getName();
	}

	/**
	 *TreeItemの種類です。
	 */
	public enum Type {
		/**グループです。*/
		GROUP,
		/**アイテムです。*/
		ITEM,
	}
}
