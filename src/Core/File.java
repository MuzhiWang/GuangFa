package Core;

public class File {
	public String fileName;
	public String path;
	
	public File(String fileName, String path) {
		this.fileName = fileName;
		this.path = path;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this) return true;
		if (!(other instanceof File)) return false;
		
		File file = (File) other;
		
		return file.path.equals(this.path) && file.fileName.equals(this.fileName);
	}
	
	@Override
	public int hashCode() {
		return this.path.hashCode() % 101 + this.fileName.hashCode();
	}
}
