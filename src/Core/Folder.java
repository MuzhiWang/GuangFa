package Core;

import java.util.*;

public class Folder {
	private Set<Document> documents;
	private Map<String, Folder> subFolders;
	
	public String name;
	
	public Folder(String name) {
		this.documents = new HashSet<>();
		this.subFolders = new HashMap<>();
		this.name = name;
	}
	
	public void addDocument(Document file) throws Exception {
		if (this.documents.contains(file)) {
			throw new Exception("document exist");
		}
		
		this.documents.add(file);
	}
	
	public void addSubFolder(Folder subFolder) throws Exception {
		if (this.subFolders.containsKey(subFolder.name)) {
			throw new Exception("sub folder exist");
		}

		this.subFolders.put(subFolder.name, subFolder);
	}

	public List<Document> getDocuments() {
		List<Document> list = new ArrayList<Document>(this.documents);
		Collections.sort(list, new Comparator<Document>() {
			public int compare(Document f1, Document f2) {
				return f1.fileName.compareTo(f2.fileName);
			}
		});
		return list;
	}

	public boolean existDocument(String name) {
		return this.documents.contains(name);
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
