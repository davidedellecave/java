package ddc.core.tfile.filter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Base64;

import ddc.core.tfile.TFileContext;
import ddc.core.tfile.TFileException;
import ddc.core.tfile.TFileLineError;
import ddc.util.DateUtil;

public class FileCsvOutErrorFilter extends BaseTFileFilter {
	private File outFile = null;
	private OutputStream out = null;
	private char separator=',';
	private boolean enableSource=false;

	public FileCsvOutErrorFilter(Path path, char separator, boolean enableSource) {
		this.outFile = path.toFile();
		this.separator = separator;
		this.enableSource = enableSource;
	}

	@Override
	public void onOpen(TFileContext context) throws TFileException {
		super.onOpen(context);
		try {
			out = new BufferedOutputStream(new FileOutputStream(outFile));
		} catch (FileNotFoundException e) {
			throw new TFileException(e);
		}
	}

	@Override
	public void onClose(TFileContext context) throws TFileException {
		super.onClose(context);
		if (out != null)
			try {
				out.close();
			} catch (IOException e) {
				throw new TFileException(e);
			}
	}

	@Override
	public void onError(TFileLineError error) throws TFileException {
		if (error == null)
			return;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(separator).append(DateUtil.formatNowToISO());
			sb.append(separator).append(getContext().getSource().getFileName());
			sb.append(separator).append(error.getLine());
			sb.append(separator).append(error.getException().getMessage());
			if (enableSource) {
				String base64 = new String(Base64.getEncoder().encode(error.getSource().getBytes()));
				sb.append(separator).append(base64);	
			}
			sb.append(getContext().getSourceEOL());
			out.write(sb.toString().getBytes());
		} catch (IOException e) {			
			throw new TFileException(e);
		}

		// if (errors.size()==0) return;
		// try {
		// for (TFileLineError err : errors) {
		// StringBuilder sb = StringUtil.removeChar(new
		// StringBuilder(err.getLine()), BaseTFileFilter.CARRIAGE_RETURN_S +
		// BaseTFileFilter.LINE_FEED_S);
		// sb.append(separator).append(DateUtil.formatNowToISO());
		//// sb.append(separator).append(getContext().getSource().getName());
		// sb.append(separator).append(err.getLineNumber());
		// sb.append(separator).append(err.getException().getMessage());
		// sb.append(getContext().getSourceEOL());
		// out.write(sb.toString().getBytes());
		// }
		// } catch (IOException e) {
		// throw new TFileException(e);
		// }
	}
}
