package ddc.task.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ddc.util.FilePair;

public class FileBag {
	public Path sourceFolder = null;
	public Path targetFolder = null;
	public Path fileError = null;
	public Path fileReport = null;
	
	public Map<String, Path> files = new HashMap<>();
	public List<Path> sourceFiles = new ArrayList<>();
	public String[] sourceFileIncludeExtension = new String[] {};
	public String[] sourceFileExcludeExtension = new String[] {}; 

	public List<FilePair> fileToDownload = new ArrayList<>();
	public List<FilePair> fileToUpload = new ArrayList<>();
	public List<FilePair> fileToProcess = new ArrayList<>();
	public List<FilePair> fileToMove = new ArrayList<>();
	public List<FilePair> remoteFileToRename = new ArrayList<>();
	public List<Path> remoteFileToDelete = new ArrayList<>();
	public List<FilePair> fileToRename = new ArrayList<>();
	public List<Path> fileToDeleteOnFail = new ArrayList<>();
	public List<Path> fileToDeleteOnSuccess = new ArrayList<>();
}
