package edu.gatech.model;
import edu.gatech.geo.*;
import processing.core.*;

/**
 * @author tina (opmiss@gmail.com)
 */

public class Bump {
	public Disk A;
	public Disk B;
	Pt A1, A2, B1, B2; 
	Pt[] CA, CB; 
	float sa, sb; 
	float cor; 
	float targetArea; 

	public void adjust(){
		for (int i=0; i<500; i++){
			float currentArea = area(sa); 
			float da = targetArea-currentArea; 
			if (PApplet.abs(da)<1.0f) {System.out.println("converge!"); return; }; 
			sa+=0.5f/(area(sa+0.5f)-area(sa))*da; 
			sb = sa/A.R*B.R; 
		}
	}
	public Bump(Topic TA, Topic TB, float co){
		A = new Disk(TA.getX(), TA.getY(), TA.getR());
		B = new Disk(TB.getX(), TB.getY(), TB.getR());
		cor = co; 
		Vec tx = new Vec(A.O, B.O); tx.unit();
		Vec ty = tx.left();
		computeContacts(tx, ty);  
		sa = A.R*2; 
		sb = B.R*2; 
		targetArea = (A.area()+B.area())*(0.1f+cor); 
		adjust(); 
	}
	
	public void computeContacts(Vec tx, Vec ty){
		float a = PApplet.PI/6; 
		float angle = PApplet.PI/2-a ; 
		float cosa = PApplet.cos(angle); 
		float sina = PApplet.sin(angle); 
		A1 = A.O.makeTranslatedBy(A.R*cosa, ty).makeTranslatedBy(A.R*sina, tx) ;
		A2 = A.O.makeTranslatedBy(-A.R*cosa , ty).makeTranslatedBy(A.R*sina, tx);
		B1 = B.O.makeTranslatedBy(B.R*cosa, ty).makeTranslatedBy(-B.R*sina, tx) ;
		B2 = B.O.makeTranslatedBy(-B.R*cosa, ty).makeTranslatedBy(-B.R*sina, tx);  
	}
	
	public void setTo(Topic TA, Topic TB){
		A = new Disk(TA.getX(), TA.getY(), TA.getR());
		B = new Disk(TB.getX(), TB.getY(), TB.getR());
		Vec tx = new Vec(A.O, B.O); tx.unit();
		Vec ty = tx.left();
		computeContacts(tx, ty); 
	}
	
	
	public float area(float sa){
		float sb = sa/A.R*B.R; 
		CA = new Pt[101]; 
		CB = new Pt[101]; 
		Vec tx = new Vec(A.O, B.O); tx.unit();
		Pt TA1 = A1.s(sa, tx); Pt TA2 = A2.s(sa, tx);
		Pt TB1 = B1.s(-sb, tx); Pt TB2 = B2.s(-sb, tx); 
		int i=0; 
		for (float t =0; t<=1.0; t+=0.01f){
			CA[i++] = Pt.Bezier(A1, TA1, TA2, A2, t); 
		}
		i=0; 
		for (float t =0; t<=1.0; t+=0.01f){
			CB[i++] = Pt.Bezier(B1, TB1, TB2, B2, t);  
		}
		return Pt.area(CA)+Pt.area(CB); 
	}
	
	public Bump(float ax, float ay, float ar, float bx, float by, float br) {
		A = new Disk(ax, ay, ar);
		B = new Disk(bx, by, br);
	}
	
	public void update(){
		Vec tx = new Vec(A.O, B.O); tx.unit();
		Vec ty = tx.left();
		A1 = A.O.makeTranslatedBy(A.R, ty);
		A2 = A.O.makeTranslatedBy(-A.R, ty);
		B1 = B.O.makeTranslatedBy(B.R, ty);
		B2 = B.O.makeTranslatedBy(-B.R, ty);  
	}
	
	public void drawC(PApplet p){
		Vec tx = new Vec(A.O, B.O); tx.unit();
		Pt TA1 = A1.s(sa, tx); Pt TA2 = A2.s(sa, tx);
		Pt TB1 = B1.s(-sb, tx); Pt TB2 = B2.s(-sb, tx); 
		p.bezier(A1.x, A1.y, TA1.x, TA1.y , TA2.x, TA2.y, A2.x, A2.y);
		p.bezier(B1.x, B1.y, TB1.x, TB1.y , TB2.x, TB2.y, B2.x, B2.y);
	}
	
	public void drawD(PApplet pa){
		A.show(pa); B.show(pa);
	}
	
	public void show(PApplet pa){
		drawC(pa); 
		drawD(pa); 
	}

	public void setB(Pt M) {
		B.O.setTo(M);
		update();
	}
	
	public void setA(Pt M) {
		A.O.setTo(M);
		update();
	}
}