package ddc.bm.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ddc.bm.app.AppTask;
import ddc.bm.app.Auth;
import ddc.bm.app.TestTask;
import ddc.bm.servlet.Environment;
import ddc.task.TaskExitCode;

@Path("/api/test")
public class Test {
	@Context
	private ServletContext context;
	@Context
	private HttpServletRequest request;

	@GET
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response echo() {
		return ApiHelper.instance().buildOk("ok", this.getClass().getName());
	}

	@GET
	@Path("/t1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t1() {
		ApiResponse<List<String>> response = new ApiResponse<>();
		response.setTask(this.getClass().getSimpleName());
		response.setExitCode(TaskExitCode.Succeeded);
		ArrayList<String> a = new ArrayList<String>();
		a.add("t1");
		a.add("t1");
		response.setResult(a);
		return Response.ok().entity(response).build();
	}

	@GET
	@Path("/t2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t2() {
		ArrayList<String> a = new ArrayList<String>();
		a.add("t2");
		a.add("t2");
		return ApiHelper.instance().buildOk("t2", a);
	}

	@GET
	@Path("/t3")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t3() {
		Exception e = new Exception("Test exception");
		return ApiHelper.instance().buildError("t3", Status.INTERNAL_SERVER_ERROR, e);
	}

	@GET
	@Path("/t4")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t4() {
		TestTask t = new TestTask();
		t.execute("message");
		return ApiHelper.instance().build(t);
	}

	@GET
	@Path("/t5")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t5() {
		AppTask<String, String> a = new AppTask<String, String>() {
			@Override
			protected String run(String domain) throws Throwable {
				Environment.setDomain(domain);
				return Environment.getTenantId().toString();
			}
		};
		a.execute(request.getServerName());
		return ApiHelper.instance().build(a);
	}

	//dev davide davidedc
	//cTNneTd3dEp6NUZ2dzg3MlhjUURQQmtrYkQ0aTl0MmFpdUJQWnZDcGNCdDNnNmYwZnZUaEVlTE5HYVMxOThScA==
	
	@GET
	@Path("/t6")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t6(@QueryParam("u") final String username, @QueryParam("p") final String password) {
		AppTask<String, Boolean> a = new AppTask<String, Boolean>() {
			@Override
			protected Boolean run(String domain) throws Throwable {
				Environment.setDomain(domain);
				String tenant = Environment.getTenantId().toString();
				return Auth.instance().isUserAuthenticated(tenant, username, password);
			}
		};
		a.execute(request.getServerName());
		return ApiHelper.instance().build(a);
	}
	
	@GET
	@Path("/t7")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response t7(@QueryParam("t") final String token) {
		AppTask<String, Boolean> a = new AppTask<String, Boolean>() {
			@Override
			protected Boolean run(String domain) throws Throwable {
				Environment.setDomain(domain);
				String tenant = Environment.getTenantId().toString();
				return Auth.instance().isUserAuthenticated(tenant, token);
			}
		};
		a.execute(request.getServerName());
		return ApiHelper.instance().build(a);
	}

}
