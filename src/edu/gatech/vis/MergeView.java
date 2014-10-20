package edu.gatech.vis;
import edu.gatech.geo.*;
import edu.gatech.model.*;
import edu.gatech.util.Snapshot;
import processing.core.*;

public class MergeView extends PApplet {
	int width = 700, height = 700;
	Topic select; 
	Snapshot S;
	Layout L;
	int f=0; 
	public void setup(){
		size(width, height); 
		this.frameRate(20); 
		S = new Snapshot("../data/vn_label01.txt");
		S.fillCorrelation(); 
	}
	public void draw(){
		background(255); 
		L.showDocs(this); 
	}
	public void mousePressed(){
		select = L.pickTopics(Pt.mouse(this)); 	
	}
	public void mouseDragged(){
		if (select!=null) select.setTo(Pt.mouse(this)); 
	}
	public void mouseReleased(){
		select = null; 
	}
	public void keyPressed(){
		
	}
}