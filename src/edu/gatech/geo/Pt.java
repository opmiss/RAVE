package edu.gatech.geo;
import java.util.ArrayList;
import processing.core.*;

public class Pt {
	public float x = 0, y = 0;
		public Pt() {
		}
		public Pt(float px, float py) {
			x = px;
			y = py;
		}
		public Pt(Pt P) {
			x = P.x;
			y = P.y;
		}
		public static Pt mouse(PApplet par){
			return new Pt(par.mouseX, par.mouseY); 
		}
		Pt(Pt P, Vec V) {
			x = P.x + V.x;
			y = P.y + V.y;
		}
		public Pt(Pt P, float s, Vec V) {
			x = P.x + s * V.x;
			y = P.y + s * V.y;
		}
		public Pt(Pt A, float s, Pt B) {
			x = A.x + s * (B.x - A.x);
			y = A.y + s * (B.y - A.y);
		}
		Pt self(){
			return new Pt(x, y); 
		}
		public static Pt circleInterpolate(Pt P, Pt Q, Pt O, float s, float r){
			Pt R = P.s(s, Q); 
			Vec or = new Vec(O, R); 
			or.unit(); 
			return O.s(r, or); 
		}
		
		
		// MODIFY
		public void setTo(float px, float py) {
			x = px;
			y = py;
		}
		public void setTo(Pt P) {
			x = P.x;
			y = P.y;
		}
		void moveWithMouse(Pt m) {
			this.setTo(m); 
		}
		void scaleBy(float f) {
			x *= f;
			y *= f;
		}
		void scaleBy(float u, float v) {
			x *= u;
			y *= v;
		}
		public void translateBy(Vec V) {
			x += V.x;
			y += V.y;
		}
		void translateBy(float u, float v) {
			x += u;
			y += v;
		}
		void translateTowards(float s, Pt P) {
			x += s * (P.x - x);
			y += s * (P.y - y);
		}
		void translateBy(float s, Vec V) {
			x += s * V.x;
			y += s * V.y;
		}
		void addScaledPt(float s, Pt P) {
			x += s * P.x;
			y += s * P.y;
		} 
		static Pt cubicBezier(Pt A, Pt B, Pt C, Pt D, float t) {
			return (s(s(s(A, t, B), t, s(B, t, C)), t, s(s(B, t, C), t, s(C, t, D))));
		}
		static Pt s(Pt A, float s, Pt B) { //interpolation
			return new Pt(A.x + s * (B.x - A.x), A.y + s * (B.y - A.y));
		}
		static Pt s(Pt A, Pt B){
			return new Pt((A.x + B.x)/2, (A.y +B.y)/2); 
		}
		public Pt s(float s, Pt B) {
			return new Pt(x + s * (B.x - x), y + s * (B.y - y));
		}
		public Pt s(Vec V){
			return new Pt(x + V.x, y + V.y);
		}
		public Pt s(float dx, float dy){
			return new Pt(x+dx, y+dy); 
		}
		public Pt s(float s, Vec V) {
			if (s == 0) return new Pt(this); 
			return new Pt(x + s * V.x, y + s * V.y);
		}
		public float disTo(Pt P) {
			return (new Vec(this, P).n());
		}
		Pt circumCenter (Pt A, Pt B, Pt C) {    // computes the center of a circumscirbing circle to triangle (A,B,C)
			  Vec AB = new Vec(A,B);  
			  float ab2 = AB.n2();
			  Vec AC = new Vec(A,C); 
			  AC.turnLeft();  
			  float ac2 = AC.n2();
			  float d = 2*AB.dot(AC);
			  AB.turnLeft();
			  AB.scaleBy(-ac2); 
			  AC.scaleBy(ab2);
			  AB.add(AC);
			  AB.scaleBy((float)1./d);
			  Pt X =  new Pt(A.x, A.y);
			  X.translateBy(AB);
			  return(X);
		}
		static boolean isIntersect(Pt a1, Pt a2, Pt b1, Pt b2 ){
			return ((isLeftTurn(a1, b1, a2)!= isLeftTurn(a1, b2, a2))&& (isLeftTurn(b1, a1, b2)!=isLeftTurn(b1, a2, b2))); 
		}
		static boolean isLeftTurn(Pt a, Pt b, Pt c){
			Vec ab = new Vec(a, b); 
			Vec bc = new Vec(b, c); 
			ab.turnLeft(); 
			float d = ab.dot(bc); 
			if (d >= 0) return true; 
			else return false; 
		}
		static float radius(Pt A, Pt B, Pt C) { //a slightly modified version of circum circle radius
			  Vec AB = new Vec(A, B); 
			  Vec AC = new Vec(A, C); 
			  AC.unit(); 
	          float v = A.disTo(C)/2; 
	          float d = AB.dot(AC);
	          Pt D = new Pt(A); 
	          D.translateBy(d, AC); 
	          float h = (new Vec(B, D)).n(); 
	          if (h <0.00001) h = (float)0.00001;
	          float r = v*v/2/h;  
	          if (r > 100000) r = 100001; 
	          return r; 
		}
		static float area(Pt A, Pt B, Pt C){
			Vec ab = new Vec(A, B);
			Vec ac = new Vec(A, C);
			return Math.abs(ab.cross(ac)/2); 
		}
	    static float area(Pt A, Pt B, Pt C, Pt D){
			return (area(A, B, C)+area(A, C, D)); 
		}
	    static float area(Pt A, Pt B){ //twice area of the vertical quad bound by AB
	    	return (A.y+B.y)*(B.x - A.x); 
	    }
	    public static Pt interpolate(double[] W, Pt[] P){
	    	int n=W.length; float x=0, y=0; 
	    	for (int i=0; i<n; i++){
	    		x+=W[i]*P[i].x; 
	    		y+=W[i]*P[i].y;
	    	}
	    	return new Pt(x, y); 
	    }
	    Pt laplacian(Pt prev, Pt next){
	    	return s(self(), s(prev, next)); 
	    }
	    Pt laplacian(float l, Pt prev, Pt next){
	    	return s(self(), l, s(prev, next)); 
	    }
		public String toString(){
			return Float.toString(this.x)+","+Float.toString(this.y); 
		}
		public void vert(PApplet pa) {
			pa.vertex(x, y);
		}
		public void showCircle(float d, PApplet p){
			p.ellipse(x, y, d, d); 
		}
		public void showSquare(float d, PApplet p){
			float hd = d/2; 
			p.rect(x-hd, y-hd, d, d, 4); 
		}
		void print(String name){
			System.out.print(name+this.toString()); 
		}
		public Pt makeTranslatedBy(float f, Vec tx) {
			return new Pt(x+f*tx.x, y+f*tx.y);
		}
		public static void showQuad(Pt q1, Pt q2, Pt q3, Pt q4, PApplet pa) {
			pa.beginShape(); 
			q1.vert(pa); 
			q2.vert(pa); 
			q3.vert(pa); 
			q4.vert(pa); 
			pa.endShape(PApplet.CLOSE); 
		}
		// Linear
		public static ArrayList<Float> L(ArrayList<Float> A, ArrayList<Float> B, float t) {
			ArrayList<Float> R = new ArrayList<Float>(); 
			for (int i=0; i<A.size(); i++){
				R.add(A.get(i)+t*(B.get(i)-A.get(i))); 
			}
			return R;
		}
		// Interpolation non-uniform (Neville's algorithm)
		/*public static double[] I(float a, double[] A, float b, double[] B, float t) {
			return L(A, B, (t - a) / (b - a));
		} // P(a)=A, P(b)=B*/

