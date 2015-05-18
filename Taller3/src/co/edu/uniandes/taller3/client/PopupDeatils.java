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

	private HTML htmlUiTitle = new HTML("<h2> HOLA </h2>");
	private HTML htmlUiSubTitle = new HTML("<br/><b>Adrress</b>");
	private Label htmlLabelName = new Label();
	private Label htmlLabelAddress = new Label();
	private Label htmlLabelHours = new Label();
	private Label htmlLabelAttributes = new Label();
	private Label htmlLabelComments = new Label();
	private VerticalPanel vp = new VerticalPanel();
	private Movie contentResultAux = new Movie();
	
	private HRSServiceAsync hrsSvc = GWT.create(HRSService.class);

	// <img src='/images/Start.png' />
	public PopupDeatils(Movie contentResult) {
		super(true);

		contentResultAux = contentResult;
		LoadInfoBusiness(contentResult);
	}

	private void LoadInfoBusiness(Movie movie) {

	
			htmlLabelName.setText(movie.getMovieUri());
			htmlLabelAddress.setText(movie.getListAbstract()[0]);
			htmlLabelAttributes.setText(movie.getListActores()[0]);
			htmlLabelComments.setText(movie.getListDirector()[0]);
			htmlLabelHours.setText(movie.getListDistribuidor()[0]);
			
			
			vp.add(new HTML("<table style='width:500px'>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td style='height:50px'><h3>" + htmlLabelName.getText() + "</h3></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td><b>Street Address</b></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td>" + htmlLabelAddress.getText() + "</br></br></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td><b>Hours</b></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td>" + htmlLabelHours.getText() + "</br></br></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td><b>Attributes</b></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td>" + htmlLabelAttributes.getText()+ "</br></br></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td><b>Comments</b></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td>" + htmlLabelComments.getText() + "</br></br></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("<tr>"));
			vp.add(new HTML("<td><b>Process</b></td>"));
			vp.add(new HTML("</tr>"));
			vp.add(new HTML("</table>"));

			setWidget(vp);
			
	}

}
