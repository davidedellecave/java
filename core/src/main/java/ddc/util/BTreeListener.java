package ddc.util;

public interface BTreeListener<T> {
	public boolean visit(BTree<T> node);
}
