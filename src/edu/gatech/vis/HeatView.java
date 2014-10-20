package edu.gatech.vis;
import edu.gatech.geo.Pt;
import edu.gatech.model.Heat;
import edu.gatech.model.Layout;
import edu.gatech.util.Snapshot;
/**
 * @author tina
 */
public class HeatView extends View {
	  Heat heat; 
	  public HeatView(){
		  super(); 
	  }
	  
	  public HeatView(int w, int h, Snapshot ss){  
		  super(w, h, ss); 
		  heat = new Heat(this, layout); 
	  }
	  
	  public void setup() {
		  size(width, height); 
		  ellipseMode(RADIUS);
		  textAlign(CENTER); 
		  imageMode(CENTER); 
		  smooth();
		  layout = 
				  (new Layout(this)).initLayout(new Snapshot("data/vn_label/"));
		  if (heat==null)  heat = new Heat(this, layout); 
		  this.noLoop(); 
	  }
	  public void draw() {
		  background(255); 
		  heat.show();
		  if (smooth) {heat.smooth(); step++; if (step>10) reset(); }
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
			  select.setTo(Pt.mouse(this)); layout.update(); heat.fillMap();  this.redraw(); 
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
		  if (key=='s') {heat.smooth(); this.redraw(); }
	  }
}
