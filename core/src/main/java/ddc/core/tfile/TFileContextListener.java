package ddc.core.tfile;

public interface TFileContextListener {
	public void onOpen(TFileContext context)  throws TFileException;
	public void onClose(TFileContext context)  throws TFileException;
}