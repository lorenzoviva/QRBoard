package com.ogc.model;

import com.example.qrboard.LWebView;
import com.google.gson.annotations.Expose;

public class QRFreeDraw extends QRSquare {
	
	//per serializzare/deserializzare personalmete l'immagine che viene inserita nel json
//	@Expose(serialize = false, deserialize = false)
	private transient byte[] img;
	private String name;

	public QRFreeDraw() {
	}

	public QRFreeDraw(byte[] im, String nome, String text) {
		super(text);
		this.img = im;
		this.name = nome;
	}

	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' id='" + LWebView.applicationid + ".create." + this.getClass().getSimpleName() + "'  bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>" + this.getClass().getSimpleName() + "</div><br><div align='center'><i  class='fa fa-pencil'></div></i></td>";
	}

	/**
	 * @return the img
	 */
	public byte[] getImg() {
		return img;
	}

	/**
	 * @param img
	 *            the img to set
	 */
	public void setImg(byte[] img) {
		this.img = img;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
