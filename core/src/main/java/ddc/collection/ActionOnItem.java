package ddc.collection;

@FunctionalInterface
public interface ActionOnItem<T> {
	T functor(T o);
}