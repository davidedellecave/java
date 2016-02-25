package ddc.task;

public abstract class Task implements Runnable {
	private TaskContext context = null;	

	/**
	 * Shortcut for getContext().get(Class<?> clazz);
	 * @param clazz
	 * @return
	 */
	public Object get(Class<?> clazz) {
		return context.get(clazz);
	}

	public Object get(String name) {
		return context.getParam(name);
	}
	
	public void set(String name, Object value) {
		context.setParam(name, value);
	}
	
	/**
	 * Shortcut for getContext().set(Object value);
	 * @param clazz
	 * @return
	 */
	public void set(Object value) {
		context.set(value);
	}
	
	public TaskContext getContext() {
		return context;
	}

	public void setContext(TaskContext context) {
		this.context = context;
	}
	
}
