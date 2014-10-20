package edu.gatech.gui;
import javax.swing.JInternalFrame;
import edu.gatech.model.Anim;
import edu.gatech.util.Snapshot;
import edu.gatech.vis.Animation;
import edu.gatech.vis.View;

public class AnimFrame extends JInternalFrame{
	static final int xoffset = 250, yoffset = 10;
	static int width=700, height=700;
	public Animation aview;
	public AnimFrame(){
		super("Animation",
	              true, //resizable
	              true, //closable
	              true, //maximizable
	              true);//iconifiable
		setSize(width+20,height+20);
		setLocation(xoffset, yoffset);
		aview = new Animation(width, height); 
		this.add(aview);    
		aview.init();
	}	
	public void print(){
		System.out.println(width+","+height); 
	}
}