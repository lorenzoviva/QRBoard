package com.ogc.browsers.elements;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.ogc.browsers.attrubutes.Fontsize;
import com.ogc.browsers.attrubutes.QRAttribute;
import com.ogc.browsers.attrubutes.Text;

public class Div extends QRElement{

	@Override
	public List<QRAttribute> getAttributes(Element element) {
		Text text = new Text(element);
		Fontsize fontsize = new Fontsize(element);
		List<QRAttribute> res = new ArrayList<QRAttribute>();
		res.add(text);
		res.add(fontsize);
		return res;
	}

}
