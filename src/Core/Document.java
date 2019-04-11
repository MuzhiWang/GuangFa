package Core;

import Tools.Utils;

import java.io.File;
import java.util.*;

public class Document {
	public Map<String, Document> folders;
	public Map<String, Map<String, Document>> files;
	
	public String name;
	public String path;
	public boolean isFolder;
	public File file;
	
	public Document(String name, String path, boolean isFolder) {
		this.name = name;
		this.path = path;
		this.isFolder = isFolder;
		this.file = new File(path);
		if (isFolder) {
			this.folders = new HashMap<>();
			this.files = new HashMap<>();
		}
	}
	
	public boolean addFolder(Document document) {
		if (this.folders.containsKey(document.name)) {
			return false;
		}
		
		this.folders.put(document.name, document);
		return true;
	}

	public boolean addFile(Document file) throws Exception {
		// The file name should be "4项目清单"
		String filePath = Utils.getLastFilePathInName(file.name);
		if (!this.files.containsKey(filePath)) {
			this.files.put(filePath, new HashMap<>());
		}

		// If same file name exist.
		if (this.files.get(filePath).containsKey(file.name)) {
			return false;
		} else {
			this.files.get(filePath).put(file.name, file);
			return true;
		}
	}

	public Document getFolder(String folderName) {
		if (!this.folders.containsKey(folderName)) {
			return null;
		}

		return this.folders.get(folderName);
	}

	public Document getFile(String fileName) {
		String filePath = Utils.getLastFilePathInName(fileName);
		if (!this.files.containsKey(filePath) || !this.files.get(filePath).containsKey(fileName)) {
			return null;
		}

		return this.files.get(filePath).get(fileName);
	}

	public void removeFolder(String folderName) throws Exception {
		if (!this.folders.containsKey(folderName)) {
			throw new Exception("folder doesn't exist");
		}

		this.folders.remove(folderName);
	}

	public void removeFile(String fileName) throws Exception {
		String filePath = Utils.getLastFilePathInName(fileName);
		if (!this.files.containsKey(filePath) || !this.files.get(filePath).containsKey(fileName)) {
			throw new Exception("file doesn't exist");
		}

		this.files.get(filePath).remove(fileName);
		if (this.files.get(filePath).isEmpty()) {
			this.files.remove(filePath);
		}
	}

	public List<Document> getAllFolders() {
		List<Document> list = new ArrayList<Document>(this.folders.values());
		Collections.sort(list, new Comparator<Document>() {
			public int compare(Document f1, Document f2) {
				return f1.name.compareTo(f2.name);
			}
		});
		return list;
	}

	public boolean existFile(String fileName) {
		String filePath = Utils.getLastFilePathInName(fileName);
		return this.files.containsKey(filePath) && this.files.get(filePath).containsKey(fileName);
	}

	// File names as "1-2-3abc.pdf" && "1-2-3xyc.pdf" are with the same path "1-2-3"
	public boolean existFileWithSamePath(String fileName) {
		String filePath = Utils.getLastFilePathInName(fileName);
		return this.files.containsKey(filePath);
	}

	public boolean existFiles() {
		return !this.files.isEmpty();
	}

	public boolean existFolder(String folderName) {
		return this.folders.containsKey(folderName);
	}

	public boolean existFolders() {
		return !this.folders.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof Document)) return false;

		Document folder = (Document) other;

		return folder.path.equals(this.path) &&
				folder.name.equals(this.name) &&
				this.isFolder == folder.isFolder;
	}

	@Override
	public int hashCode() {
		return this.path.hashCode() % 101 + this.name.hashCode() + Boolean.hashCode(this.isFolder);
	}
}
