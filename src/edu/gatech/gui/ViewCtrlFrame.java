package edu.gatech.gui;
import javax.swing.JInternalFrame;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import edu.gatech.listener.PackListener;
import edu.gatech.listener.PaintListener;
import edu.gatech.listener.PullListener;
import edu.gatech.listener.ViewCtrlListener;
/**
 * @author tina
 */
public class ViewCtrlFrame extends JInternalFrame {
	static final int xoffset = 5, yoffset = 15;
	PackListener pack_lis; 
	PaintListener paint_lis; 
	PullListener pull_lis; 
	ViewCtrlListener vcl; 
	Window window; 
	
	public ViewCtrlFrame(Window w){
		super("ViewCtrl",
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
		pack_lis = new PackListener(window); 
		paint_lis = new PaintListener(window); 
		pull_lis = new PullListener(window); 
		vcl = new ViewCtrlListener(window); 
		this.setLayout(new GridLayout(0, 1)); 
		this.add(createPackPanel()); 
		this.add(createPaintPanel()); 
		this.add(createPullPanel()); 
		this.add(createAnimPanel());
	}
	
	ImageIcon resize(ImageIcon in, int width, int height){
		if (in==null) return null;
		return new ImageIcon(in.getImage().getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH));
	}
	
	private JPanel createPackPanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon packIcon = resize(createImageIcon("/edu/gatech/resource/pack.gif"), 22,22);
		JButton pack = new JButton("Pack", packIcon); 
		pack.addActionListener(vcl); 
		
		String[] packChoice = {"off", "repel", "raster", "mixed"}; 
		JComboBox packList = new JComboBox(packChoice);
		packList.setSelectedIndex(0);
		packList.addActionListener(pack_lis);
		ret.add(pack); ret.add(packList);  
		return ret; 
	}
	
	private JPanel createPaintPanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon paintIcon = resize(createImageIcon("/edu/gatech/resource/tint.gif"), 22,22);
		JButton paint = new JButton("Paint", paintIcon); 
		paint.addActionListener(vcl);  
		
		String[] paintChoice = {"none", "one", "pair", "all"}; 
		JComboBox paintList = new JComboBox(paintChoice);
		paintList.setSelectedIndex(0);
		paintList.addActionListener(paint_lis);
		ret.add(paint); ret.add(paintList); 
		return ret; 
	}
	
	private JPanel createTintPanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon paintIcon = resize(createImageIcon("/edu/gatech/resource/blur.gif"), 22,22);
		JButton paint = new JButton("Tint", paintIcon); 
		paint.addActionListener(vcl);  
		
		String[] tintChoice = {"none", "one", "pair", "all"}; 
		JComboBox tintList = new JComboBox(tintChoice);
		tintList.setSelectedIndex(0);
		tintList.addActionListener(vcl);
		ret.add(paint); ret.add(tintList); 
		return ret; 
	}
	
	private JPanel createShakePanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon icon = resize(createImageIcon("/edu/gatech/resource/shake.gif"), 25,25);
		JButton but = new JButton("Shake", icon); 
		but.addActionListener(vcl);  
		
		String[] choice = {"wave"}; 
		JComboBox list = new JComboBox(choice);
		list.setSelectedIndex(0);
		list.addActionListener(vcl);
		ret.add(but); ret.add(list); 
		return ret; 
	}
	
	private JPanel createAnimPanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon icon = resize(createImageIcon("/edu/gatech/resource/anim.gif"), 30,22);
		JButton but = new JButton("Anim", icon); 
		but.addActionListener(vcl);  
		
		String[] choice = {"spatial", "temporal"}; 
		JComboBox list = new JComboBox(choice);
		list.setSelectedIndex(0);
		list.addActionListener(vcl);
		ret.add(but); ret.add(list); 
		return ret; 
	}
	
	private JPanel createPullPanel(){
		JPanel ret = new JPanel(new FlowLayout()); 
		ret.setSize(200, 50); 
		ImageIcon icon = resize(createImageIcon("/edu/gatech/resource/pull.gif"), 30,22);
		JButton but = new JButton("Pull", icon); 
		but.addActionListener(vcl);  
		String[] choice = {"off", "bump", "channel", "mixed"};
		JComboBox list = new JComboBox(choice); 
		list.setSelectedIndex(0);
		list.addActionListener(pull_lis);
		ret.add(but); ret.add(list); 
		return ret; 
	}
	
	 protected ImageIcon createImageIcon(String path) {
	      java.net.URL imgURL = getClass().getResource(path);
	      if (imgURL != null) {
	            return new ImageIcon(imgURL);
	      } else {
	            System.err.println("Couldn't find file: " + path);
	            return null;
	      }
	 }
}
