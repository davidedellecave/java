package com.s2e.gwcr.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.s2e.gwcr.service.PackService;

import ddc.util.FileUtil;
import ddc.zip.impl.JavaUtilZipper;

public class _SimpleMessageBuilder {
//	implements PackTransformer {
//
//	@Override
//	public void build(Path source, Path dest) throws FileNotFoundException, IOException {
//		Path zipName = FileUtil.postfixFileName(dest, ".zip");
//		zip(source, zipName);
//		
//		Path encodedName = FileUtil.postfixFileName(zipName, ".p7e");
//		encode(zipName, encodedName);
//		
//		Path signedName = FileUtil.postfixFileName(encodedName, ".p7m");
//		sign(encodedName, signedName);
//		
//	}
//
//	public void zip(Path source, Path dest) throws FileNotFoundException, IOException {
//		JavaUtilZipper zipper = new JavaUtilZipper();
//		List<File> sources = new ArrayList<>();
//		sources.add(source.toFile());
//		zipper.zip(sources, dest.toFile());
//	}
//
//	
//	public void encode(Path source, Path dest) throws FileNotFoundException, IOException {
//	zip(source,dest);
//	}
//
//
//	public void sign(Path source, Path dest) throws FileNotFoundException, IOException {
//		zip(source,dest);	
//	}


}
