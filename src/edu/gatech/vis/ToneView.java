package edu.gatech.vis;

import edu.gatech.geo.Pt;
import edu.gatech.model.Layout;
import edu.gatech.model.ToneMap;
import edu.gatech.util.Snapshot;

public class ToneView extends View{
	  ToneMap tone; 
	/*  public ToneView(){
		  super(); 
	  }
	  public ToneView(int w, int h, Snapshot ss){  
		  super(w, h, ss); 
		  tone = new ToneMap(this, layout); 
	  }*/
	  
	  public void setup() {
		  size(width, height); 
		  ellipseMode(RADIUS);
		  textAlign(CENTER); 
		  imageMode(CENTER); 
		  smooth();
		  if (layout==null) layout = 
				  (new Layout(this)).initLayout(new Snapshot("data/vn_label/"));
		  layout.paintAll(); 
		 // System.out.println(layout.topics.size()); 
		  if (tone==null)  tone = new ToneMap(this, layout); 
		  this.noLoop(); 
	  }
	  
	  public void draw() {
		  background(255); 
		  tone.show();
		  if (smooth) {tone.smooth(); step++; if (step>10) reset(); }
		  stroke(0); 
		  strokeWeight(1); 
		  layout.showDocs(this); 
		   noFill(); 
		  layout.showTopicCircle(this); 
	  }
	  
	  public void mousePressed(){
		  select = layout.pickTopics(Pt.mouse(this)); 
	  }
	  public void mouseDragged(){
		  reset(); 
		  if (select!=null) {
			  select.setTo(Pt.mouse(this)); layout.update(); tone.fillToneMap();  this.redraw(); 
		  }
	  } 
	  boolean smooth = false; int step =0; 
	  public void mouseReleased(){
		  start(); 
	  }
	  public void start(){
		  smooth=true; 
		  step = 0; 
		  this.loop(); 
	  }
	  public void reset(){
		  smooth = false; this.noLoop(); 
	  }
	  public void pack(){
		  layout.packon();  this.redraw(); 
	  }
	  public void wave(){
		  layout.toggleWave(); 
	  }
	  public void keyPressed(){
		  if (key=='p') pack(); 
		  if (key=='w') wave(); 
		  if (key=='h') {System.out.println("heatmap"); }
		  if (key=='c') saveFrame("pic/pic-####.png"); 
		  if (key=='s') {tone.smooth(); this.redraw(); }
	  }
}
