package ddc.collection;

public interface PredicateOnItem<T> {
	boolean functor(T o);
}