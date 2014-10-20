package edu.gatech.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import processing.core.PApplet;
import edu.gatech.geo.Pt;

public class Snapshot {
	
	private float stamp; //time stamp
	private float[][] correlation=null; //correlation matrix 
	private int[] member_count = null; //count of members in each topic
	private static ArrayList<String> topic_name = null; 
	private static ArrayList<String> doc_name = null; 
	private ArrayList<ArrayList<Float>> weight = null; 
	private static int doc_num, topic_num;
	
	public Snapshot(int t){
		stamp = t; 
	}
	
	public void setDocNum(int num){
		doc_num = num; 
	}
	
	public Snapshot(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f:files){
				setPar(f); 
			}
		}else{
			System.out.println("select directory");
		}
		if (doc_name!=null) doc_num = doc_name.size(); 
		else if (weight!=null) {
			doc_num = weight.size(); 
			doc_name = new ArrayList<String>(); 
			for (int i=0; i<doc_num; i++) doc_name.add(""); 
		}
		if (topic_name!=null) topic_num = topic_name.size(); 
		else if (weight!=null) {
			topic_num = weight.get(0).size(); 
			topic_name = new ArrayList<String>(); 
			for (int i=0; i<topic_num; i++) topic_name.add("");  
		}
	}
	
	public void setTime(float t){
		stamp = t; 
	}
	
	public Snapshot(String path){
		File file = new File(path);
		System.out.println(file.getAbsolutePath()); 
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File f:files){
				setPar(f); 
			}
			doc_num = doc_name.size(); 
			topic_num = topic_name.size(); 
			assert weight.size()==doc_num; 
			assert weight.get(0).size()==topic_num; 
		}else{
			System.out.println("select directory");
		}
	}
	
	public ArrayList<Integer> filter(String reg){
		ArrayList<Integer> in = new ArrayList<Integer>(); 
		int i=0; 
		for (String n:doc_name){
			if (n.toLowerCase().contains(reg.toLowerCase())) in.add(i); 
			i++; 
		}
		return in; 
	}

	public Snapshot(ArrayList<ArrayList<Float>> W, float t){
		weight = W; stamp = t; 
	}
	
	private void setPar(File file){
		String path = file.getAbsolutePath();
    	String suffix = path.substring(path.lastIndexOf('.')+1); 
    	if (suffix.equals("weight")){
    		setWeight(file);  
    	}
    	else if (suffix.equals("docmeta")){
    		setDocName(file); 
    	}
		if(suffix.equals("topicmeta")){
			setTopicName(file); 
		}	
	}
	
	public static ArrayList<String> getTopicNames(){
		return topic_name; 
	}
	
	public static String getTopicName(int i){
		return topic_name.get(i);
	}
	
	public static ArrayList<String> getDocNames(){
		return doc_name; 
	}
	
	public static String getDocName(int i){
		return doc_name.get(i); 
	}
	
	public static int getDocNum(){
		return doc_num; 
	}
	
	public static int getTopicNum(){
		return topic_num; 
	}
	
	public ArrayList<ArrayList<Float>> getWeights(){
		return weight; 
	}
	
	public void setWeight(File file){
	    Scanner scanner=null;
	    System.out.println(file.getAbsolutePath()); 
		try {
			System.out.println("set weight"); 
			weight = new ArrayList<ArrayList<Float>>(); 
			scanner = new Scanner(file); 
		    while(scanner.hasNextLine()) {
		    	if (scanner.hasNextFloat() == false) break;
		    	StringTokenizer stk = new StringTokenizer(scanner.nextLine(), " ");
		    	ArrayList<Float> wts = new ArrayList<Float>(); 
		    	while(stk.hasMoreTokens()){
		    		float val = Float.valueOf(stk.nextToken()); 
		    		//System.out.println(val); 
		    		wts.add(val);
		    	}
		    	weight.add(wts); 
		    }
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		finally {
			if (scanner!=null) scanner.close();
		}
	}
	
	public static void setTopicName(File file){
	    Scanner scanner=null;
	    topic_name = new ArrayList<String>(); 
		try {
			scanner = new Scanner(file); 
		    while(scanner.hasNextLine()) {
		    	topic_name.add(scanner.nextLine());
		    }
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		finally{
			if (scanner!=null) scanner.close();
			topic_num = topic_name.size(); 
		}
	}
	
	public static void setDocName(File file){
	    Scanner scanner=null;
	    doc_name = new ArrayList<String>(); 
		try {
			scanner = new Scanner(file); 
			doc_name = new ArrayList<String>(); 
		    while(scanner.hasNextLine()) {
		    	doc_name.add(scanner.nextLine());
		    }
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		finally{
			if (scanner!=null) scanner.close();
			doc_num = doc_name.size(); 
		}
	}
	
	public static Snapshot lerp(Snapshot A, Snapshot B, 
			Snapshot C, Snapshot D, float t){
		ArrayList<ArrayList<Float>> rw = new ArrayList<ArrayList<Float>>(); 
		for (int i=0; i<doc_num; i++){
			rw.add(Pt.LinearInterpolate(A.stamp, A.weight.get(i), 
					B.stamp, B.weight.get(i), 
					C.stamp, C.weight.get(i), 
					D.stamp, D.weight.get(i), t)); 
		}
		return new Snapshot(rw, t); 
	}
	
	public double sum(int k){ //sum the weights for kth topic 
		float s=0; 
		for (ArrayList<Float> w:weight){
			s+=w.get(k); 
		}
		return s; 
	}
	
	public double avg(int k){
		return sum(k)/weight.size(); 
	}
	
	public double correlation(double[][] WM, int k, int j){
		double mk = avg(k), mj=avg(j); 
		double dk, dj, skj=0, skk=0, sjj=0; 
		for (int n=0; n<doc_num; n++){
			dk = WM[n][k]-mk; dj = WM[n][j]-mj; 
			skj += dk*dj; skk += dk*dk; sjj += dj*dj; 
		}
		return skj/Math.sqrt(skk*sjj); 
	}
	
	public float cos_correlation(int k, int j){
		float ab=0, aa=0, bb=0;
		for (int n=0; n<doc_name.size(); n++){
			ab+=weight.get(n).get(k)*weight.get(n).get(j); 
			aa+=weight.get(n).get(k)*weight.get(n).get(k); 
			bb+=weight.get(n).get(j)*weight.get(n).get(j); 
		}
		return ab/(PApplet.sqrt(aa)*PApplet.sqrt(bb)); 
	}
	
	public void fillCorrelation(){
		int topic_num = topic_name.size(); 
		correlation = new float[topic_num][topic_num];
		for (int k=0; k<topic_num; k++){
			for (int j=0; j<topic_num; j++){
				correlation[k][j] = cos_correlation(k, j); 
			}
		}
	}
	
	public float getCorrelation(int i, int j){
		if (correlation==null){
			fillCorrelation(); 
		}
		return correlation[i][j]; 
	}
	
	public void countMembers(){
		member_count = new int[topic_num]; for (int j=0; j<topic_num; j++) member_count[j]=0;  
		int id; float c;  
		for (int i=0; i<doc_num; i++){
			id = -1; c=0;
			for (int j=0; j<topic_num; j++){
				if (c < weight.get(i).get(j)) {id = j; c = weight.get(i).get(j); } 
			}
			member_count[id] ++; 
		}
	}
	
	public ArrayList<Float> getWeight(int d){
		return weight.get(d); 
	}
	
	public int getMemberNum(int g){
		return member_count[g]; 
	}	
	
}
