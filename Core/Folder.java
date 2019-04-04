package Core;

import java.util.*;

public class Folder {
	private Set<File> files;
	private Map<String, Folder> subFolders;
	
	public String name;
	
	public Folder(String name) {
		this.files = new HashSet<>();
		this.subFolders = new HashMap<>();
		this.name = name;
	}
	
	public void addFile(File file) throws Exception {
		if (this.files.contains(file)) {
			throw new Exception("file exist");
		}
		
		this.files.add(file);
	}
	
	public void addSubFolder(Folder subFolder) {
		
	}
}
