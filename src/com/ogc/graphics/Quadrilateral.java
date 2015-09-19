package com.ogc.graphics;



public class Quadrilateral {
	Point[] points;
	int count;
	public Quadrilateral(){
		count = 0;
		points = new Point[4];
	}
	public Point[] getPoints() {
		return  points;
	}
	public void addVertex(Point p){
		
		points[count] = p;
		count++;
	}

}
