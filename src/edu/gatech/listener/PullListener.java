package edu.gatech.listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import edu.gatech.gui.Window;
import edu.gatech.interact.PullMode;

public class PullListener implements ActionListener{
	Window window; 
	public PullListener(Window w){
		window=w;
	}
	public void actionPerformed(ActionEvent event) {
		JComboBox cb = (JComboBox)event.getSource();
        String choice = (String)cb.getSelectedItem();
		if (choice.equals("off")){
			window.frame.tview.pull_mode = PullMode.OFF; 
		}
		else if (choice.equals("bump")){
			window.frame.tview.pull_mode = PullMode.BUMP; 
		}
		else if (choice.equals("channel")){
			window.frame.tview.pull_mode = PullMode.CHANNEL; 
		}
		else if (choice.equals("mixed")){
			window.frame.tview.pull_mode = PullMode.MIXED; 
		}
	}
}