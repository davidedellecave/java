package ddc.task1;

import java.util.Observable;

public class ProcessObservable extends Observable {
			
	public void notifyStart(Object arg) {
		super.setChanged();
    	super.notifyObservers(new ProcessNotification(ProcessNotification.Notification.START, arg));
    }
		
	public void notifyTerminated(Object arg) {
		super.setChanged();
		super.notifyObservers(new ProcessNotification(ProcessNotification.Notification.END, arg));
    }	
	
    public void notifyStop(Object arg) {
    	super.setChanged();
    	super.notifyObservers(new ProcessNotification(ProcessNotification.Notification.STOP, arg));
    }

    public void notifyRunning(Object arg) {
    	super.setChanged();
    	super.notifyObservers(new ProcessNotification(ProcessNotification.Notification.RUNNING, arg));
    }
    
    public void notifyException(Object arg) {
    	super.setChanged();
    	super.notifyObservers(new ProcessNotification(ProcessNotification.Notification.EXCEPTION, arg));
    }    
}
