package edu.gatech.model;
import processing.core.PApplet;
import edu.gatech.geo.Color;

public class Heat {
	public static Color[] hue; 
	Layout L;
	int ksize = 150; 
	int dsize = 100; 
	float[][] kernel;
	float[][] data; 
	int[][] cid; 
	int[] pixelcolor; 
	float sigma = 3000;
	PApplet P; 
	int wh; 
	public Heat(PApplet pa, Layout la){
		P=pa; L=la;
		kernel = new float[ksize+1][ksize+1]; 
		hue = Color.Gradient_White_to_Black; 
		for (int xx=0; xx<ksize+1; xx++) {	
    		for (int yy=0; yy<ksize+1; yy++) {
				kernel[xx][yy] = 
			    (float) ((1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-0.5/sigma*(xx)*(xx))) * (1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-0.5/sigma*yy*yy))); 
    		}
    	}
		wh = P.width*P.height; pixelcolor = new int[wh]; data = new float[dsize][dsize]; 
		fillMap(); 
	}
    public void fillMap() {
    	float largest = Float.MIN_VALUE;
		float smallest = Float.MAX_VALUE;
		//long tick1 = System.nanoTime(); 
    	for (int x=0; x<dsize; x++) {
    		for (int y=0; y<dsize; y++) {
    			float sum = 0.0f;
    			float xx = (float)x/dsize*P.width; 
    			float yy = (float)y/dsize*P.height; 
    			
    			for (Doc d:L.docs){
    				int Xindex, Yindex;
    				Xindex = Math.min(Math.max(0, Math.abs((int)(xx-d.getX()))), ksize);
    				Yindex = Math.min(Math.max(0, Math.abs((int)(yy-d.getY()))), ksize);
    				sum = sum + kernel[Xindex][Yindex];
    			}
    			
    			data[x][y] = sum; 
    			largest = Math.max(sum, largest);
    			smallest = Math.min(sum, smallest);
    		}
    	}
    //	long tick2 = System.nanoTime(); 
    	float range = largest - smallest;
    	cid = new int[dsize][dsize];    
        for (int x = 0; x < dsize; x++){
        	for (int y = 0; y < dsize; y++){
        		float norm = (data[x][y] - smallest)/range; 
        		int pos = (int) Math.floor(norm * (hue.length - 1));
        		cid[x][y] = pos; if (pos <0) { pos=0; }
        	}
        }
      // long tick3 = System.nanoTime(); 
     	int k=0;
    	for (int i = 0; i < P.height; i++) {
			for (int j = 0; j < P.width; j++) {
				int w = (int)((float)j/P.width*dsize);
				int h = (int)((float)i/P.height*dsize); 
				int cc = PApplet.min(PApplet.max(cid[w][h], 0), hue.length-1); 
				int c = hue[cc].toPColor(P); 
				pixelcolor[k] = c; 
				k++; 
			 }
		}
      // long tick4 = System.nanoTime(); 
      // System.out.println("compute temperature: "+(tick2-tick1)); 
      // System.out.println("compute color index: "+(tick3-tick2)); 
      // System.out.println("fill pixel colors: "+(tick4-tick3)); 
    }
    int nextJ(int j){
    	return PApplet.min(j+1, P.width-1); 
    }
    int prevJ(int j){
    	return PApplet.max(j-1, 0); 
    }
    int nextI(int i){
    	return PApplet.min(i+1, P.height-1); 
    }  
    int prevI(int i){
    	return PApplet.max(i-1, 0); 
    }
    public void smooth(){
    	int[] newpixels = new int[P.height*P.width]; 
    	for (int i=0; i<P.height; i++){
    		for (int j=0; j<P.width; j++){
    			int c0 = pixelcolor[j+i*P.width]; 
    			int c1 = pixelcolor[j+prevI(i)*P.width]; 
    			int c2 = pixelcolor[j+nextI(i)*P.width]; 
    			int c3 = pixelcolor[prevJ(j)+i*P.width]; 
    			int c4 = pixelcolor[nextJ(j)+i*P.width]; 
    			int b1 = P.lerpColor(c1, c2, 0.5f); 
    			int b2 = P.lerpColor(c3, c4, 0.5f); 
    			newpixels[j+i*P.width]=P.lerpColor(b1, b2, 0.5f); 
    		}
    	}
    	pixelcolor = newpixels; 
    }
    public void show(){
    	P.loadPixels(); 
    	for (int i=0; i<wh; i++)
    	P.pixels[i] = pixelcolor[i]; 
    	P.updatePixels();
    } 
}
