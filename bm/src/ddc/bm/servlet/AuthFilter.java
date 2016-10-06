package ddc.bm.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AuthFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {

//		if (errorPage == null) {
//			returnError(request, response, "AuthorizationFilter not properly configured! Contact Administrator.");
//		}
//
//		HttpSession session = ((HttpServletRequest) request).getSession(false);
//		User currentUser = (User) session.getAttribute("user");
//
//		if (currentUser == null) {
//			returnError(request, response, "User does not exist in session!");
//		} else {
//			// Get relevant URI.
//			String URI = ((HttpServletRequest) request).getRequestURI();
//
//			// //Obtain AuthorizationManager singleton from Spring
//			// //ApplicationContext.
//			// ApplicationContext ctx =
//			// WebApplicationContextUtils.getWebApplicationContext(
//			// session.getServletContext());
//			// AuthorizationManager authMgr =
//			// (AuthorizationManager)ctx.getBean("AuthorizationManager");
//
//			// Invoke AuthorizationManager method to see if user can
//			// access resource.
//			boolean authorized = authMgr.isUserAuthorized(currentUser, URI);
//			if (authorized) {
//				chain.doFilter(request, response);
//			} else {
//				returnError(request, response, "User is not authorized to access this area!");
//			}
//
//		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
