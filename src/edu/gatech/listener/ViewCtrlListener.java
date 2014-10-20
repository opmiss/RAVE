package edu.gatech.listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import edu.gatech.gui.Window;
/**
 * @author tina
 */
public class ViewCtrlListener implements ActionListener{
	Window window; 
	public ViewCtrlListener(Window f){
		window=f;
	}
	public void actionPerformed(ActionEvent event) {
		if (window.frame==null) {
			System.out.println("load data first"); 
			return; 
		}
		String cmd = event.getActionCommand(); 
		if (cmd.equals("Pack")){
			System.out.println("Packing"); 
			window.frame.pack(); 
		}
		else if (cmd.equals("Paint")){
			System.out.println("Painting"); 
			window.frame.paint(); 
		}
		else if (cmd.equals("Pull")){
			System.out.println("Pull"); 
			window.frame.pull();
		}
		else if (cmd.equals("Shake")){
			System.out.println("Packing"); 
			window.frame.shake(); 
		}
		else if (cmd.equals("Animate")){
			System.out.println("Animate"); 
			window.createAnimFrame(); 
		}
	}
}
