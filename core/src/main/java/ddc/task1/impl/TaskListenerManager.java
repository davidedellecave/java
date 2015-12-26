package ddc.task1.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ddc.task1.TaskListener;

public class TaskListenerManager {
	private Logger logger = Logger.getLogger(TaskListenerManager.class);
	private List<TaskListener> listeners = null;
	
	
	public void remove(TaskListener listener) {
		if (listeners==null) {
			logger.warn("remove() cannot remove listener if it is null");
			return;
		}
		logger.debug("remove() listener, listener:[" + listener.getClass().getName() + "]");
		if (listeners.contains(listener)) listeners.remove(listener);
		else logger.warn("remove() listener not found, listener:[" + listener.getClass().getName() + "]");
	}
		
	public void add(TaskListener listener) {
		if (listener==null) {
			logger.warn("add() cannot add listener if it is null");
			return;
		}
		if (listeners==null) listeners=new ArrayList<TaskListener>();
		
		logger.debug("add() listener, listener:[" + listener.getClass().getName() + "]");
		if (listeners.contains(listener)) 
			logger.warn("add() listener already contains, listener:[" + listener.getClass().getName() + "]");
		else listeners.add(listener);
	}
	
	public void add(List<TaskListener> listenerList) {
		if (listenerList==null) {
			logger.warn("add() cannot add listenerList if it is null");
			return;
		}
		if (listeners==null) listeners=new ArrayList<TaskListener>();
		logger.debug("add() listener, listener list:[" + listenerList.getClass().getName() + "] size:[" + listenerList.size() + "]");
		listeners.addAll(listenerList);
	}
	
	public List<TaskListener> getListeners() {
		return listeners;		
	}
	
	public boolean hasValues() {
		if (listeners==null) return false;
		return (listeners.size()>0);
	}
	
}
