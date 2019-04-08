package Core;

import java.io.File;
import java.util.*;

public class Document {
	private Map<String, Document> documents;
	
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
			this.documents = new HashMap<>();
		}
	}
	
	public void addDocument(Document document) throws Exception {
		if (this.documents.containsKey(document.name)) {
			throw new Exception("document exist");
		}
		
		this.documents.put(document.name, document);
	}

	public void removeDocument(String fileName) throws Exception {
		if (!this.documents.containsKey(fileName)) {
			throw new Exception("document doesn't exist");
		}

		this.documents.remove(fileName);
	}

	public List<Document> getDocuments() {
		List<Document> list = new ArrayList<Document>(this.documents.values());
		Collections.sort(list, new Comparator<Document>() {
			public int compare(Document f1, Document f2) {
				return f1.name.compareTo(f2.name);
			}
		});
		return list;
	}

	public boolean existFile(String name) {
		return this.documents.containsKey(name) && !this.documents.get(name).isFolder;
	}

	public boolean existSubFolder(String name) {
		return this.documents.containsKey(name) && this.documents.get(name).isFolder;
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
