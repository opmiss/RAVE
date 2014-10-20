package edu.gatech.util;

public class TopicMeta {
	 private String Name; 
	 private int Id; //use id to search more terms in aan.db
	 
	 TopicMeta(int I, String N){
		 Id = I; Name = N; 
	 }
	 
	 public String getName(){
		 return Name; 
	 }
	 
	 public void setName(String s){
		 Name = s; 
	 }
	 
	 public int getId(){
		 return Id; 
	 }
	 
	 public void print(){
		 System.out.println(Name); 
	 }
	
}
