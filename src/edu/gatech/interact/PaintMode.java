package edu.gatech.interact;

import edu.gatech.model.Layout;

public enum PaintMode {
	NONE, ONE, PAIR, ALL;
	private static int first=0, second=1; 
	public void print(){
		switch(this){
			case NONE:
				System.out.println("no color");
				break; 
			case ONE: 
				System.out.println("one color");
				break; 
			case PAIR:
				System.out.println("two colors"); 
			    break; 
			case ALL:
				System.out.println("colors"); 
			break; 
		}
	}
	private int next(int i){
		return (i==Layout.topics.size()-1)? 0:(i+1); 
	}
	private void nextOne(){
		first = next(first); 
		System.out.println(first); 
	}
	private void nextPair(){
		second = next(second); 
		if (second==first) first =next(first); 
		System.out.println(first+", "+second); 
	}
	private void reset(){
		first =0; second = 1; 
	}
	public void paint(Layout layout){
		switch(this){
			case NONE:
				layout.paintNone(); reset(); 
				break; 
			case ONE: 
				nextOne();  layout.paint(first); 
				break; 
			case PAIR:
				nextPair(); layout.paint(first, second); 
				break; 
			case ALL:
				layout.paintAll(); 
				break; 
		}	
	}
}
