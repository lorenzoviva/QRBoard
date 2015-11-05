package com.ogc.model.special;

import android.util.Log;

import com.ogc.model.QRRepresentation;
import com.ogc.model.QRSquare;

public class QRCreationChooserPage extends QRRepresentation{

	public QRCreationChooserPage(QRSquare realQR, String choises) {
		super(realQR.getText(), getStaticHtml(choises));
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}
	
	public static String getStaticHtml(String params) {
		if (!params.equals(null)) {
			String html = "<!DOCTYPE html><html><head><link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css'></head><body style=\"margin: 0px;\"><table height='250px' width='250px'  cellpadding='0' cellspacing='0' style=\"table-layout: fixed;\"><tbody>";

			if (params.endsWith(",")) {
				params = params.substring(0, params.length() - 1);
			}
			String[] choisesArray = params.split(",");
			for (int i = 0; i < 16; i++) {
				if (choisesArray.length > i) {
					String qrType = choisesArray[i];
					Log.d("qrType", qrType);
					try {
						QRSquare qrSquare = (QRSquare) Class.forName("com.ogc.model." + qrType).newInstance();
						if(i == 0){
							html += "<tr height='25%'>";
						}
						if(i != 0 && i%4 == 0){
							html += "</tr><tr height='25%'>";
						}
						html += qrSquare.getCreationChoiseHtml();
					} catch (Exception  e) {
						e.printStackTrace();
					}
				}else{
					if(i != 0 && i%4 == 0){
						html += "</tr><tr height='25%'>";
					}
					html +="<td width='25%' >&nbsp;</td>";
				}
			}
			html += "</tr></tbody></table></body></html>";
			return html;

		} else {
			return "<h1 styleClass=\"margin:0 ;padding: 0;text-align: center;display:table-cell;vertical-align:middle; background:grey;height: 300px;width: 450px;\">Unable to request choises</h1>";
		}
	}

}