	/*	public static double[] I(float a, double[] A, float b, double[] B, float c, double[] C, float t) {
			double[] P = I(a, A, b, B, t);
			double[] Q = I(b, B, c, C, t);
			return I(a, P, c, Q, t);
		} // P(a)=A, P(b)=B, P(c)=C*/

		/*public static double[] I(float a, double[] A, float b, double[] B, float c, double[] C, float d, double[] D, float t) {
			double[] P = I(a, A, b, B, c, C, t);
			double[] Q = I(b, B, c, C, d, D, t);
			return I(a, P, d, Q, t);
		} // P(a)=A, P(b)=B, P(c)=C, P(d)=D*/
		
		/*public static double[] LinearInterpolate(float a, double[] A, float b, double[] B, 
				float c, double[] C, float d, double[] D, float t){
			if (t<a) return A; 
			else if (t<b) return L(A, B, (t-a)/(b-a)); 
			else if (t<c) return L(B, C, (t-b)/(c-b));
			else if (t<d) return L(C, D, (t-c)/(d-c));
			else return D; 
		}*/
		
		public static ArrayList<Float> LinearInterpolate(float a, ArrayList<Float> A, 
				float b, ArrayList<Float> B, float c, ArrayList<Float> C, 
				float d, ArrayList<Float> D, float t){
			if (t<a) return A; 
			else if (t<b) return L(A, B, (t-a)/(b-a)); 
			else if (t<c) return L(B, C, (t-b)/(c-b));
			else if (t<d) return L(C, D, (t-c)/(d-c));
			else return D; 
		}
		
