package ddc.collection;

import java.util.Collections;
import java.util.LinkedList;


public class ListEx<E extends Comparable<E>> extends LinkedList<E> {

	private static final long serialVersionUID = 2139778219300967736L;
	
	public ListEx<E> addEx(E item) {
		this.add(item);
		return this;
	}
	
	public void forEach(ActionOnItem<E> action) {
		for (E e : this) {
			e = action.functor(e);
		}
	}

	public void sort() {
		Collections.sort(this);
	}
	
	public ListEx<E> findAll(Comparable<E> item) {
		ListEx<E> list = new ListEx<E>();
		for (E x : this) {
			if (item.equals(x)) {
				list.add(x);
			}
		}
		return list;
	}

	public E findFirst(Comparable<E> item) { 
		for (E x : this) {
			if (item.equals(x)) return x;
		}
		return null;
	}

	public boolean contains(Comparable<E> item) { 
		for (E x : this) {
			if (item.equals(x)) return true;
		}
		return false;
	}
	

}
