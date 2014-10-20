package edu.gatech.model;
import java.util.ArrayList;
import processing.core.PApplet;
import edu.gatech.geo.Color;
import edu.gatech.geo.Pt;
import edu.gatech.geo.Vec;
/**
 * @author tina
 */
public class Doc {
	//geometry properties 
	Pt pos; 
	Vec offset; 
	//ArrayList<Pt> blur; 
	//high dimensional parameters 
	ArrayList<Float> weight;  
	//display parameters 
	//public static final float r=7f, dr2=14, dr22=196; 
	//public static final float r=5f, dr2=10, dr22=100; 
	public static final float r=2f, dr2=4, dr22=16; 
	public static ArrayList<Topic> topics; 
	public static Layout layout; 
	Color color; 
	//metadata
	public String label; 
	boolean show_label=false; 
	
	public Doc(){
		pos = new Pt(0, 0); offset = new Vec();  color = Color.White; 
	}
	
	public static void setTopics(ArrayList<Topic> t){
		topics =t; 
	}
	
	public static void setLayout(Layout l){
		layout=l; 
	}
	
	public Doc setWeight(ArrayList<Float> w){
		weight=w;
		pos.setTo(0, 0); 
		int i=0; 
		for (Topic t:topics){
			pos.x+=weight.get(i)*t.getX(); 
			pos.y+=weight.get(i)*t.getY(); 
			i++; 
		} return this; 
	}
	
	public Doc setColor(){
		float[] lab = new float[3];  
		for (int k=0; k<3; k++) lab[k]=0; 
		int i=0; 
		for (Topic t:topics){
			float[] tlab = t.getColor().toLab(); 
			lab[0]+=weight.get(i)*tlab[0]; 
			lab[1]+=weight.get(i)*tlab[1]; 
			lab[2]+=weight.get(i)*tlab[2]; 
			i++; 
		}
		color = Color.fromLab(lab);  
		return this; 
	}
	
	public Doc setColor(Color C){
		if (color == Color.White) color = C; 
		else {
			color = Color.interpolate(color, C, 0.4f); 
		}
		return this; 
	}
	
	public Doc resetColor(){
		color = Color.White; return this; 
	}
	
	public Doc setLabel(String l){
		label =l; return this; 
	}
	
	public float getX(){
		return pos.x+offset.x;  
	}
	
	public float getY(){
		return pos.y+offset.y; 
	}
	
	public float getCX(){
		return pos.x; 
	}
	
	public float getCY(){
		return pos.y; 
	}
	
	public void addO(float x, float y){
		offset.x+=x; offset.y+=y; 
	}
	
	public void subO(float x, float y){
		offset.x-=x; offset.y-=y;  
	}
	
	public void sink(){
		offset.x*=0.99; offset.y*=0.99; 
	}
	
	public void showLabel(PApplet pa){
		pa.fill(0, 0, 150);
		pa.textSize(12); 
		pa.text(label, getX(), getY()-r-4);
		pa.ellipse(getX(), getY(), r, r); 
	}
	
	public void show(PApplet pa){
		boolean in = PApplet.dist(getX(), getY(), layout.getMouseX(), layout.getMouseY()) < r+2; 
		if ( show_label || (pa.key=='d' && in )) {
			if (pa.mousePressed && in) show_label = !show_label; 
			pa.strokeWeight(2); 
			pa.stroke(0);
			pa.fill(10); 
			pa.textSize(14); 
			pa.text(label, getX(), getY()-r-4);
		}
		else{
			pa.stroke(150, 150);
			pa.strokeWeight(r/3); 
		}
		color.fill(pa);
		pa.ellipse(getX(), getY(), r, r); 
	}
	
	public void showSmall(PApplet pa){
		if (show_label || PApplet.dist(getX(), getY(), layout.getMouseX(), layout.getMouseY()) < r+2) {
			if (pa.keyPressed&&pa.key=='s') show_label = true; 
			pa.fill(255, 10, 10);
		}
		else {pa.fill(100, 150);} 
		pa.ellipse(getX(), getY(), r/2, r/2); 
	}
	
	public void showMedium(PApplet pa){
		if (show_label || PApplet.dist(getX(), getY(), layout.getMouseX(), layout.getMouseY()) < r+2) {
			if (pa.keyPressed&&pa.key=='s') show_label = true; 
			pa.fill(255, 10, 10);
		}
		else {pa.fill(100);} 
		pa.ellipse(getX(), getY(), r, r); 
	}

	public void showTransparent(PApplet pa){
		color.fill(pa, 20); 
		pa.ellipse(getX(), getY(), r, r); 
	}
	
	public void showGray(PApplet pa){
		if (show_label || PApplet.dist(getX(), getY(), layout.getMouseX(), layout.getMouseY()) < r+2) {
			if (pa.keyPressed&&pa.key=='s') show_label = !show_label; 
			pa.fill(250, 10, 10);
			pa.textSize(14); 
			pa.text(label, getX(), getY()-r-4);
		}
		else {
			pa.fill(180); 
		} 
		pa.ellipse(getX(), getY(), r, r); 
	} 
	
	public void showColor(PApplet pa){
		color.fill(pa); 
		pa.ellipse(getX(), getY(), r, r); 
	}
	
	public void update(ArrayList<Topic> T){
		int i=0; 
		pos.x=0; pos.y=0; 
		for (Topic t:T){
			pos.x+=weight.get(i)*t.getX(); 
			pos.y+=weight.get(i)*t.getY(); 
			i++; 
		}
	}
	
}
