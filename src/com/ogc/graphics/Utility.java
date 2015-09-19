package com.ogc.graphics;

import java.util.ArrayList;



public class Utility {
	public static boolean PointInPolygon(Point point, Quadrilateral polygon) {
		  Point[] points = polygon.getPoints();
		  int i, j, nvert = points.length;
		  boolean c = false;

		  for(i = 0, j = nvert - 1; i < nvert; j = i++) {
		    if( ( (points[i].y >= point.y ) != (points[j].y >= point.y) ) &&
		        (point.x <= (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)
		      )
		      c = !c;
		  }

		  return c;
	}
}