		public static Pt L(Pt A, Pt B, float t) {
			return new Pt(A.x + t*(B.x - A.x), A.y + t*(B.y - A.y));
		}
		// Interpolation non-uniform (Neville's algorithm)
		public static Pt I(float a, Pt A, float b, Pt B, float t) {
			return L(A, B, (t - a)/(b - a));
		} // P(a)=A, P(b)=B

		public static Pt I(float a, Pt A, float b, Pt B, float c, Pt C, float t) {
			Pt P = I(a, A, b, B, t);
			Pt Q = I(b, B, c, C, t);
			return I(a, P, c, Q, t);
		} // P(a)=A, P(b)=B, P(c)=C

		public static Pt I(float a, Pt A, float b, Pt B, float c, Pt C, float d, Pt D, float t) {
			Pt P = I(a, A, b, B, c, C, t);
			Pt Q = I(b, B, c, C, d, D, t);
			return I(a, P, d, Q, t);
		} // P(a)=A, P(b)=B, P(c)=C, P(d)=D
		
		public static Pt Bezier(Pt A, Pt B, Pt C, Pt D, float s){
			Pt P = A.s(s, B); 
			Pt Q = B.s(s, C);
			Pt R = C.s(s, D); 
			return (P.s(s, Q)).s(s, Q.s(s, R)); 
		}
	
		public float MVC(Pt prev, Pt cur, Pt next){
			float area1 = Pt.area(this, prev, cur);
			float area2 = Pt.area(this, cur, next);
			float lp = this.disTo(prev); 
			float lc = this.disTo(cur);
			float lc2 = lc*lc; 
			float ln = this.disTo(next); 
			float pc = prev.disTo(cur); 
			float cn = cur.disTo(next); 
			float sin1 = 2*area1/lp/lc; 
			float sin2 = 2*area2/lc/ln; 
			float cos1 = (lp*lp+lc2-pc*pc)/2/lp/lc;
			float cos2 = (lc2+ln*ln-cn*cn)/2/lc/ln; 
			float tan1 = sin1/(1+cos1); 
			float tan2 = sin2/(1+cos2); 
			return (tan1+tan2)/this.disTo(cur); 
		}
		public static float area(Pt[] C){
			int np = C.length;
			float A =0;
			for (int i=0; i<np; i++){
				float xi = C[i].x; 
				float yi = C[i].y; 
				float xni, yni; 
				if (i==np-1){
					xni = C[0].x;
					yni = C[0].y; 
				}
				else{
					xni=C[i+1].x; 
					yni=C[i+1].y; 
				}
				A+=(xi*yni-xni*yi); 	
			}
			return PApplet.abs(A/2); 
		}
}
