package Core;

public class Document {
	public String fileName;
	public String path;
	
	public Document(String fileName, String path) {
		this.fileName = fileName;
		this.path = path;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof Document)) return false;
		
		Document file = (Document) other;
		
		return file.path.equals(this.path) && file.fileName.equals(this.fileName);
	}
	
	@Override
	public int hashCode() {
		return this.path.hashCode() % 101 + this.fileName.hashCode();
	}
}
