package edu.gatech.interact;
import processing.core.PApplet;
import edu.gatech.model.*;

public enum PullMode {
	OFF, BUMP, CHANNEL, MIXED;
	
	public void show(Layout layout){
		for (Caplet c:Layout.relations){
			this.show(c, layout.view);
		}
	}
	
	public void update(Layout layout){
		for (Caplet c:Layout.relations){
			this.update(c);
		}
	}
	
	public void show(Caplet cap, PApplet pa){
		switch (this){
			case OFF: 
				return; 
			case BUMP: 
				cap.drawBump(pa);
				return; 
			case CHANNEL:
				cap.drawChannel(pa);
				return;   
			case MIXED:
				cap.drawMixed(pa); 
				return; 
		}
	}
	
	public void update(Caplet cap){
		switch(this) {
		    case OFF: 
		    	return; 
			case BUMP: 
				cap.computeBump();
				return; 
			case CHANNEL: 
				cap.computeChannel();
				return; 
			case MIXED: 
				cap.computeMixed(); 
				return; 
		}
	}
	
	public void print(){
		switch(this){
			case OFF:
				System.out.println("no pull");
				break; 
			case BUMP: 
				System.out.println("bump");
				break; 
			case CHANNEL:
				System.out.println("channel"); 
			    break; 
			case MIXED:
				System.out.println("mixed"); 
			break; 
		}
	}
}
