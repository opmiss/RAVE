package edu.gatech.model;
import processing.core.PApplet;
import edu.gatech.geo.Color;
import edu.gatech.geo.Pt;
import edu.gatech.geo.Vec;

/**
 * @author tina (opmiss@gmail.com)
 */

public class Topic {
	public Pt pos; 
	public Vec offset; 
	public static final float r = 20; 
	public float radius; 
	Color color;
	public String label; 
	Layout layout; 
	
	public Topic(Pt p){
		pos = p; offset = new Vec(); radius =r; 
	}
	public Topic setColor(Color c){
		color = c; return this;  
	}
	public Topic setLayout(Layout l){
		layout = l; return this; 
	}
	public Topic setRadius(float rr){
		radius = rr; return this; 
	}
	public Topic resetRadius(){
		radius = r; return this; 
	}
	
	public Topic setLabel(String l){
		label=l; return this; 
	}
	public void show(PApplet pa){
	 // if (PApplet.dist(getX(), getY(), layout.getMouseX(), layout.getMouseY()) < radius) {
			pa.fill(0); 
			pa.textSize(24); 
			pa.text(label, getX(), getY()-radius-10);
	//	}
		pa.fill(147, 277, 249); 
		pa.ellipse(getX(), getY(), radius, radius); 
	}
	public void showCircle(PApplet pa){
		pa.ellipse(getX(), getY(), radius, radius); 
	}
	
	public void showLabel(PApplet pa){
		pa.fill(0); 
		pa.textSize(24); 
		pa.text(label, getX(), getY()-radius-4);
	}
	public float getX(){
		return pos.x+offset.x; 
	}
	public float getY(){
		return pos.y+offset.y; 
	}
	public Pt getP(){
		return pos; 
	}
	public float getR(){
		return radius; 
	}
	public Color getColor(){
		return color; 
	}
	boolean isIn(Pt m){
		if (m.disTo(pos)<radius) return true; 
		return false; 
	}
	public void setTo(Pt m){
		pos.setTo(m); 
		layout.update(); 
	}
	public void resetO(){
		offset=new Vec(); 
	}
	public void waveX(float A, float w, float t){
		offset.x= A*PApplet.sin(w*t); 
	}
	public void waveY(float A, float w, float t){
		offset.y= A*PApplet.cos(w*t); 
	}
}
