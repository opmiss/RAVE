package edu.gatech.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import edu.gatech.gui.ViewFrame;
import edu.gatech.gui.Window;
import edu.gatech.interact.PackMode;


public class PackListener implements ActionListener{
	Window window; 
	public PackListener(Window w){
		window=w;
	}
	public void actionPerformed(ActionEvent event) {
		JComboBox cb = (JComboBox)event.getSource();
        String choice = (String)cb.getSelectedItem();
		if (choice.equals("raster")){
			window.frame.tview.pack_mode= PackMode.RASTER; 
		}else if (choice.equals("repel")){
			window.frame.tview.pack_mode = PackMode.REPEL; 
		}else if (choice.equals("mixed")){
			window.frame.tview.pack_mode = PackMode.MIXED; 
		}
		else if (choice.equals("off")){
			window.frame.tview.pack_mode = PackMode.OFF; 
		}
	}
}
