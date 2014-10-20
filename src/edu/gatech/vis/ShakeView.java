package edu.gatech.vis;

import edu.gatech.geo.Pt;
import edu.gatech.model.Layout;
import edu.gatech.model.ToneMap;
import edu.gatech.util.Snapshot;

public class ShakeView extends View{
	  boolean refresh = true; 
	  public ShakeView(){
		  super(); 
	  }
	  public ShakeView(int w, int h, Snapshot ss){  
		  super(w, h, ss); 
	  }
	  public void setup() {
		  size(width, height); 
		  ellipseMode(RADIUS);
		  textAlign(CENTER); 
		  imageMode(CENTER); 
		  smooth();
		  if (layout==null) layout = 
				  (new Layout(this)).initLayout(new Snapshot("data/vn_label/"));
		 // layout.paintAll(); 
		  this.noLoop(); 
	  }
	  public void draw() {
		  if (refresh) {
			 background(255); 
			 layout.showMediumDocs(this);
		  }
		  else{
			  if (layout.wave) layout.showSmallDocs(this);
		  }
	  }
	  
	  public void mousePressed(){
		  select = layout.pickTopics(Pt.mouse(this)); 
	  }
	  public void mouseDragged(){
		  reset(); 
		  if (select!=null) {
			  select.setTo(Pt.mouse(this)); layout.update();  this.redraw(); 
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
		  this.frameRate(30); 
		  layout.toggleWave(); 
	  }
	  public void keyPressed(){
		  if (key=='p') pack(); 
		  if (key=='w') wave(); 
		  if (key=='h') {System.out.println("heatmap"); }
		  if (key=='c') saveFrame("pic/pic-####.png"); 
		  if (key=='b') {
			  refresh = !refresh; 
		  }
	  }
}
