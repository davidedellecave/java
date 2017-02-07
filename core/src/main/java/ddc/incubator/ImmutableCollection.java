package ddc.incubator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ImmutableCollection {

	public static void main(String[] args) {
		Set<String> set1 = Collections.unmodifiableSet(new HashSet<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("a");
				add("b");
				add("c");
			}
		});

//		Set<String> set2 = Collections.unmodifiableSet(Stream.of("a", "b", "c")).collect(toSet()));
		
		
	}

}
