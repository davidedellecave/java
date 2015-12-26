package ddc.util;

public class BTree<T> {
	private T value;
	private BTree<T> parent = null;
	private BTree<T> left = null;
	private BTree<T> right = null;
	
	public BTree(T value) {
		this.value=value;
	}
	
	public BTree<T> search(T valueToSearch) {
		Search callback = new Search(valueToSearch);
		preorder(this, callback); 
		return callback.found;
	}
	
	class Search implements BTreeListener<T> {
		public BTree<T> found = null;
		private T valueToSearch;
		public Search(T valueToSearch) {
			this.valueToSearch=valueToSearch;
		}
		@Override
		public boolean visit(BTree<T> node) {
			if (valueToSearch.equals(node.getValue())) {
				found = node;
				return false;
			}
			return true;
		}
	}
	
	public void visit(BTreeListener<T> callback) {
		preorder(this, callback);
	}
	
	public void preorder(BTree<T> node, BTreeListener<T> callback) {
		if (!callback.visit(node)) return;
		if (node.getLeft()!=null) preorder(node.left, callback);
		if (node.getRight()!=null) preorder(node.right, callback);
	}
	
//	public void inorder(BTree<T> node, BTreeVisit<T> callback) {
//		if (node.getLeft()!=null) preorder(node.left, callback);
//		if (!callback.visit(node)) return;
//		if (node.getRight()!=null) preorder(node.right, callback);
//	}
//	
//	public void postorder(BTree<T> node, BTreeVisit<T> callback) {
//		if (node.getLeft()!=null) preorder(node.left, callback);
//		if (!callback.visit(node)) return;
//		if (node.getRight()!=null) preorder(node.right, callback);
//	}
	
	//
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public BTree<T> getParent() {
		return parent;
	}
	public void setParent(BTree<T> parent) {
		this.parent = parent;
	}
	public BTree<T> getLeft() {
		return left;
	}

	public void setRight(T value) {
		BTree<T> node = new BTree<T>(value);
		setRight(node);
	}
	
	public void setLeft(T value) {
		BTree<T> node = new BTree<T>(value);
		setLeft(node);
	}

	public void setRight(BTree<T> right) {
		right.setParent(this);
		this.right = right;
	}

	public void setLeft(BTree<T> left) {
		left.setParent(this);
		this.left = left;
	}
	
	public BTree<T> getRight() {
		return right;
	}

	
	
}
