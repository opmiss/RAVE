package edu.gatech.gui;
import edu.gatech.interact.PackMode;
import edu.gatech.util.Snapshot;
import edu.gatech.vis.View;
import javax.swing.JInternalFrame;

/**
 * @author tina
 */

public class ViewFrame extends JInternalFrame{
	static final int xoffset = 250, yoffset = 10;
	static int width=700, height=700;
	public View tview;
	public ViewFrame(Snapshot ss){
		super("View",
	              true, //resizable
	              true, //closable
	              true, //maximizable
	              true);//iconifiable
		setSize(width+20,height+20);
		setLocation(xoffset, yoffset);
		tview = new View(width, height, ss); 
		this.add(tview);    
		tview.init();
	}	
	public void pack(){
		tview.Pack(); 
	}

	public void paint(){
		tview.Paint(); 
	}
	public void shake(){
		tview.Shake(); 
	}
	public void pull(){
		tview.Pull(); 
	}
	public void highlight(String txt){
		tview.Highlight(txt); 
	}
	public void print(){
		System.out.println(width+","+height); 
	}
}
