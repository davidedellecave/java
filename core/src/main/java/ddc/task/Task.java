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
	
	public Object getOptional(String name) {
		return context.getOptionaParam(name);
	}
	
	public Object getOptional(Class<? extends Task> clazz) {
		return context.getOptional(clazz);
	}
	
	public void set(String name, Object value) {
		context.setParam(name, value);
	}
		
	public void set(Object value) {
		context.set(value);
	}
	
	public Object make(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		return context.make(clazz);
	}

	public void setContext(TaskContext context) {
		this.context = context;
	}
	
	public Throwable getException() {
		return context.getException();
	}

	public void setException(Throwable exception) {
		this.context.setException(exception);
	}
	
}
