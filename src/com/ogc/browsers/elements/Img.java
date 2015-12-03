package com.ogc.browsers.elements;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.ogc.browsers.attrubutes.Backgroundcolor;
import com.ogc.browsers.attrubutes.Bordercolor;
import com.ogc.browsers.attrubutes.Borderradius;
import com.ogc.browsers.attrubutes.Borderwidth;
import com.ogc.browsers.attrubutes.Bounds;
import com.ogc.browsers.attrubutes.Fontsize;
import com.ogc.browsers.attrubutes.Fontweight;
import com.ogc.browsers.attrubutes.Image;
import com.ogc.browsers.attrubutes.Link;
import com.ogc.browsers.attrubutes.QRAttribute;
import com.ogc.browsers.attrubutes.Text;
import com.ogc.browsers.attrubutes.Textcolor;

public class Img extends QRElement{

	@Override
	public List<QRAttribute> getAttributes(Element element) {
		
		Backgroundcolor bgcolor = new Backgroundcolor(element);
		Borderwidth borderwidth = new Borderwidth(element);
		Bordercolor bodercolor = new Bordercolor(element);
		Borderradius borderradius = new Borderradius(element);
		Image image = new Image(element);
		Bounds bounds = new Bounds(element);
		Link link = new Link(element);
		List<QRAttribute> res = new ArrayList<QRAttribute>();
		res.add(link);
		res.add(bgcolor);
		res.add(image);
		res.add(borderwidth);
		res.add(bodercolor);
		res.add(borderradius);
		res.add(bounds);
		return res;
	}

}
