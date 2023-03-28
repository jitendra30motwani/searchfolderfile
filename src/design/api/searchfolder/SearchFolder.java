/**
 * 
 */
package design.api.searchfolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author 91978
 *
 *
 *searchFiles(String dirName, searchRules)
 *
 *usecases:
 *Search by File name
 *Search by Extension
 *
 *file has this name and this extension
 *
 *
 *https://www.baeldung.com/java-list-directory-files
 */
public class SearchFolder {
	
	
	public static void main(String args[]) throws IOException {
		
		SearchFolder folder = new SearchFolder();
		
		String dirName="C:\\Jitendra Stuff\\Prepare";
		
		String fileName="FileSystem.docx";
		
		String extName = "txt";
		
		Filter filter = new NameFilter(fileName);
		
		Filter extFilter = new ExtFilter(extName);
		
		List<Filter> filters = new ArrayList<>();
		filters.add(filter);
		filters.add(extFilter);
		
		@SuppressWarnings("unused")
		Filter andFilter = new AndFilter(filters);
		
		Filter orFilter = new OrFilter(filters);
		
		//List<File> files = folder.searchFiles(dirName, filter);
		
		//List<File> files = folder.searchFiles(dirName, extFilter);
		
		//List<File> files = folder.searchFiles(dirName, andFilter);
		
		List<File> files = folder.searchFiles(dirName, orFilter);
		
		System.out.println(files);
		
	}
	
	public List<File> searchFiles(String dirName,Filter filter) throws IOException{
		
		List<File> files = new LinkedList<>();
		
		List<String> allFiles = getAllFiles(dirName);
		
		for(String fileName : allFiles) {
			
			File f = new File(fileName);
			
			if(filter.match(f)) {
				
				files.add(f);
			}
			
		}
		
		return files;
		
	}

	public List<String> getAllFiles(String dirName) throws IOException {
		
		@SuppressWarnings("resource")
		Stream<Path> pathStream = Files.walk(Paths.get(dirName), Integer.MAX_VALUE);
		
		List<String> files = new LinkedList<>();
		
		pathStream.forEach(path -> {
			
			files.add(path.getFileName().toString());
			
		});
		
		return files;
	}
	
	
}

interface Filter{
	
	boolean match(File f);
	
}

class NameFilter implements Filter{
	
	private String fileName; 
	
	public NameFilter(String name) {
		
		this.fileName = name;
		
	}
	
	public boolean match(File f) {
		
		return this.fileName.equals(f.getName());
		
	}
	
}

class ExtFilter implements Filter{
	
	private String extName; 
	
	public ExtFilter(String extName) {
		
		this.extName = extName;
		
	}
	
	public boolean match(File f) {
		
		String paths[] = f.getName().split("\\.");
		
		if(paths.length > 0) {
			
			return this.extName.equals(paths[paths.length-1]);
			
		}else {
			
			return false;
		}
		
		
	}
	
}

class AndFilter implements Filter{
	
	private List<Filter> filters;
	
	public AndFilter(List<Filter> filters) {
		
		this.filters = filters;

	}
	
	public boolean match(File f) {
		
		for(Filter filter: filters) {
			
			if(!filter.match(f)) {
				return false;
			}
		}
		
		return true;
		
	}
	
}

class OrFilter implements Filter{
	
	private List<Filter> filters;
	
	public OrFilter(List<Filter> filters) {
		
		this.filters = filters;

	}
	
	public boolean match(File f) {
		
		for(Filter filter: filters) {
			
			if(filter.match(f)) {
				return true;
			}
		}
		
		return false;
		
	}
	
}
