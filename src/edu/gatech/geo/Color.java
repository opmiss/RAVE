package edu.gatech.geo;
import processing.core.PApplet;

public class Color {
	public int r; 
	public int g; 
	public int b; 
	//public int a; 
	//primary colors
	//add lab interpolation
	public Color(){
		r=0; g=0; b=0; 
	}
	public Color(float[] rgb){
		r=(int) rgb[0]; g=(int) rgb[1]; b=(int) rgb[2]; 
	}
	
	public static Color Red = new Color(255, 0, 0); 
	public static Color Green = new Color(0, 255, 0); 
	public static Color Blue = new Color(0, 0, 255);
	public static Color Yellow = new Color(255, 255, 0); 
	public static Color Purple = new Color(128, 0, 128); 
	public static Color Orange = new Color(255, 127, 0);
	public static Color[] PrimaryHue = {Red, Yellow, Blue, Green, Purple, Orange}; 
	
	// more colors 
	public static Color lightGray = new Color(165, 161, 160); 
	public static Color lightBlue = new Color(183, 226, 240); 
	public static Color Ruby = new Color(209, 101, 135);
	public static Color Cyan = new Color(0, 255, 255); 
	public static Color lightGreen = new Color(120, 240, 100); 
	public static Color darkBlue = new Color(123, 166, 180); 
	public static Color lightPink= new Color(239, 173, 245); 
	public static Color Pink= new Color(239, 173, 245);
	public static Color White = new Color(255, 255, 255);
	public static Color Black = new Color(0, 0, 0); 
	public final static Color[] stripe = {Ruby, Yellow, Cyan, Green}; 
	public final static Color[] Gradient_White_to_Red = createGradient(Color.White, Color.Red, 100);
	public final static Color[] Gradient_White_to_Black = createGradient(Color.White, Color.Black, 100);
	
	public static Color[] Gradient(Color C, int num){
		return createGradient(Color.White, C, num); 
	}
	
	public Color getScaled(float s){
		return interpolate(Color.White, this, s); 
	}
	
	public void add(Color col){ //additive blending
		r= PApplet.min(r+col.r, 256); 
		g= PApplet.min(g+col.g, 256); 
		b= PApplet.min(b+col.b, 256); 
	}
	public void add(float s, Color col){
		r+=s*col.r; 
		g+=s*col.g; 
		b+=s*col.b; 
	}
	public static void fill(Color c, PApplet pa){
		pa.fill(c.r, c.g, c.b); 
	}
	public static void stroke(Color c, PApplet pa){
		pa.stroke(c.r, c.g, c.b); 
	}
	public void stroke(PApplet pa){
		pa.stroke(r, g, b); 
	}
	public void fill(PApplet pa){
		pa.fill(r, g, b); 
	}
	public void fill(PApplet pa, int a){
		pa.fill(r, g, b, a); 
	}
	
	Color(int r, int g, int b){
		this.r=r; this.g=g; this.b=b; 
	}
	
	Color(float r, float g, float b){
		this.r=(int) r; this.g=(int) g; this.b=(int) b; 
	}
	
	public int toPColor(PApplet P){
		return P.color(r, g, b); 
	}
	public static Color[] createGradient(final Color one, final Color two, final int numSteps)
	    {
	        int r1 = one.r, g1 = one.g, b1 = one.b;
	        int r2 = two.r, g2 = two.g, b2 = two.b; 
	        int newR = 0, newG = 0, newB = 0, newA = 0;
	        Color[] gradient = new Color[numSteps];
	        double iNorm;
	        for (int i = 0; i < numSteps; i++)
	        {
	            iNorm = i / (double)numSteps; //a normalized [0:1] variable
	            newR = (int) (r1 + iNorm * (r2 - r1));
	            newG = (int) (g1 + iNorm * (g2 - g1));
	            newB = (int) (b1 + iNorm * (b2 - b1));
	           // newA = (int) (a1 + iNorm * (a2 - a1));
	            gradient[i] = new Color(newR, newG, newB);
	        }
	        return gradient;
	    }
	public static Color interpolate(Color one, Color two, float s){
		   int newR = (int) (one.r + s*(two.r - one.r));
           int newG = (int) (one.g + s*(two.g - one.g));
           int newB = (int) (one.b + s*(two.b - one.b));
           return new Color(newR, newG, newB); 
	}
	public static Color s(Color c1, float s, Color c2){
		return new Color(c1.r+(int)((c2.r-c1.r)*s), c1.g+(int)((c2.g-c1.g)*s), c1.b+(int)((c2.b-c1.b)*s)); 
	}
	public static CIELab lab = new CIELab(); 
	public float[] toArray(){
		float[] ar = new float[3]; 
		ar[0] = r; ar[1] = g; ar[2] = b; 
		return ar; 
	}
	public float[] toLab(){
		return lab.fromRGB(this.toArray()); 
	}
	public static Color fromLab(float[] l){
		float[] nc = lab.toRGB(l); 
		return new Color(nc[0]*255, nc[1]*255, nc[2]*255); 
	}
	public static Color interpolate_lab(Color one, Color two, float s){
		float[] lab1 = lab.fromRGB(one.toArray()); 
		float[] lab2 = lab.fromRGB(two.toArray()); 
		float[] labnew = new float[3];
		labnew[0] = (int) (lab1[0] + s*(lab2[0] - lab1[0]));
        labnew[1] = (int) (lab1[1] + s*(lab2[1] - lab1[1]));
        labnew[2] = (int) (lab1[2] + s*(lab2[2] - lab1[2]));
        return new Color(lab.toRGB(labnew)); 
	}
	public void print(){
		System.out.println(r+", "+g+", "+b); 
	}
	public static void main(String[] args){
		Color red = new Color(255, 0, 0);
		float[] labred = red.toLab(); 
		System.out.println(labred[0]+", "+labred[1]+", "+labred[2]); 
		float[] ared = lab.toRGB(labred); 
		System.out.println(ared[0]+", "+ared[1]+", "+ared[2]); 
		Color nred = Color.fromLab(labred);
		System.out.println(nred.r+", "+nred.g+", "+nred.b); 
	}
}
