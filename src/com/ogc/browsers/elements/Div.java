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
import com.ogc.browsers.attrubutes.QRAttribute;
import com.ogc.browsers.attrubutes.Text;
import com.ogc.browsers.attrubutes.Textcolor;

public class Div extends QRElement{

	@Override
	public List<QRAttribute> getAttributes(Element element) {
		Text text = new Text(element);
		Fontsize fontsize = new Fontsize(element);
		Textcolor textcolor = new Textcolor(element);
		Backgroundcolor bgcolor = new Backgroundcolor(element);
		Fontweight fontweight = new Fontweight(element);
		Borderwidth borderwidth = new Borderwidth(element);
		Bordercolor bodercolor = new Bordercolor(element);
		Image image = new Image(element);
		Borderradius borderradius = new Borderradius(element);
		Bounds bounds = new Bounds(element);
		List<QRAttribute> res = new ArrayList<QRAttribute>();
		res.add(text);
		res.add(fontsize);
		res.add(textcolor);
		res.add(bgcolor);
		res.add(fontweight);
		res.add(borderwidth);
		res.add(bodercolor);
		res.add(borderradius);
		res.add(bounds);
		res.add(image);
		return res;
	}

}
