/**
 * 
 */
package ddc.bm.api;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ddc.bm.app.AppTask;
import ddc.bm.app.Auth;
import ddc.bm.model.User;
import ddc.bm.servlet.Environment;
import ddc.bm.servlet.Tenant.TenantName;

/**
 * @author dellecave
 */
@Path("/api/login")
public class Login {
	@Context
	private ServletContext context;
	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;
	@Context
	ContainerResponseContext responseContext;
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticateUserByGet( @Context HttpHeaders headers, @QueryParam("u") final String username, @QueryParam("p") final String password) {
		AppTask<String, String> a = new AppTask<String, String>() {
			@Override
			protected String run(String domain) throws Throwable {
				return Auth.getToken(domain, username, password);
			}
		};		
		a.execute(request.getServerName());
//		if (response..getCookies()!=null && responseContext.getCookies().containsKey("token")) {
//			responseContext.getCookies().remove("token");
//		}
		response.addCookie(new Cookie("token", a.getResult()));
//		response.addHeader(HttpHeaders.AUTHORIZATION, a.getResult());
		return ApiHelper.instance().build(a);			
	}
	
	
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public Response authenticateUserByPost(@FormParam("u") String username, 
                                     @FormParam("p") String password) {
        try {
            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    }

    private String issueToken(String username) {
       return "token_value";
    }
}