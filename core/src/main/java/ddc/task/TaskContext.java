package ddc.task;

import java.util.LinkedHashMap;

public class TaskContext {
	private LinkedHashMap<Class<?>, Object> values = new LinkedHashMap<Class<?>, Object>();
	private LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>();
	private Throwable exception = null;
	
	public synchronized Object get(Class<?> clazz) throws TaskException {
		if (!values.containsKey(clazz)) throw new TaskException("Context object not found:[" + clazz.getName() + "]");
		return values.get(clazz);
	} 
	
	public synchronized Object make(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		if (values.containsKey(clazz)) {
			return values.get(clazz);	
		} else {
			return clazz.newInstance();
		}
	}

	public synchronized boolean contains(Class<?> clazz) throws TaskException {
		return values.containsKey(clazz);
	} 
	
	public synchronized void set(Object value) {
		values.put(value.getClass(), value);
	}

	public synchronized Object getOptional(Class<? extends Task> clazz) {
		return values.get(clazz);
	}

	public synchronized void setParam(String name, Object value) {
		params.put(name, value);
	}

	public synchronized Object getParam(String name) {
		if (!params.containsKey(name)) throw new TaskException("Param object not found:[" + name + "]");
		return params.get(name);
	}
	
	public synchronized Object getOptionaParam(String name) {
		return params.get(name);
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}



}