package edu.gatech.listener;
import edu.gatech.gui.*;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SaveOpenListener extends JPanel implements ActionListener{
	JFileChooser jfc;
	JTextArea txtarea;
	Window window;
	
	public SaveOpenListener(Window w){
		//super(new BorderLayout());
		window = w;
		txtarea = new JTextArea(5,20);
		txtarea.setMargin(new Insets(5,5,5,5));
		txtarea.setEditable(false);
		jfc = new JFileChooser();
		try {
		    File f = new File(new File(".").getCanonicalPath());
		    jfc.setCurrentDirectory(f);
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Load Snapshot")){ 
			int retval = jfc.showOpenDialog(SaveOpenListener.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				File file = jfc.getSelectedFile();
				createFrame(file); 
			} else {
				txtarea.append("Open File command failed");
			}
			txtarea.setCaretPosition(txtarea.getDocument().getLength());
		} else if(e.getActionCommand().equals("Load Snapshots")){ 
			int retval = jfc.showOpenDialog(SaveOpenListener.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				File file = jfc.getSelectedFile();
				createFrame(file); 
			} else {
				txtarea.append("Open Directory command failed");
			}
			txtarea.setCaretPosition(txtarea.getDocument().getLength());
		} else if(e.getActionCommand().equals("Save Image")){
			int retval = jfc.showSaveDialog(SaveOpenListener.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				System.out.println("save to txt files...");
			}
			txtarea.setCaretPosition(txtarea.getDocument().getLength());
		} else if (e.getActionCommand().equals("Save Mesh")){
			int retval = jfc.showOpenDialog(SaveOpenListener.this);
			if(retval == JFileChooser.APPROVE_OPTION){
				File file = jfc.getSelectedFile();
				//populateTable(file);
			} else {
				txtarea.append("Open Directory command failed");
			}
			txtarea.setCaretPosition(txtarea.getDocument().getLength());
		}
	}
	protected void createFrame(File file){
		System.out.println("Creating frame");
		window.createViewFrame(file); 
    	/*if (suffix.equals("png")){
    		window.createFrame(); 
    	}
    	else if (suffix.equals("vts")){
    		window.createPackFrame(); 
    	}
		if(window == null){
			System.err.println("Initialize SaveOpenListener before use");
			System.exit(1);
		}*/
	}
}
