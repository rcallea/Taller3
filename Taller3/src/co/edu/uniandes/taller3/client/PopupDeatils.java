package co.edu.uniandes.taller3.client;

import java.util.Arrays;
import java.util.HashMap;

import co.edu.uniandes.taller3.shared.ContentParameters;
import co.edu.uniandes.taller3.shared.ContentResult;

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
	private Label htmlLabelProcess = new Label();
	private VerticalPanel vp = new VerticalPanel();
	private ContentResult contentResultAux = new ContentResult();
	
	private HRSServiceAsync hrsSvc = GWT.create(HRSService.class);

	// <img src='/images/Start.png' />
	public PopupDeatils(ContentResult contentResult) {
		super(true);

		contentResultAux = contentResult;
		LoadInfoBusiness(contentResult);
	}

	private void LoadInfoBusiness(ContentResult contentResult) {

		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors.
			}

			public void onSuccess(String[] result) {
				htmlLabelName.setText(result[0]);
				htmlLabelAddress.setText(result[1]);
				htmlLabelAttributes.setText(result[2]);
				htmlLabelComments.setText(result[3]);
				htmlLabelHours.setText(result[5]);
				
				String process = "Arreglo solicitud del usuario: " + Arrays.toString(contentResultAux.getKeyWordsRequest()) + "</br>";
				process += "</br>";
				process += "Arreglo nombre: " + Arrays.toString(contentResultAux.getKeyWordsName()) + "</br>";
				process += "Similitud: " + contentResultAux.getSimilarityName() + "</br></br>";
				process += "Arreglo categorias: " + Arrays.toString(contentResultAux.getKeyWordsCategories()) + "</br>";
				process += "Similitud: " + contentResultAux.getSimilarityCategories() + "</br></br>";
				process += "Arreglo atributos: " + Arrays.toString(contentResultAux.getKeyWordsAttributes()) + "</br>";
				process += "Similitud: " + contentResultAux.getSimilarityAttributes() + "</br></br>";
				process += "Arreglo comentarios: " + Arrays.toString(contentResultAux.getKeyWordsComments()) + "</br>";
				process += "Similitud: " + contentResultAux.getSimilarityComments() + "</br></br>";
				process += "SIMILITUD TOTAL: " + contentResultAux.getSimilarityTotal() + "</br></br>";
				process += contentResultAux.getIdBusiness();
				
				htmlLabelProcess.setText(process);
				
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
				vp.add(new HTML("<tr>"));
				vp.add(new HTML("<td>" + htmlLabelProcess.getText() + "</td>"));
				vp.add(new HTML("</tr>"));
				vp.add(new HTML("</table>"));

				setWidget(vp);
			}
		};
		hrsSvc.getInformationBusiness(contentResult.getIdBusiness(), callback);

	}

}
