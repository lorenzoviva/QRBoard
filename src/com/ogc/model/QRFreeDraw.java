package com.ogc.model;

public class QRFreeDraw extends QRSquare {
	@Override
	public String getCreationChoiseHtml() {
		return "<td height='25%' width='25%' bgcolor='#FF0000' style=\"word-wrap:break-word;\"><div align='center'>"+this.getClass().getSimpleName()+"</div><br><div align='center'><i  class='fa fa-pencil'></div></i></td>";
	}
}
