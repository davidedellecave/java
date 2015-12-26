package ddc.files.scan;

import java.io.File;
import java.util.List;

public class ContextScan {
		private ScanFolderConfig config = null;
		private StatsScan stats = null;
		private File folder = null;
		private List<File> siblingFiles = null;
		private List<File> siblingFolder = null;
		public ScanFolderConfig getConfig() {
			return config;
		}
		public void setConfig(ScanFolderConfig config) {
			this.config = config;
		}
		public StatsScan getStats() {
			return stats;
		}
		public void setStats(StatsScan stats) {
			this.stats = stats;
		}
		public File getFolder() {
			return folder;
		}
		public void setFolder(File folder) {
			this.folder = folder;
		}
		public List<File> getSiblingFiles() {
			return siblingFiles;
		}
		public void setSiblingFiles(List<File> siblingFiles) {
			this.siblingFiles = siblingFiles;
		}
		public List<File> getSiblingFolder() {
			return siblingFolder;
		}
		public void setSiblingFolder(List<File> siblingFolder) {
			this.siblingFolder = siblingFolder;
		}
		
		
}
