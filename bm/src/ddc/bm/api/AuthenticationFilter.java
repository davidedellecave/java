package ddc.bm.api;

import java.io.IOException;
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

import org.apache.commons.lang3.StringUtils;

import ddc.bm.app.Auth;
import ddc.bm.app.SecurityAnnotation.SecureFeature;
import ddc.bm.model.User;
import ddc.core.crypto.TokenException;

@SecureFeature({})
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
	private static String TOKEN_NAME = "bmtokena";
	@Context
	private HttpServletRequest request;

	private String detectTokenValue(ContainerRequestContext requestContext) {
		String value = requestContext.getHeaderString(TOKEN_NAME);
		if (StringUtils.isBlank(value)) {
			if (requestContext.getCookies().containsKey(TOKEN_NAME)) {
				Cookie cookie = requestContext.getCookies().get(TOKEN_NAME);
				value = cookie.getValue();
			}
		}
		if (StringUtils.isBlank(value)) {
			value = request.getParameter(TOKEN_NAME);
		}
		return value;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String token = detectTokenValue(requestContext);
		if (token == null) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
		final String domain=request.getServerName();
		try {
			final User user = Auth.getUser(domain, token);

			requestContext.setSecurityContext(new SecurityContext() {
				@Override
				public boolean isUserInRole(String feature) {
					return user.isFeatureEnabled(feature);
				}

				@Override
				public boolean isSecure() {
					return false;
				}

				@Override
				public Principal getUserPrincipal() {
					return user;
				}

				@Override
				public String getAuthenticationScheme() {
					return "BASIC";
				}
			});
		} catch (NoSuchAlgorithmException | TokenException e) {
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}

	}

}