package edu.gatech.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

import edu.gatech.gui.ViewFrame;
import edu.gatech.gui.Window;
import edu.gatech.interact.PaintMode;

public class PaintListener implements ActionListener{
	Window window; 
	public PaintListener(Window w){
		window=w;
	}
	public void actionPerformed(ActionEvent event) {
		JComboBox cb = (JComboBox)event.getSource();
        String choice = (String)cb.getSelectedItem();
		if (choice.equals("none")){
			window.frame.tview.paint_mode = PaintMode.NONE; 
		}
		else if (choice.equals("one")){
			window.frame.tview.paint_mode = PaintMode.ONE; 
		}else if (choice.equals("pair")){
			window.frame.tview.paint_mode = PaintMode.PAIR; 
		}else if (choice.equals("all")){
			window.frame.tview.paint_mode = PaintMode.ALL; 
		}
	}
}
