package ddc.bm.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.commons.codec.EncoderException;
import org.apache.http.auth.BasicUserPrincipal;

import ddc.bm.app.Auth;
import ddc.bm.app.SecurityAnnotation.SecureFeature;
import ddc.bm.servlet.Environment;
import ddc.core.crypto.TokenException;

@SecureFeature({})
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	@Context
	private HttpServletRequest request;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (requestContext.getCookies().containsKey("token")) {
			Cookie cookie = requestContext.getCookies().get("token");
			try {
				Environment.setDomain(request.getServerName());				
				final String[] toks = Auth.instance().decodeToken(Environment.getTenantId().toString(), cookie.getValue());				
				requestContext.setSecurityContext(new SecurityContext() {
					
					@Override
					public boolean isUserInRole(String role) {
						Auth.instance().isFeatureEnabled(toks[0], toks[1], toks[2]);
						return false;
					}
					
					@Override
					public boolean isSecure() {
						try {
							Auth.instance().isUserAuthenticated(toks[0], toks[1], toks[2]);
							return true;
						} catch (NoSuchAlgorithmException | UnsupportedEncodingException | EncoderException e) {
							return false;
						}
					}
					
					@Override
					public Principal getUserPrincipal() {
						BasicUserPrincipal p = new BasicUserPrincipal(toks[1]);
						return p;
					}
					
					@Override
					public String getAuthenticationScheme() {
						return "BASIC";
					}
				});
			} catch (NoSuchAlgorithmException | TokenException e) {
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
			}
		} else {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}

}