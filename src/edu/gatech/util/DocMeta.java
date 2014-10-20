package edu.gatech.util;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocMeta {
	 private ArrayList<Float> Weight;
	 private String Name; 
	 private String Authors; 
	 private String Venue; 
	 private int Year;
	 private int Id; 
	 public DocMeta(int I, String N, String A, String V, int Y) {
		 Id=I; 
		 Name = N; 
		 Authors = A; 
		 Venue = V; 
		 Year = Y; 
		 Weight = null; 
	 }
	 public void setWeight(){ 
		 Search s = new Search(); 
		 try {
			 Weight = s.getWeight(Id); 
		 }
		 catch (SQLException e) {
			 System.err.println(e.getMessage());
			 }
		 s.cleanup(); 
	 }
	 public void setWeight(ArrayList<Float> w){
		 Weight = w; 
	 }
	 public String getName(){
		 return Name; 
	 }
	 public String getAuthors(){
		 return Authors; 
	 }
	 public String getVenue(){
		 return Venue; 
	 }
	 public int getYear(){
		 return Year; 
	 }
	 public int getId(){
		 return Id; 
	 }
	 public void print(){
		 System.out.println(Id+", "+Authors+", "+Name+", "+Venue+", "+Year); 
		 if (Weight!=null){
			 for (Float w:Weight){
				 System.out.print("| "+w); 
			 }
			 System.out.println(); 
		 }
	 }
	 public ArrayList<Float> getWeight(){
		 return Weight; 
	 }
	 
	 public String getWeightString(){
		 StringBuffer sb = new StringBuffer(); 
		 for (Float f:Weight){
			 sb.append(f).append(' ');  
		 }
		 return sb.toString(); 
	 }
	 
}
