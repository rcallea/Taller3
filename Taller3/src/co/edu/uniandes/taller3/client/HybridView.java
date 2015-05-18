package co.edu.uniandes.taller3.client;

import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HybridView {

	/**
	 * Constants used in web page
	 */
	private RSConstants constants = GWT.create(RSConstants.class);

	/**
	 * Form elements: Label, textbox, dropdown, button
	 */
	private VerticalPanel vp=new VerticalPanel();
	private HTML htmlUiTitle=new HTML("<div style='width:776px'><h3>Recomendador hibrido</h3></div>");
	private HTML htmlUiSubTitle=new HTML("<br/><h3></h3>");
	private HTML htmlLabelUsuario=new HTML(constants.cblUser());
	private HTML htmlResultList=new HTML(constants.cfResultList());
	private HTML htmlError=new HTML();
	private TextBox textboxUser=new TextBox();
	private Button buttonSend = new Button(constants.hybridSend());
	private Controller controller;
	private HTML htmlResultListResult=new HTML();
	

	HorizontalPanel hp0=new HorizontalPanel();
	/**
	 * @param controller the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Adds the elements to the root page
	 */
	public void generateUI() {
		int row=0;
		int column=0;
		FlexTable ft=new FlexTable();
		this.textboxUser.setWidth("250px");
				
		hp0.add(new HTML("<div style= 'width:150px'>" + this.htmlLabelUsuario + "</div>"));
		hp0.add(this.textboxUser);
		
		this.vp.add(this.htmlUiTitle);
		ft.setWidget(row++,column, hp0);
		ft.setStyleName("table table-striped");
		this.vp.add(ft);

		this.vp.add(this.htmlError);
		this.vp.add(this.buttonSend);
		this.vp.add(this.htmlUiSubTitle);
		this.vp.add(this.htmlResultListResult);
		this.buttonSend.addClickHandler(this.controller);
		
		RootPanel.get("hybrid").add(this.vp);
		
		this.hidErrorMessage();
		
	}
	
	/**
	 * Shows an error message on htmlError 
	 * @param message
	 */
	public void showErrorMessage(String message) {
		this.htmlError.setHTML(message);
		this.htmlError.setStyleName("alert alert-danger");
	}
	
	public void hidErrorMessage() {
		this.htmlError.setHTML("");
		this.htmlError.setStyleName("none");
	}

	public boolean validate() {
		boolean retorno=true;
		String message="<ul>";

		if(retorno==false) {
			this.showErrorMessage(message + "</ul>");
		}
		else {
			this.hidErrorMessage();
		}
		return retorno;
	}
	

	/**
	 * @return the htmlUiTitle
	 */
	public HTML getHtmlUiTitle() {
		return htmlUiTitle;
	}

	/**
	 * @param htmlUiTitle the htmlUiTitle to set
	 */
	public void setHtmlUiTitle(HTML htmlUiTitle) {
		this.htmlUiTitle = htmlUiTitle;
	}

	/**
	 * @return the htmlResultList
	 */
	
	/**
	 * @return the htmlError
	 */
	public HTML getHtmlError() {
		return htmlError;
	}

	/**
	 * @param htmlError the htmlError to set
	 */
	public void setHtmlError(HTML htmlError) {
		this.htmlError = htmlError;
	}

	/**
	 * @return the buttonSend
	 */
	public Button getButtonSend() {
		return buttonSend;
	}

	/**
	 * @param buttonSend the buttonSend to set
	 */
	public void setButtonSend(Button buttonSend) {
		this.buttonSend = buttonSend;
	}

	public HTML getHtmlUiSubTitle() {
		return htmlUiSubTitle;
	}

	public void setHtmlUiSubTitle(HTML htmlUiSubTitle) {
		this.htmlUiSubTitle = htmlUiSubTitle;
	}
	public TextBox getTextboxUser() {
		return textboxUser;
	}

	public void setTextboxUser(TextBox textboxUser) {
		this.textboxUser = textboxUser;
	}

	public HTML getHtmlResultList() {
		return htmlResultList;
	}

	public void setHtmlResultList(HTML htmlResultList) {
		this.htmlResultList = htmlResultList;
	}

	public HTML getHtmlResultListResult() {
		return htmlResultListResult;
	}

	public void setHtmlResultListResult(HTML htmlResultListResult) {
		this.htmlResultListResult = htmlResultListResult;
	}
}
