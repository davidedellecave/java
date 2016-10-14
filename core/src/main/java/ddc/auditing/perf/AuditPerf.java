package ddc.auditing.perf;

import java.io.IOException;
import java.io.OutputStream;

import ddc.util.Chronometer;
public class AuditPerf {
	public static String AUDIT_TAG_START="<audit metric='PerfTime'>";
	public static String AUDIT_TAG_END="</audit>";
	public static char SEP_FIELD=';';
	public static char SEP_VALUE='=';
	public static char REPLACING_CHAR='_';
	private Chronometer chron=new Chronometer();	
	private Class<? extends Object> clazz=null;
	private String method=null;
	private OutputStream outStream=null;
	
	public AuditPerf(Object obj, String method, OutputStream outStream) {
		this.method = method;
		this.clazz = obj==null ? this.getClass() : obj.getClass();
		this.outStream=outStream;
		start();
	}
	
	public Class<? extends Object> getClazz() {
		return clazz;
	}

	public String getMethod() {
		return method;
	}
	
	public AuditPerf start() {
		chron.start();
		return this;
	}
	
	public AuditPerf stop() {
		chron.stop();
		return this;
	}

	/**
	 * Return the current elapsed time (chron is not stopped)
	 */
	public long getElapsedMillis() {		
		return chron.getElapsed();
	}
	
	/**
	 * stop chron and log success audit
	 * @return
	 * @throws IOException 
	 */
	public AuditPerf logSuccess() throws IOException {
		stop();
		doLog(chron.getElapsed(), true);
		return this;
	}

	/**
	 * stop chron and log fail audit
	 * @return
	 * @throws IOException 
	 */
	public AuditPerf logFail() throws IOException {
		stop();
		doLog(chron.getElapsed(), false);
		return this;
	}

	private void doLog(long elapsedMillis, boolean success) throws IOException {		

		StringBuilder b = new StringBuilder();
		b.append(AUDIT_TAG_START)
		.append("timestamp")		
		.append(SEP_VALUE)
		.append(System.currentTimeMillis())
		
		.append(SEP_FIELD)		
		.append("class")		
		.append(SEP_VALUE)
		.append(clazz.getCanonicalName()).append(".").append(method)
		
		.append(SEP_FIELD)
		.append("elapsedMillis")
		.append(SEP_VALUE)
		.append(elapsedMillis)
		
		.append(SEP_FIELD)
		.append("result")
		.append(SEP_VALUE)
		.append(success ? "success" : "fail")		
		
		.append(AUDIT_TAG_END);
		outStream.write(b.toString().getBytes());
	}
	
	@Override
	/**
	 * Return the class.method and the current elapsed time (chron is not stopped)
	 */
	public String toString() {
		return clazz.getCanonicalName() + "." + method + " elapsed:[" + getElapsedMillis() + "]";
	}
}
