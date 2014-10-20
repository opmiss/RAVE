package edu.gatech.model;
import java.util.ArrayList;
import java.util.Vector;
import processing.core.PApplet;
import edu.gatech.geo.*;
import edu.gatech.util.Snapshot;
import edu.gatech.vis.View;
/**
 * @author tina (opmiss@gmail.com)
 */
public class Layout {
	public static boolean relation = false; 
	public static boolean paint = false; 
	public static boolean wave = false; 
	Snapshot snapshot; 
	public View view;  
	public ArrayList<Doc> docs; 
	Grid grid; 
	
	public static ArrayList<Topic> topics;  
	public static ArrayList<Caplet> relations; 
	
	private Layout(){
		docs = new ArrayList<Doc>(); 
		topics = new ArrayList<Topic>(); 
		relations= new ArrayList<Caplet>();
	}
	
	public Layout(View tv){
		this(); view = tv;  
	}
	
	public float getMouseX(){
		return view.mouseX; 
	}
	
	public float getMouseY(){
		return view.mouseY; 
	}
	
	public Layout initLayout(Snapshot ss){
		snapshot=ss;   
		this.initTopics(120).initDocs(); return this; 
	}
	
	private Layout initTopics(float oft){
		float da = PApplet.TWO_PI/Snapshot.getTopicNum(); 
		Pt O = View.getCenter(); float r = View.getRadius()-oft;
		Pt[] T = new Pt[Snapshot.getTopicNum()]; float sa=0; 
		for (int i=0; i<Snapshot.getTopicNum(); i++){
			T[i] = O.s(r*PApplet.cos(sa), r*PApplet.sin(sa)); sa+=da; 
			Topic t = (new Topic(T[i])).setLayout(this).setLabel(Snapshot.getTopicName(i)); 
			topics.add(t); 
		}
		Doc.setTopics(topics); Doc.setLayout(this); 
		return this; 
	}
	
	private Layout initDocs(){
		for (int i=0; i<Snapshot.getDocNum(); i++){
			Doc d = (new Doc()).setWeight(snapshot.getWeight(i)).
					setLabel(Snapshot.getDocName(i));
			docs.add(d); 
		}
		return this;
		//if (pack) Grid.pack(this); return this; 
		//if (pack) pack(50); return this; 
	}
	
	private Layout setTopicRadius(){
		int i=0;  snapshot.countMembers(); 
		for (Topic t:topics){
			t.setRadius(Topic.r+snapshot.getMemberNum(i)*0.5f ); 
			i++; 
		}
		return this; 
	}
	
	public Layout initRelations(){
		relations = new ArrayList<Caplet>(); 
		snapshot.fillCorrelation(); 
		for (int i=0; i<topics.size()-1; i++){
			for (int j=i+1; j<topics.size(); j++){
				relations.add(new Caplet(topics.get(i), topics.get(j), 
						snapshot.getCorrelation(i, j))); 
			}
		} 
		return this; 
	}
	
	public void updateRelations(){
		int i=0, j=1; 
		for (Caplet c:relations){
			c.setTo(topics.get(i), topics.get(j)); 
			j++; if (j==topics.size()){i++; j=i+1; }
		}
	}
	
	public void profile(){
		int doc_num = 500; 
		while (doc_num<docs.size()){
			snapshot.setDocNum(doc_num);
			System.out.println("doc num: "+doc_num); 
			//long t1 = System.currentTimeMillis(); 
			//this.pack(); 
			//long t2 = System.currentTimeMillis(); 
			Grid.pack(this); 
			//long t3 = System.currentTimeMillis(); 
			//System.out.println("repel takes: "+ (t2-t1) +" ms");
			//System.out.println("raster takes: "+ (t3-t2) +" ms");
			doc_num += 500; 
		}
	}
	
	public void switchSnapshot(Snapshot ss){
		snapshot=ss; int i=0; 
		for (Doc d:docs) d.setWeight(snapshot.getWeight(i++)); 
		//setTopicRadius(); 
		//if (pack) pack(15); 
		if (relation) initRelations();
	}
	
	public Topic pickTopics(Pt m){
		for (Topic t:topics){
			if (t.isIn(m)) return t; 
		}
		return null; 
	}
	
	/*-------------------------packing------------------------------------*/ 
	
	Vector<int[]> pairs = new Vector<int[]>();   
	
	private void detect(){ //detect overlapping pairs 
		pairs.clear();
		for (int i=0; i<Snapshot.getDocNum()-1; i++){
			for (int j=i+1; j<Snapshot.getDocNum(); j++){
				float dx = docs.get(i).getX() - docs.get(j).getX();
				float dy = docs.get(i).getY() - docs.get(j).getY(); 
				float dx2 = dx*dx; 
				float dy2 = dy*dy; 
				float d2 = dx2+dy2; 
				if (d2 < Doc.dr22){
					int[] p = new int[2];
					p[0] = i; p[1] = j; 
					pairs.add(p); 
				}
			}
		} 
	}
	
	private void repel(){
		for (int[] p:pairs){
			float dx = docs.get(p[0]).getX() - docs.get(p[1]).getX();
			float dy = docs.get(p[0]).getY() - docs.get(p[1]).getY(); 
			float dx2 = dx*dx; 
			float dy2 = dy*dy; 
			float d2 = dx2+dy2; 
			float d = (float) Math.sqrt(d2); 
			float f = (Doc.dr2-d)/Doc.dr2; 
			float fx = f*dx; float fy = f*dy; 
			if (d==0) {
				dx = (float)(Math.random()-0.5); dx2= dx*dx; 
				dy = (float)(Math.random()-0.5); dy2 = dy*dy;
				fx = (float)(Math.sqrt(dx2/(dx2+dy2))*Doc.dr2); 
				if (dx<0) fx =-fx; 
				fy = (float)(Math.sqrt(dy2/(dx2+dy2))*Doc.dr2); 
				if (dy<0) fy =-fy; 
			}
			docs.get(p[0]).addO(fx, fy); 
			docs.get(p[1]).subO(fx, fy); 
		}
	}
	
