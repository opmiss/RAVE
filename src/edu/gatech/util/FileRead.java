package edu.gatech.util;
import java.io.*; 
import java.net.URL;

public class FileRead {
	
	BufferedReader br; 
	
	public FileRead(String filename){
        try {
			br = new BufferedReader(new FileReader(new File(filename)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileRead(URL url){
	     try {
			br = new BufferedReader(new InputStreamReader(url.openStream()));
	     } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileRead(InputStream filestream){
        try {
			br = new BufferedReader(new InputStreamReader(filestream));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public String readLine() {
    	try {
				return br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				return null; 
			}
    }
    
    public void close() {
    	try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
    
}
