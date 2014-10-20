package edu.gatech.geo;

public class Vec {
	public float x = 0;
	public float y = 0;
	public Vec() { 
	};
	Vec(Vec V) {
		x = V.x;
		y = V.y;
	};

	Vec(float s, Vec V) {
		x = s * V.x;
		y = s * V.y;
	};
	Vec(float px, float py) {
		x = px;
		y = py;
	};

	public Vec(Pt P, Pt Q) {
		x = Q.x - P.x;
		y = Q.y - P.y;
	};
	Vec makeUnit() {
		float n = this.n();
		if (n == 0)
			return new Vec(0, 0);
		else
			return new Vec(x / n, y / n);
	}
	public void unit(){
		float n = this.n(); 
		x = x/n;
		y = y/n; 
	}
	public void reset(){
		x=0; y=0; 
	}

	// MODIFY
	void setTo(float px, float py) {
		x = px;
		y = py;
	};

	void setTo(Pt P, Pt Q) {
		x = Q.x - P.x;
		y = Q.y - P.y;
	};

	void setTo(Vec V) {
		x = V.x;
		y = V.y;
	};

	void scaleBy(float f) {
		x *= f;
		y *= f;
	};

	void back() {
		x = -x;
		y = -y;
	};


	void div(float f) {
		x /= f;
		y /= f;
	};

	void scaleBy(float u, float v) {
		x *= u;
		y *= v;
	};

	void add(Vec V) {
		x += V.x;
		y += V.y;
	};

	void add(float s, Vec V) {
		x += s * V.x;
		y += s * V.y;
	};
	
	public Vec left(){
		return new Vec(-y, x); 
	}
	public Vec right(){
		return new Vec(y, -x); 
	}

	void add(float u, float v) {
		x += u;
		y += v;
	};

	void turnLeft() {
		float w = x;
		x = -y;
		y = w;
	};

	float n() {
		return (float) Math.sqrt(x*x + y*y );
	}
	float n2(){
		return (x*x + y*y);
	}
	
	float dot(Vec v) {
		return x*v.x + y*v.y;
	}
	
	float cross(Vec V){
	    return Math.abs(x*V.y - y*V.x); 	
	}
	
	void print(String name){
		System.out.print(name + ": ("+x+", "+y+") ");
	}
	
	static float angle(Vec u, Vec v){
		return (u.x*v.y-u.y*v.x)/(u.n()*v.n()); 
	}
}