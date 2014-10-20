package edu.gatech.model;
import java.io.File;
import java.util.ArrayList;

import edu.gatech.util.Snapshot;
import edu.gatech.vis.Animation;
import processing.core.PApplet;

/**
 * @author tina (opmiss@gmail.com)
 */

public class Anim {
	ArrayList<Snapshot> Key = new ArrayList<Snapshot>();  //snapshots 
	ArrayList<Snapshot> Tween = new ArrayList<Snapshot>();
	Animation aview; 
	public int length = 150; 
	public boolean stop = true;
	int id=0; 
	int seg; 
	
	public Anim(Animation a){ 
		aview = a; 
	}
	
	public Anim setKey(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			String path, suffix; 
			seg = length/(files.length-3); 
			int t =0; 
			for(File f:files){
				//System.out.println(f.getAbsolutePath()); 
				path = f.getAbsolutePath();
		    	suffix = path.substring(path.lastIndexOf('.')+1); 
		    	if (suffix.equals("weight")){
		    		Snapshot s = new Snapshot(t);
		    		s.setWeight(f); Key.add(s); 
		    		t+=seg; 
		    	}
		    	else if (suffix.equals("docmeta")){
		    		Snapshot.setDocName(f); 
		    		//System.out.println(Snapshot.getDocNum()); 
		    	}
				if(suffix.equals("topicmeta")){
					Snapshot.setTopicName(f); 
					//System.out.println(Snapshot.getTopicNum()); 
				}	
			}
		}
		else{
			System.out.println("select directory");
			}
		System.out.println(Key.size()); 
		return this; 
	}
	
	public void fillTween(){
		for (int l =0; l<length; l++){
			Tween.add(Snapshot.lerp(Key.get(0), Key.get(1), Key.get(2), Key.get(3), l));
		} 
	}
	
	public Snapshot getCurrent(){
		return Tween.get(id); 
	}
	
	public void toggleStop(){
		stop =!stop; 
		if (!stop) aview.toggle(); 
		if (id==length-1) id=0; 
	}
	
	public void next(){ 
		//show 3 transitions and initial and last 
		if (stop) return; 
		id++; if (id>length-1) {stop = true; id = length-1;}
		aview.layout.switchSnapshot(Tween.get(id)); 
		if (id %seg==0 || id==length-1) {
			//aview.saveFrame("anim/pic-####.png"); 
			aview.toggle(); }
	}
	
}
