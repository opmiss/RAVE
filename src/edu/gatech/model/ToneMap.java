package edu.gatech.model;
import processing.core.PApplet;
import edu.gatech.geo.Color;
import edu.gatech.geo.Pt;
/**
 * @author tina (opmiss@gmail.com)
 */
public class ToneMap {
	Layout L;
	int ksize = 150, dsize = 100, hsize = 100, tsize; 
	float[][] kernel;
	float[][] heat; 
	Color[][] tone;  
	int[] pixelcolor; 
	float sigma = 3000;
	PApplet P; 
	int wh; 
	public ToneMap(PApplet pa, Layout la){
		P=pa; L=la;
		kernel = new float[ksize+1][ksize+1];  
		for (int xx=0; xx<ksize+1; xx++) {	
    		for (int yy=0; yy<ksize+1; yy++) {
				kernel[xx][yy] = 
			    (float) ((1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-0.5/sigma*(xx)*(xx))) * (1/Math.sqrt(2*Math.PI*sigma)*Math.exp(-0.5/sigma*yy*yy))); 
    		}
    	} 
		wh = P.width*P.height; pixelcolor = new int[wh]; 
		tsize = Layout.topics.size(); 
		heat = new float[dsize][dsize]; 
		tone = new Color[dsize][dsize]; 
		fillToneMap(); 
	}
	public void fillToneMap(){
		fillHeat(); fillTone(); fillPixelColors(); 
	}
	public void fillHeat(){
		float largest = Float.MIN_VALUE;
		float smallest = Float.MAX_VALUE;
		//fill heat values 
    	for (int x=0; x<dsize; x++) {
    		for (int y=0; y<dsize; y++) {
 				float sum = 0; 
    			float xx = (float)x/dsize*P.width; 
    			float yy = (float)y/dsize*P.height; 
    			for (Doc d:L.docs){
    				int Xindex, Yindex;
    				Xindex = Math.min(Math.max(0, Math.abs((int)(xx-d.getX()))), ksize);
    				Yindex = Math.min(Math.max(0, Math.abs((int)(yy-d.getY()))), ksize);
    				sum+=kernel[Xindex][Yindex]; 
    			}
    			heat[x][y]=sum; 
    		}
    	}
    	//find largest and smallest 
    	for (int x=0; x<dsize; x++){
    		for (int y=0; y<dsize; y++){
    			for (int t=0; t<tsize; t++){
    			largest = Math.max(heat[x][y], largest);
    			smallest = Math.min(heat[x][y], smallest);
    			}
    		}
    	}
    	float range = largest - smallest;
    	//normalize heat values 
        for (int x = 0; x < dsize; x++){
        	for (int y = 0; y < dsize; y++){
        		heat[x][y] = (heat[x][y]-smallest)/range; 
        	}
        }
	}
	
	int p(int k){
		return (k<1)?(k-1+tsize):(k-1); 
	}
	
	int n(int k){
		return (k>tsize-2)?(k+1-tsize):(k+1); 
	}
	
	public void fillTone() { //use mean value coordinates 
       int k=0;
       Pt[] T = new Pt[tsize]; 
       for (Topic t:Layout.topics){
    	   T[k++]=t.getP(); 
       }
       float[] w = new float[tsize]; 
       float wsum = 0; 
       // convert topic color to lab
       float[][] tlab = new float[tsize][3]; 
       for (int i=0; i<tsize; i++){
    	   tlab[i] = Layout.topics.get(i).getColor().toLab();  
       }
       float[][][] plab = new float[dsize][dsize][3];  
       //
       for (int i=0; i<dsize; i++){
    	   for (int j=0; j<dsize; j++){
    		   Pt pos = new Pt((float)i/dsize*P.width, (float)j/dsize*P.height); 
    		   wsum=0; 
   			   for (k=0; k<tsize; k++){
    		       w[k]=pos.MVC(T[p(k)], T[k], T[n(k)]);  
    		       wsum+=w[k]; 
   			   }
   			   for (k=0; k<tsize; k++){
   				   w[k]/=wsum; 
   			   }
   			   tone[i][j] = new Color(); 
   			   /*k=0; 
   			   for (Topic t:Layout.topics){
   				   tone[i][j].add(w[k++], t.getColor()); 
   			   }*/
   			   for (k=0; k<tsize; k++){
   				   for (int u=0; u<3; u++){
   					   plab[i][j][u] += w[k] * tlab[k][u];  
   				   }
   			   }
   			   tone[i][j] = Color.fromLab(plab[i][j]); 
   			   tone[i][j] = tone[i][j].getScaled(heat[i][j]); 
    	   }
       }
    }
	
	public void fillPixelColors(){
		int k=0;
    	for (int i = 0; i < P.height; i++) {
			for (int j = 0; j < P.width; j++) {
				int w = (int)((float)j/P.width*dsize);
				int h = (int)((float)i/P.height*dsize); 
				int c = tone[w][h].toPColor(P); 
				pixelcolor[k] = c; 
				k++; 
			 }
		}
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
    	for (int i=0; i<wh; i++) P.pixels[i] = pixelcolor[i]; 
    	P.updatePixels();
    } 
}
