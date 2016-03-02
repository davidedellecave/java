package ddc.task1;

import ddc.util.FormatUtils;

public class ProcessNotification {
	public enum Notification {START, END, STOP, RUNNING, EXCEPTION};
	public Notification notification;
	public Object arg;
	
	public ProcessNotification(Notification notification, Object arg) {
		this.notification=notification;
		this.arg=arg;
	}
	
	public String toString() {
		return FormatUtils.format(notification, arg);
	}
	
}
