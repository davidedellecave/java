package ddc.bm.app;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ddc.bm.api.AuthenticationFilter;
import ddc.bm.api.AuthorizationFilter;
import ddc.bm.api.Login;
import ddc.bm.api.Test;
 
public class Application extends javax.ws.rs.core.Application {
	
    private Set<Object> singletons = new HashSet<Object>();
//    private Set<Object> classes = new HashSet<Object>();
 
    public Application() {
        singletons.add(new Login());
        singletons.add(new AuthenticationFilter());
        singletons.add(new AuthorizationFilter());
        singletons.add(new Test());
//        singletons.add(new SecurityInterceptor());
    }
 
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
 
    public static Logger getLogger(String name) {
    	Logger logger = Logger.getLogger(name);
    	try {
    		FileHandler handler = new FileHandler("ddc.log", true);
    		handler.setFormatter(new SimpleFormatter());
    		logger.addHandler(handler);
    	} catch (IOException e) {
    		throw new IllegalStateException("Could not add file handler.", e);
    	}
    	return logger;  	
    }
}