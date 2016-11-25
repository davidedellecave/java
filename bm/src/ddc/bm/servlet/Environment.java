package ddc.bm.servlet;

import java.util.Map;
import java.util.TreeMap;

public class Environment {



	
//	public static void setDomain(String domain) {
//	threadLocal.get().setDomain(domain);
//}


	
//	private static final ThreadLocal<info> threadLocal = new ThreadLocal<info>() {
//		@Override
//		protected info initialValue() {
//			return (new Environment()).new info();
//		}
//	};

//	public class info {
//		private String domain;
//
//		public String getDomain() {
//			return domain;
//		}
//
//		public void setDomain(String domain) {
//			this.domain = domain;
//		}
//
//	}

//	public static void setDomain(String domain) {
//		threadLocal.get().setDomain(domain);
//	}

//	public static EnvType getTenantId() {
//		String domain = threadLocal.get().getDomain();
//		if ("localhost".equals(domain)) {
//			return EnvType.dev;
//		}
//		if ("app.delle-cave.it".equals(domain)) {
//			return EnvType.test;
//		}
//		if ("admin.medisportgottardo.it".equals(domain)) {
//			return EnvType.gottardo;
//		}
//		if ("admin.spartacoms.it".equals(domain)) {
//			return EnvType.spartaco;
//		}
//		return EnvType.unknown;
//	}

	//
	// This class provides thread-local variables.
	// These variables differ from their normal counterparts in that
	// each thread that accesses one (via its get or set method) has its own,
	// independently initialized copy of the variable.
	// ThreadLocal instances are typically private static fields in classes
	// that wish to associate state with a thread (e.g., a user ID or
	// Transaction ID).
	//
	// For example, the class below generates unique identifiers local to each
	// thread. A thread's id is assigned the first time it invokes
	// ThreadId.get() and remains unchanged on subsequent calls.
	//
	// import java.util.concurrent.atomic.AtomicInteger;
	//
	// public class ThreadId {
	// // Atomic integer containing the next thread ID to be assigned
	//
	// // Thread local variable containing each thread's ID
	//
	// }
	//
	//
	// Each thread holds an implicit reference to its copy of a thread-local
	// variable as long as the thread is alive and the ThreadLocal instance is
	// accessible; after a thread goes away, all of its copies of thread-local
	// instances are subject to garbage collection (unless other references to
	// these copies exist).

}
