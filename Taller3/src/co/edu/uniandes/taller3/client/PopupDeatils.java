package co.edu.uniandes.taller3.client;


import java.util.Arrays;
import java.util.HashMap;

import co.edu.uniandes.taller3.shared.Movie;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupDeatils extends PopupPanel {

	private Label htmlLabelName = new Label();
	private Label htmlLabelAbstract = new Label();
	private Label htmlLabelDistribuidor = new Label();
	private Label htmlLabelReparto = new Label();
	private Label htmlLabelDirector = new Label();
	private VerticalPanel vp = new VerticalPanel();
	
	private HRSServiceAsync hrsSvc = GWT.create(HRSService.class);


	public PopupDeatils(Movie contentResult) {
		super(true);

		LoadInfoBusiness(contentResult);
	}

	private void LoadInfoBusiness(Movie movie) {
			
		htmlLabelName.setText(movie.getName());
		htmlLabelAbstract.setText(movie.getListAbstract()[0].replaceAll("%2E", "."));
		
		String text = "";
		for (int i = 0; i < movie.getListActores().length; i++) {
			text += movie.getListActores()[i] + "</br>";
		}
		htmlLabelReparto.setText(text.replaceAll("%2E", "."));
			
		text = "";
		for (int i = 0; i < movie.getListDirector().length; i++) {
			text += movie.getListDirector()[i] + "</br>";
		}	
		htmlLabelDirector.setText(text.replaceAll("%2E", "."));
		
		text = "";
		for (int i = 0; i < movie.getListDistribuidor().length; i++) {
			text += movie.getListDistribuidor()[i] + "</br>";
		}	
		htmlLabelDistribuidor.setText(text.replaceAll("%2E", "."));
			
		vp.add(new HTML("<table style='width:500px'>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td style='height:50px'><h3>" + htmlLabelName.getText() + "</h3></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td><b>Resumen</b></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td>" + htmlLabelAbstract.getText() + "</br></br></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td><b>Reparto</b></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td>" + htmlLabelReparto.getText() + "</br></br></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td><b>Director</b></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td>" + htmlLabelDirector.getText() + "</br></br></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td><b>Distribuidor</b></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("<tr>"));
		vp.add(new HTML("<td>" + htmlLabelDistribuidor.getText() + "</br></br></td>"));
		vp.add(new HTML("</tr>"));
		vp.add(new HTML("</table>"));

		setWidget(vp);
			
	}

}
