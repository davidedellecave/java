package ddc.incubator;

import java.util.Map;

public interface TaskHandler {
	@SuppressWarnings("rawtypes")
	public void run(Map context, Map request,  Map response);

}
