package edu.gatech.util;
import java.io.*;

public class FileWrite {
	BufferedWriter bw; 
	
	public FileWrite(String filename) throws IOException {
		File file = new File(filename); 
		file.getParentFile().mkdirs(); 
		bw = new BufferedWriter(new FileWriter(file));
	}
	
    public void writeLine(String line) throws IOException{
    	bw.write(line);
    	bw.newLine();
    }

    public void close() throws IOException {
		bw.close();
    }
    
}