
package edu.gatech.vis;
import java.io.File;
import edu.gatech.geo.Pt;
import edu.gatech.model.*;
import edu.gatech.util.Snapshot;
import processing.core.*;

/**
 * @author tina
 */

public class Animation extends View {
	Anim anim; 
	public static int width = 700; 
	public static int height = 700; 
	public boolean refresh = false; 
	public boolean toggle = false; 
	int f=0; 
	public Animation(){
		//super(); 
	}
	public Animation(int w, int h){  
		  width = w; 
		  height = h; 
		  anim = (new Anim(this)).setKey(new File("data/vn_label_animation/")); 
		  anim.fillTween(); 
		  layout = (new Layout(this)).initLayout(anim.getCurrent()); 
	}
	public Animation(int w, int h, File file){  
		  width = w; 
		  height = h; 
		  anim = (new Anim(this)).setKey(file); 
		  layout = (new Layout(this)).initLayout(anim.getCurrent()); 
	}
	public void setup(){
		size(width, height);
		ellipseMode(RADIUS);
		textAlign(CENTER); 
		anim = (new Anim(this)).setKey(new File("data/vn_label_animation/")); 
		anim.fillTween(); 
		this.noStroke(); 
		layout = (new Layout(this)).initLayout(anim.getCurrent());
		layout.paintAll(); 
		this.frameRate(20); 
	}
	
	public void draw(){
		// if (refresh || anim.stop) {
			 background(255); 
			  layout.showTopicLabel(this);
			  layout.showDocs(this);
			  
		/*	}
		 else{
		      tint(255, 50);
			  layout.showTransDocs(this); 
		  } 
		 if (toggle&&refresh) {refresh=false; toggle = false; background(255);   } */
		anim.next(); 
		/*if (anim.stop) {
			 background(255); 
			  layout.showTopicLabel(this);
			  layout.showDocs(this);
		}
		else { 
			 if (toggle) {layout.showDocs(this); saveFrame("anim/pic-####.png");}
			 else layout.showTransDocs(this); 
		} 
		if (toggle) {toggle = false; background(255);   } 
		anim.next();*/
	}
	
	public void mouseReleased(){
		select = null; 
	}
	
	public void toggle(){
		toggle = true; //refresh = true; 
	}
	
	public void keyPressed(){
		super.keyPressed(); 
		if (key==' ') anim.toggleStop(); 
		if (key=='b')  refresh = !refresh; 
		if (key =='t') {toggle();   }
		if (key == 'c'){saveFrame("anim/pic-####.png"); }
	}
	
}