	private void sink(){
		for (int i=0; i<Snapshot.getDocNum(); i++) docs.get(i).sink(); 
	}
	
	/* ------------------------------------------------------------------------*/ 
	public void packon(){
		pack(30); this.setTopicRadius();
	}
	
	public void packoff(){
		for (Doc d:docs){ d.offset.reset(); for (Topic t:topics) t.resetRadius(); }
	}
	
	public void pack(int num){
		for (int i=0; i<num; i++) pack(); 
	}
	
	private void pack(){
		detect(); repel(); sink(); 
	}
	/* ------------------------------------------------------------------------*/ 
	public void update(){
		for (Doc d:docs){
			d.update(topics); 
		}
		if (relation) updateRelations(); 
	}
	
	/* ------------------------------------------------------------------------*/
	public void toggleWave(){
		wave=!wave; 
		if (!wave) {
			//view.saveFrame("wave/pic-####.png"); 
			time=0; wid =0; 
		}
	}
	
	public void togglePull(){
		relation = !relation;
		if (relation) this.initRelations(); 
	}
	
	private int time=0; 
	private int total_time = 200; 
	private float amp = 50;  //amplitude 
	private float wmg = PApplet.TWO_PI/total_time*10; //frequency
	private int next(int i){return (i<topics.size()-1)?(i+1):0;}
	int wid=0; 
	private void wave(){
		int twid = 0; 
		for (int i=0; i<topics.size(); i++){
			if (time < total_time/topics.size()*(i+1)){
				twid = i; 
				if (i%2==0) {
					topics.get(i).waveX(amp, wmg, time);
					topics.get(next(i)).waveY(amp, wmg, time); 
				}
				else {
					topics.get(i).waveY(amp, wmg, time);
					topics.get(next(i)).waveX(amp, wmg, time); 
				}
				break; 
			}
		}
		if (twid>wid) {wid = twid; 
		//view.saveFrame("wave/pic-####.png"); 
		view.background(255); }
		update(); 
	}
	
	public void shake(){
		wave(); time++; 
		if (time==total_time) { toggleWave(); for (Topic t:topics) t.resetO(); update();  }
	}
	
	/*------------------------------------------------*/
	
	private int cid=0;  
	
	public void highlight(String filtertxt){
		ArrayList<Integer> id = snapshot.filter(filtertxt);
		System.out.println("find "+id.size()+" matches"); 
		for (Integer i:id) docs.get(i).setColor(Color.PrimaryHue[cid]); 
		cid++; if (cid==6) cid=0; 
	}
	
	public void paint(int i){
		for (Topic t:topics) t.setColor(Color.White); 
		topics.get(i).setColor(Color.PrimaryHue[i]); 
		for (Doc d:docs) d.setColor(); 
	}
	
	public void paint(int i, int j){
		for (Topic t:topics) t.setColor(Color.White); 
		topics.get(i).setColor(Color.PrimaryHue[i]); 
		topics.get(j).setColor(Color.PrimaryHue[j]); 
		System.out.println("topic colors"); 
		for (Topic t:topics) t.color.print(); 
		for (Doc d:docs) d.setColor(); 
	}
	
	public void paintAll(){
		int i=0; 
		for (Topic t:topics) {t.setColor(Color.PrimaryHue[i++]);}
		for (Doc d:docs) d.setColor(); 
	}
	
	public void paintNone(){
		for (Topic t:topics) t.setColor(Color.White); 
		for (Doc d:docs) d.resetColor(); 
	}
	
	public void show(PApplet pa){
		pa.strokeWeight(1); 
		pa.noStroke(); 
		showTopics(pa);  
		showDocs(pa); 
	}
	
	public void showGray(PApplet pa){
		pa.stroke(150);
		pa.strokeWeight(2);
		pa.noFill(); 
		for (Topic t:topics){
			t.showCircle(pa); 
		}
		for (Doc d:docs){
			d.showGray(pa); 
		}
	}

	public void showTopics(PApplet pa){
		for (Topic t:topics){
			t.show(pa); 
		}
	}
	public void showTopicCircle(PApplet pa){
		//pa.stroke(180);
		pa.strokeWeight(2); 
		for (Topic t:topics){
			t.showCircle(pa); 
		}
	}

	
	public void showDocs(PApplet pa){
		//pa.noStroke(); 
		pa.stroke(0);
		for (Doc d:docs){
			d.show(pa); 
		}
		if (wave) shake(); 
	}
	
	public void showSmallDocs(PApplet pa){ //used in shakeview
		pa.noStroke(); 
		for (Doc d:docs){
			d.showSmall(pa); 
		}
		if (wave) shake();
	}
	
	public void showMediumDocs(PApplet pa){ //used in shakeview
		pa.noStroke(); 
		for (Doc d:docs){
			d.showMedium(pa); 
		}
		if (wave) shake();
	}
	
	public void showTransDocs(PApplet pa){
		for (Doc d:docs){
			d.showTransparent(pa); 
		}
	}
	
	public void showTopicLabel(PApplet pa) {
		for (Topic t:topics) 
			t.showLabel(pa); 
	}
	
	public void showBlur(PApplet pa){
		pa.noStroke(); 
		for (Doc d:docs){
			d.showTransparent(pa); 
		}
		if (wave) shake(); 
	}
	
}
