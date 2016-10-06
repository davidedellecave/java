package ddc.bm.app;

import java.util.HashSet;
import java.util.Set;

import ddc.bm.api.Test;
 
public class Application extends javax.ws.rs.core.Application {
	
    private Set<Object> singletons = new HashSet<Object>();
//    private Set<Object> classes = new HashSet<Object>();
 
    public Application() {
        singletons.add(new Test());
    }
 
    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
 
}