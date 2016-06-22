package ddc.collection;

public class TestListEx {
	public static void test() {
		ListEx<Integer> list = new ListEx<>();
		list.add(3);
		list.add(12);
		list.add(5);
		
		list.forItem((Integer x) -> x);
		
	}
}
