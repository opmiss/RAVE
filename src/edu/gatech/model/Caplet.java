package edu.gatech.model;
import edu.gatech.geo.*;
import processing.core.*;

/**
 * @author tina (opmiss@gmail.com)
 */
public class Caplet {
	public Disk A;
	public Disk B;
	Pt Q1, Q2, Q3, Q4; //circumscribed tangent points 
	Pt S1, S2, S3, S4; //cut tangent points 
	public float d, ar, br; 
	Pt O; 
	boolean show_curve = false; 
	Pt R1, R2, R3, R4; //contact point  
	Pt T1, T2, T3, T4; 
	Pt MA, MB, OA, OB; 
	Vec tx, ty; 
	float ratio; 
	float cor; 
	float tar_area; 
	float cur_area;
	
	public void setTo(Topic TA, Topic TB){
		A = new Disk(TA.getX(), TA.getY(), TA.getR());
		B = new Disk(TB.getX(), TB.getY(), TB.getR());
		compute();
	}
	
	/*public void setTargetArea(){
		tar_area = (ar*ar+br*br)*PApplet.PI; 
	}
	
	float theta, beta; 
	
	public void setCurrentArea(){
		if (d>ar+br) {cur_area=tar_area; return; }
		float maxr = PApplet.max(ar, br), minr = PApplet.min(ar, br); 
		if (d < maxr-minr) {cur_area=PApplet.PI*maxr*maxr; return;}
		float a2 = ar*ar, d2 = d*d, b2 = br*br; 
		float cos_theta = (a2+d2-b2)/ar/d/2.0f; 
		float cos_beta = (b2+d2-a2)/br/d/2.0f; 
		theta = PApplet.acos(cos_theta); 
		beta = PApplet.acos(cos_beta); 
		float sin_theta = PApplet.sin(theta); 
		float sin_beta = PApplet.sin(beta); 
		cur_area = a2*(PApplet.PI-theta+sin_theta*cos_theta)+b2*(PApplet.PI-beta+sin_beta*cos_beta); 
	}
	
	public void adjustArea(){
		setCurrentArea(); 
		float da = tar_area - cur_area; 
		da = da/(PApplet.TWO_PI+PApplet.TWO_PI-beta*2-theta*2);
		ar+=da/ar; br+=da/br; 
		B.R = br; A.R = ar; 
	}*/
	
	public Caplet(Topic TA, Topic TB, float co){
		A = new Disk(TA.getX(), TA.getY(), TA.getR());
		B = new Disk(TB.getX(), TB.getY(), TB.getR());
		cor = co; 
		compute(); //setTargetArea();
	}
	
	protected Caplet(float ax, float ay, float ar, float bx, float by, float br) {
		A = new Disk(ax, ay, ar);
		B = new Disk(bx, by, br);
	    compute(); //setTargetArea();
	}
	
	private void compute(){
		d = A.O.disTo(B.O);
		ar = A.R; br = B.R; 
		float x = (ar - br) / d;
		float y = PApplet.sqrt((d - ar + br) * (d + ar - br)) / d;
		tx = new Vec(A.O, B.O); tx.unit();
		ty = tx.left();
		Q1 = (A.O.makeTranslatedBy(x*ar, tx)).makeTranslatedBy(y*ar, ty);
		Q2 = (A.O.makeTranslatedBy(x*ar, tx)).makeTranslatedBy(-y*ar, ty);
		Q3 = (B.O.makeTranslatedBy(x*br, tx)).makeTranslatedBy(y*br,ty);
		Q4 = (B.O.makeTranslatedBy(x*br, tx)).makeTranslatedBy(-y*br, ty);
		x = (ar + br)/d; 
		y = PApplet.sqrt((d-ar-br)*(d+ar+br))/d;
		S1 = (A.O.makeTranslatedBy(x*ar, tx)).makeTranslatedBy(y*ar, ty);
		S2 = (A.O.makeTranslatedBy(x*ar, tx)).makeTranslatedBy(-y*ar, ty);
		S3 = (B.O.makeTranslatedBy(-x*br, tx)).makeTranslatedBy(y*br,ty);
		S4 = (B.O.makeTranslatedBy(-x*br, tx)).makeTranslatedBy(-y*br, ty);
		O = A.O.makeTranslatedBy(d*ar/(ar+br), tx);
		MA = A.O.makeTranslatedBy(ar, tx); 
		MB = B.O.makeTranslatedBy(-br, tx);  
	}
	
