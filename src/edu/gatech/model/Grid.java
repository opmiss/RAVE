package edu.gatech.model;
import processing.core.PApplet;
import edu.gatech.vis.View;

public class Grid {
	static int n; //grid size 
	static float s=1; //cell width  
	//static int [][] X; //points to be packed 
	static boolean[][] O;
	//Layout layout; 
	
	public static void pack(Layout layout){
		s = Doc.r*2; 
		n = (int) ((float)View.width/s); 
		int nd = layout.snapshot.getDocNum(); 
		int [][] X = new int[nd][2]; //int i=0; 
		O = new boolean[n][n]; 
		for (int i = 0; i<n; i++){
			for (int j=0; j<n; j++){
				O[i][j] = true; 
			}
		}
		int find=0; 
		for (int i=0; i<nd; i++){
			Doc d = layout.docs.get(i); 
			int x = (int)(d.pos.x/s); 
			int y = (int)(d.pos.y/s); 
			if (O[x][y]) { 
				X[i][0] = x;  X[i][1] = y; O[x][y] = false; 
			}
			else {
				int[] p = findNext(x, y); 
				find+=p[2];
				if (p[0]==x && p[1]==y) break;
				X[i][0] = p[0]; 
				X[i][1] = p[1];  
				O[p[0]][p[1]] = false; 
			} 
			d.offset.x = X[i][0]*s - d.pos.x; 
			d.offset.y = X[i][1]*s - d.pos.y; 
		}
		find/=nd; 
		System.out.println("average # pixels visited: "+find); 
	}
	
	public static int[] findNext(int cx, int cy) {
		int[] r = new int[3];
		r[2]=0; 
		for (int k = 0; k < n; k++) {
			float r0 = k * 0.5f, r1 = r0 + 0.5f;
			for (int i = 0; i <= PApplet.floor(r1); i++) {
				float h0 = PApplet.sqrt(PApplet.sq(r0) - PApplet.sq(i)), h1 = PApplet.sqrt(PApplet.sq(r1) - PApplet.sq(i));
				for (int j = PApplet.floor(h0) + 1; j <= PApplet.floor(h1); j++) {
					r[2]++; 
					if (O[b(cx + i)][b(cy + j)]) {
						r[0] = cx + i;
						r[1] = cy + j;
						return r;
					};
					if (O[b(cx - j)][b(cy + i)]) {
						r[0] = cx - j;
						r[1] = cy + i;
						return r;
					};
					if (O[b(cx - i)][b(cy - j)]) {
						r[0] = cx - i;
						r[1] = cy - j;
						return r;
					};
					if (O[b(cx + j)][b(cy - i)]) {
						r[0] = cx + j;
						r[1] = cy - i;
						return r;
					};
				}
			}
		}
		System.out.println("can not find!");
		r[0] = cx; r[1] = cy; 
		return r; 
	}
	
	public static int b(int i) {
		if (i>n-1) return n-1;
		if (i<0) return 0;
		return i; 
	}
}
