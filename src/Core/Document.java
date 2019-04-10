package Core;

import Tools.Utils;

import java.io.File;
import java.util.*;

public class Document {
	private Map<String, Document> folders;
	private Map<String, Map<String, Document>> files;
	
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
		String filePath = Utils.queryFilePathInName(file.name);
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

	public Document getFolder(String name) {
		if (!this.folders.containsKey(name)) {
			return null;
		}

		return this.folders.get(name);
	}

	public void removeFolder(String folderName) throws Exception {
		if (!this.folders.containsKey(folderName)) {
			throw new Exception("document doesn't exist");
		}

		this.folders.remove(folderName);
	}

	public List<Document> getFolders() {
		List<Document> list = new ArrayList<Document>(this.folders.values());
		Collections.sort(list, new Comparator<Document>() {
			public int compare(Document f1, Document f2) {
				return f1.name.compareTo(f2.name);
			}
		});
		return list;
	}

	public boolean existFile(String name) {
		String filePath = Utils.queryFilePathInName(name);
		return this.files.containsKey(filePath) && this.files.get(filePath).contains();
	}

	public boolean existSubFolder(String name) {
		return this.folders.containsKey(name) && this.folders.get(name).isFolder;
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
