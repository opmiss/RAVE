package edu.gatech.interact;
import edu.gatech.model.Grid;
import edu.gatech.model.Layout;


public enum PackMode {
	OFF, REPEL, RASTER, MIXED;
	
	public void pack(Layout layout){
		switch (this){
		case OFF: 
			layout.packoff();
			return; 
		case REPEL:
			layout.packon();
			return; 
		case RASTER:  
		case MIXED:
			Grid.pack(layout);  
			return; 
		}
	}
	
	public void update(Layout layout){
		long start, end; 
		switch(this) {
			case OFF: 
				return; 
			case REPEL:
			case MIXED: 
				//start = System.currentTimeMillis(); 
				layout.pack(50);
				//end = System.currentTimeMillis(); 
				//System.out.println("repel takes: "+(end-start)+" ms"); 
				return; 
			case RASTER: 
				//start = System.currentTimeMillis(); 
				Grid.pack(layout);
				//end = System.currentTimeMillis(); 
				//System.out.println("raster takes: "+(end-start)+" ms"); 
				return; 
		}
	}
	
}
