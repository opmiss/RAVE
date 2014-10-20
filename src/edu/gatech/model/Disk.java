package edu.gatech.model;
import edu.gatech.geo.*;
import processing.core.*;

/**
 * @author tina
 */

public class Disk {
	  public Pt O; 
	  public float R;
	  Disk( float x, float y, float r){
	    O = new Pt(x, y); 
	    R = r; 
	  }
	  void show(PApplet pa){
	    O.showCircle(R, pa) ; 
	  }
	  
	  public float area(){
		  return PApplet.PI*R*R; 
	  }
}