package ddc.task;

import org.junit.Before;
import org.junit.Test;

import ddc.task.impl.ConfigurationTask;
import ddc.task.impl.DeleteFileOnFailTask;
import ddc.task.impl.FtpDownloadTask;
import ddc.task.impl.FtpUploadTask;
import ddc.task.impl.MailTask;

public class TaskSchemaTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTaskSchema() {
		TaskSchema t = new TaskSchema(ConfigurationTask.class);
		t.nextSuccess(FtpDownloadTask.class)
		.nextSuccess(FtpUploadTask.class)
		.nextSuccess(MailTask.class);		
		
		t.getChild(ConfigurationTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
		t.getChild(FtpDownloadTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
		t.getChild(FtpUploadTask.class).nextFail(DeleteFileOnFailTask.class).next(MailTask.class, MailTask.class);
//		t.getChild(MailTask.class).nextFail(FtpRenameTask.class).next(MailTask.class, MailTask.class);
		
		System.out.println("---------");
		TaskSchema child = t.getChild(FtpDownloadTask.class);
		System.out.println("Found:" + child.toString());
		
		System.out.println("---------");
		t.print();
		
		System.out.println("---------");
		System.out.println(t.toSchemaString());
		
	}
	


}
