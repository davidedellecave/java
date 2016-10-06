package ddc.bm.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ddc.core.jack.JackUtil;
import ddc.task.TaskExitCode;

@Path("/api/test")
public class Test {
	@GET
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public BaseResponse echo() {
		// http://localhost:8080/mb/api/test/
		BaseResponse response = new BaseResponse();
		response.setExitCode(TaskExitCode.Succeeded);
		response.setMessage(this.getClass().getName());
		response.setResults(JackUtil.parse("{}"));
		return response;
	}
}
