package com.ogc.model.special;

import android.util.Log;

import com.ogc.action.Action;
import com.ogc.model.QRInternalWebPage;
import com.ogc.model.QRSquare;

public class QRCreationChooserPage extends QRInternalWebPage {

	public QRCreationChooserPage(QRSquare realQR, String choises) {
		super(realQR.getText(), getHtmlFromChoises(choises));
		setOne(realQR.getOne());
		setTwo(realQR.getTwo());
		setThree(realQR.getThree());
		setFour(realQR.getFour());
	}

	public static String getHtmlFromChoises(String choises) {
		if (!choises.equals(null)) {
			String html = "<!DOCTYPE html><html><head><link rel='stylesheet' href='http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css'></head><body style=\"margin: 0px;\"><table height='250px' width='250px'  cellpadding='0' cellspacing='0' style=\"table-layout: fixed;\"><tbody>";

			if (choises.endsWith(",")) {
				choises = choises.substring(0, choises.length() - 1);
			}
			String[] choisesArray = choises.split(",");
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
