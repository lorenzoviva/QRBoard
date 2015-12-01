package com.ogc.browsers.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jsoup.nodes.Element;

import com.ogc.browsers.attrubutes.QRAttribute;


public abstract class QRElement {

	private static String correctTagName(String string){
		if (string.length() > 1) {
			return string.substring(0, 1).toUpperCase(Locale.ROOT).concat(string.substring(1, string.length()).toLowerCase());
		} else {
			return string.toUpperCase(Locale.ROOT);

		}
	}
	public static QRElement getQRElement(String tagname) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String correctedTagname = correctTagName(tagname);
		return (QRElement)Class.forName("com.ogc.browsers.elements."+correctedTagname).newInstance();
	}
	public abstract List<QRAttribute> getAttributes(Element element);
}
