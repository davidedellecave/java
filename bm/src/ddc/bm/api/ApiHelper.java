package ddc.bm.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import ddc.bm.app.AppTask;
import ddc.task.TaskExitCode;
import ddc.util.ExceptionUtil;

public class ApiHelper<T> {
	
	public static ApiHelper<Object> instance() {
		return new ApiHelper<>();
	}
	
	public Response build(AppTask<? extends Object, ? extends Object> t) {
		ApiResponse<Object> response = new ApiResponse<>();
		response.setTask(StringUtils.isBlank(t.getClass().getSimpleName()) ? t.getClass().getName() : t.getClass().getSimpleName());
		response.setExitCode(t.getExitCode());		
		response.setElapsedMillis(t.getChron().getElapsed());
		if (t.getExitCode().equals(TaskExitCode.Succeeded)) {
			response.setResult(t.getResult());
			return Response.ok().entity(response).build();	
		} else {
			response.setResult(ExceptionUtil.getMsg(t.getException()));
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(response).build();
		}
	}

	public Response buildOk(String taskName, String message) {
		ApiResponse<String> response = new ApiResponse<>();
		response.setTask(taskName);
		response.setExitCode(TaskExitCode.Succeeded);
		response.setResult(message);		
		return Response.ok().entity(response).build();
	}

	public Response buildOk(String taskName, T result) {
		ApiResponse<T> response = new ApiResponse<>();
		response.setTask(taskName);
		response.setExitCode(TaskExitCode.Succeeded);
		response.setResult(result);		
		return Response.ok().entity(response).build();
	}
	
	public Response buildError(String taskName, Status status, String message) {
		ApiResponse<String> response = new ApiResponse<>();
		response.setTask(taskName);
		response.setExitCode(TaskExitCode.Failed);
		response.setResult(message);
		return Response.status(status).entity(response).build();
	}

	public Response buildError(String taskName, Status status, Throwable exception) {
		return buildError(taskName, status, ExceptionUtil.getMsg(exception));		
	}


}
