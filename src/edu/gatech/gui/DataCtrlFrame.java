package edu.gatech.gui;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.gatech.util.Meta;



public class DataCtrlFrame extends JInternalFrame implements ActionListener{
	
	static final int xoffset = 5, yoffset = 350;
	JTextField lookup_txt; 
	JTextField search_txt; 
	Window window; 
	
	public DataCtrlFrame(Window w){
		super("DataCtrl",
	              true, //resizable
	              false, //closable
	              false, //maximizable
	              true);//iconifiable
		setSize(200,250);
		setLocation(xoffset, yoffset);
		window = w; 
		init(); 
	}
	
	public void init(){
		this.setLayout(new GridLayout(0, 1)); 
		this.add(createSearchFilter());
		this.add(createLookupFilter()); 
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand()=="Highlight"){
			if (window.frame==null) {
				System.out.println("load data first"); 
				return; 
			}
			window.frame.highlight(lookup_txt.getText()); 
		}
		if (e.getActionCommand()=="Generate"){
			Meta collection = new Meta(); 
			collection.setDocs(search_txt.getText());
			collection.filterTopics(6); 
			collection.saveMeta("topicviz_"+search_txt.getText()); 
		}
	}
	private JPanel createLookupFilter(){
		JPanel filter = new JPanel(new FlowLayout());
		lookup_txt = new JTextField(10);
		JButton but = new JButton("Highlight");
		but.addActionListener(this); 
		filter.setBorder(BorderFactory.createTitledBorder("Lookup"));
		filter.add(lookup_txt); 
		filter.add(but);
		return filter; 
	}
	private JPanel createSearchFilter(){
		JPanel filter = new JPanel(new FlowLayout());
		search_txt = new JTextField(10);
		JButton but = new JButton("Generate");
		but.addActionListener(this); 
		filter.setBorder(BorderFactory.createTitledBorder("Search"));
		filter.add(search_txt); 
		filter.add(but);
		return filter; 
	}
}
