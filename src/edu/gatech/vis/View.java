package edu.gatech.vis;
import edu.gatech.geo.Pt;
import edu.gatech.interact.PackMode;
import edu.gatech.interact.PaintMode;
import edu.gatech.interact.PullMode;
import edu.gatech.model.Grid;
import edu.gatech.model.Layout;
import edu.gatech.model.Topic;
import edu.gatech.util.Snapshot;
import processing.core.PApplet;

public class View extends PApplet {
	  public Layout layout; 
	  Topic select; 
	  public static int width=800;
	  public static int height=800; 
	  boolean wave = false;  
	  public PackMode pack_mode = PackMode.OFF;
	  public PaintMode paint_mode = PaintMode.NONE; 
	  public PullMode pull_mode = PullMode.OFF; 
	  
	  public View(){
		  //super(); 
	  }
	  
	  public View(int w, int h, Snapshot ss){  
		  width = w; 
		  height = h; 
		  layout = (new Layout(this)).initLayout(ss); 
	  }
	  
	  public static Pt getCenter(){
		  return new Pt(width/2, height/2); 
	  }
	  
	  public static float getRadius(){
		  return min(width, height)/2; 
	  }
	  
	  public void setup() {
		  size(width, height);
		  background(255); 
		  ellipseMode(RADIUS);
		  textAlign(CENTER); 
		  imageMode(CENTER); 
		  //if (font==null) font = createFont("Univers-Bold", 12);
		  //textFont(font);
		  smooth();
		  if (layout==null) layout = 
				  (new Layout(this)).initLayout(new Snapshot("data/vn_label/"));
		//  this.noLoop(); 
	  }
	  
	  public void draw() {
		  background(255); 
		  stroke(0); 
		  pull_mode.show(layout);
		  layout.show(this); 
		 /* show packing scheme 
		  layout.showTopicCircle(this); 
		  layout.showDocs(this);*/ 
	  }
	  
	  public void mousePressed(){
		  select = layout.pickTopics(Pt.mouse(this)); 
	  }
	  
	  public void mouseDragged(){
		  if (select!=null) {
			select.setTo(Pt.mouse(this)); layout.update(); pack_mode.update(layout); pull_mode.update(layout);
		  }
	  }
	  
	  public void Paint(){
		 paint_mode.paint(layout); 
	  }
	  
	  public void Pack(){
		  pack_mode.pack(layout); 
	  }
	  
	  public void Shake(){
		  //this.frameRate(30); 
		  layout.toggleWave(); 
	  }
	  
	  public void Pull(){
		  layout.togglePull(); 
		  pull_mode.update(layout);
	  }
	  
	  public void Highlight(String txt){
		  layout.highlight(txt); 
	  }
	  
	  public void keyPressed(){
		  if (key=='1') {pack_mode = PackMode.REPEL;}
		  if (key=='2') {pack_mode = PackMode.RASTER;}
		  if (key=='3') {pack_mode = PackMode.MIXED;}
		  if (key=='4') {pack_mode = PackMode.OFF;}
		  if (key=='p') {pack_mode.pack(layout);}
		  if (key=='h') layout.paintAll(); 
		  if (key=='w') Shake(); 
		  if (key=='t') layout.profile();
		  if (key=='c') {
			  System.out.println("save a picture");
			  saveFrame("pic/pic-####.png"); 
		  }
	  }
}
