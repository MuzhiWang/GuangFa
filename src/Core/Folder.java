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
	
	public void addSubFolder(Folder subFolder) throws Exception {
		if (this.subFolders.containsKey(subFolder.name)) {
			throw new Exception("sub folder exist");
		}

		this.subFolders.put(subFolder.name, subFolder);
	}

	public List<File> getFiles() {
		List<File> list = new ArrayList<File>(this.files);
		Collections.sort(list, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.fileName.compareTo(f2.fileName);
			}
		});
		return list;
	}

	public boolean existFile(String name) {
		return this.files.contains(name);
	}

	public List<Folder> getSubFolders() {
		List<Folder> list = new ArrayList<>(this.subFolders.values());
		Collections.sort(list, new Comparator<Folder>() {
			@Override
			public int compare(Folder o1, Folder o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		return list;
	}

	public boolean existSubFolder(String name) {
		return this.subFolders.containsKey(name);
	}
}
