package ddc.collection;

@FunctionalInterface
public interface PredicateOnItem<T> {
	boolean functor(T o);
}