	float cut_ratio = 2.5f; 
	
	public void computeBump(){
		compute(); 
		if (d/(ar+br) < 1) return; 
		else {
			float s=1/4; float t = cor*8+1; 
			R1 = Pt.circleInterpolate(S1, MA, A.O, s, ar);
			T1 = R1.s(t, (new Vec(A.O,R1)).right()); 
			R2 = Pt.circleInterpolate(S2, MA, A.O, s, ar);
			T2 = R2.s(t, (new Vec(A.O,R2)).left()); 
			R3 = Pt.circleInterpolate(S3, MB, B.O, s, br); 
			T3 = R3.s(t, (new Vec(B.O,R3)).left()); 
			R4 = Pt.circleInterpolate(S4, MB, B.O, s, br); 
			T4 = R4.s(t, (new Vec(B.O,R4)).right());
		}
	}
	
	public void computeChannel(){
		compute(); 
		if (d/(ar+br) < 1) return;
		float s1 = 1-cor*3;  //System.out.println(s1); 
		float s2 = cor*3; 
		R1 = Pt.circleInterpolate(Q1, MA, A.O, s1, ar);
		T1 = R1.s(s2, (new Vec(A.O,R1)).right()); 
		R2 = Pt.circleInterpolate(Q2, MA, A.O, s1, ar);
		T2 = R2.s(s2, (new Vec(A.O,R2)).left()); 
		R3 = Pt.circleInterpolate(Q3, MB, B.O, s1, br); 
		T3 = R3.s(s2, (new Vec(B.O,R3)).left()); 
		R4 = Pt.circleInterpolate(Q4, MB, B.O, s1, br); 
		T4 = R4.s(s2, (new Vec(B.O,R4)).right()); 
	}
	
	public void computeMixed(){
		if (d<cut_dis) computeChannel(); 
		else computeBump(); 
	}
	
	int cut_dis = 360; 
	
	public void drawBump(PApplet p){
		if (d/(ar+br) < 1) return; 
		p.fill(180, 150);
		p.stroke(180);
		p.bezier(R1.x, R1.y, T1.x, T1.y, T2.x, T2.y, R2.x, R2.y); 
		p.bezier(R3.x, R3.y, T3.x, T3.y, T4.x, T4.y, R4.x, R4.y); 
		return ; 
	}
	
	public void drawChannel(PApplet pa){
		if (d/(ar+br) < 1) return; 
		pa.fill(180, 150);
		pa.stroke(180);
		Pt M1, M2, M3, B1, B2; 
		pa.beginShape(); 
		for (float t =0; t<=1.05; t+=0.05f){
			M1 = R1.s(t, T1); M2 = T1.s(t, T3); M3 = T3.s(t, R3); 
			B1=M1.s(t, M2); B2= M2.s(t, M3); 
			B1.s(t, B2).vert(pa); 
		}
		for (float t =0; t<=1.05; t+=0.05f){
			M1 = R4.s(t, T4); M2 = T4.s(t, T2); M3 = T2.s(t, R2); 
			B1=M1.s(t, M2); B2= M2.s(t, M3); 
			B1.s(t, B2).vert(pa); 
		}
		pa.endShape(PApplet.CLOSE); 
	}
	
	public void drawMixed(PApplet pa){
		if (d < cut_dis) drawChannel(pa); 
		else drawBump(pa); 
	}
	
	public void showMarkers(PApplet p){
		Color.darkBlue.fill(p); 
		Q1.showCircle(5, p); 
		Q2.showCircle(5, p); 
		Q3.showCircle(5, p); 
		Q4.showCircle(5, p); 
		Color.Green.fill(p); 
		S1.showCircle(5, p); 
		S2.showCircle(5, p); 
		S3.showCircle(5, p); 
		S4.showCircle(5, p); 
		if (ratio>2){
			Color.lightPink.fill(p); 
			OA.showCircle(5, p); 
			OB.showCircle(5, p); 
			Color.lightGray.fill(p); 
			MA.showCircle(5, p); 
			MB.showCircle(5, p); 
		}
	}
	public void printRatio(){
		System.out.println("ratio: "+ d/(ar+br)); 
	}
	public void setB(Pt M) {
		B.O.setTo(M);
		compute();
	}
	public void setA(Pt M) {
		A.O.setTo(M);
		compute();
	}
}