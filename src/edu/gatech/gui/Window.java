package edu.gatech.gui;
import edu.gatech.listener.SaveOpenListener;
import edu.gatech.util.Snapshot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Window extends JFrame {
	JDesktopPane desktop;
	public ViewFrame frame; 
	public AnimFrame aframe; 
	ViewCtrlFrame ctrlFrame; 
	DataCtrlFrame dataFrame; 
	SaveOpenListener file_listener;
	
	public Window(){
		super("RAVE: Responsive Animated Visualization Environment");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init();
	}
	
	private void init(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int inset = 0;
		setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);
		desktop = new JDesktopPane();
		setContentPane(desktop);
		JMenuBar menubar = create_menubar();
		setJMenuBar(menubar);
		desktop.setBackground(Color.CYAN);
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		createIconFrame(); 
		createDataFrame(); 
	}

	protected void createIconFrame(){
		ctrlFrame = new ViewCtrlFrame(this);
		ctrlFrame.setVisible(true);
        desktop.add(ctrlFrame);
        try {
            ctrlFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
	}
	
	protected void createDataFrame(){
		dataFrame = new DataCtrlFrame(this);
		dataFrame.setVisible(true);
        desktop.add(dataFrame);
        try {
            dataFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
	}
	
	public void createViewFrame(File file){
		frame = new ViewFrame(new Snapshot(file)); 
		frame.setVisible(true);
		frame.setBackground(Color.WHITE);
		desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {e.printStackTrace();}
	}
	
	public void createAnimFrame(){
		aframe = new AnimFrame(); 
		aframe.setVisible(true);
		aframe.setBackground(Color.WHITE);
		desktop.add(aframe);
        try {
            aframe.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {e.printStackTrace();}
	}
	
	private JMenuBar create_menubar(){
		JMenuBar menubar = new JMenuBar();
		file_listener = new SaveOpenListener(this);
		JMenu file = new JMenu("File");
		JMenuItem load = new JMenuItem("Load Snapshot");
		load.addActionListener(file_listener);
		file.add(load);
		JMenuItem loads = new JMenuItem("Load Snapshots");
		loads.addActionListener(file_listener);
		file.add(loads);
		JMenu help = new JMenu("Help"); 
		menubar.add(file);
		menubar.add(help);
		return menubar;
	}
	public static void main(String[]args){
		//JFrame
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
			e.printStackTrace(); 
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
		Window window = new Window();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
}
