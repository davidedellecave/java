package ddc.task;

import org.junit.Before;
import org.junit.Test;

import ddc.task.impl.ArgsValue;
import ddc.task.impl.ConfigurationTask;
import ddc.task.impl.DeleteFileOnFailTask;
import ddc.task.impl.FtpDownloadTask;
import ddc.task.impl.FtpUploadTask;
import ddc.task.impl.MailTask;

public class TaskExecutorTest {

	@Before
	public void setUp() throws Exception {
	}

	private TaskSchema getTaskSchema() {
		TaskSchema s = new TaskSchema(ConfigurationTask.class);
		s.nextSuccess(FtpDownloadTask.class)
		.nextSuccess(FtpUploadTask.class)
		.nextSuccess(MailTask.class);		
		
		s.getChild(ConfigurationTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
		s.getChild(FtpDownloadTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
		s.getChild(FtpUploadTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
//		t.getChild(MailTask.class).nextFail(FtpRenameTask.class).next(MailTask.class, MailTask.class);
		
		System.out.println("---------");
		TaskSchema child = s.getChild(FtpDownloadTask.class);
		System.out.println("Found:" + child.toString());
		
		System.out.println("---------");
		s.print();
		
		System.out.println("---------");
		System.out.println(s.toSchemaString());
		
		return s;
	}
	
	
	@Test
	public void testTaskExecutorTaskSchemaObject() {
		TaskSchema s = getTaskSchema();
		ArgsValue args = new ArgsValue(new String[] {});
		TaskExecutor e = new TaskExecutor(s, args);
		
		e.run();

		System.out.println("--------");	
		for (TaskInstance t : e.getExecutedTask()) {
			System.out.println("\t>" + t);	
		}
		
		System.out.println("--------");		
		if (e.hasFailedTask()) {
			System.out.println("Execution failed:" + e.getFailedTask());	
		}
		
	}

}
