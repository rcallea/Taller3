package co.edu.uniandes.taller3.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class CFView {

	/**
	 * Constants used in web page
	 */
	private RSConstants constants = GWT.create(RSConstants.class);

	/**
	 * Form elements: Label, textbox, dropdown, button
	 */
	//private FlexTable ft=new FlexTable();
	//private ListBox listBoxDispositivo=new ListBox();
	private VerticalPanel vp=new VerticalPanel();
	private HTML htmlUiTitle=new HTML("<div style='width:776px'><h2>" + constants.cfTitle() + "</h2></div>");
	private HTML htmlWindowInitialDate=new HTML(constants.cfWindowDate());
	private HTML htmlGradeNumber=new HTML(constants.cfGradeNumber());
	private HTML htmlNeighbors=new HTML(constants.cfNeighbors());
	private HTML htmlMeasureType=new HTML(constants.cfMeasureType());
	private HTML htmlRecommenderType=new HTML(constants.cfRecommenderType());
	private HTML htmlUser=new HTML(constants.cfUser());
	private HTML htmlPrecision=new HTML(constants.cfPrecision());
	private HTML htmlRecall=new HTML(constants.cfRecall());
	private HTML htmlResultList=new HTML("<strong>" +  constants.cfResultList() + "</strong></br></br></br><img src='/images/DVDMoviesTemplate.png'>");
	
	private HTML htmlError=new HTML();
	private TextBox textboxWindowInitialDate=new TextBox();
	private TextBox textboxWindowFinalDate=new TextBox();
	private TextBox textboxNeighbors=new TextBox();
	private TextBox textboxGradeNumber=new TextBox();
	private ListBox listBoxMeasureType=new ListBox();
	private ListBox listBoxRecommenderType=new ListBox();
	private TextBox textboxUser=new TextBox();
	private HTML htmlPrecisionResult=new HTML("");
	private HTML htmlRecallResult=new HTML("");
	private HTML htmlResultListResult=new HTML("");
	private Button buttonSend = new Button(constants.cfSend());
	private Controller controller;
	
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
		HorizontalPanel hp=new HorizontalPanel();
		
		this.htmlResultList.setVisible(false);
		
		this.textboxWindowInitialDate.setText("2014/12/01");
		this.textboxWindowFinalDate.setText("2014/12/31");
		this.textboxNeighbors.setText("4");
		this.textboxGradeNumber.setText("5");
		this.setListBoxMeasureType(constants.cfMeasureTypeValues());
		this.setListBoxRecommenderType(constants.cfRecommenderTypeValues());
		this.textboxUser.setText("66635");
		
		this.textboxNeighbors.setWidth("100px");
		this.textboxGradeNumber.setWidth("100px");
		this.listBoxMeasureType.setWidth("200px");
		this.listBoxRecommenderType.setWidth("200px");
		this.textboxUser.setWidth("200px");
		
		hp.add(this.textboxWindowInitialDate);
		hp.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		hp.add(this.textboxWindowFinalDate);
		
		this.vp.add(this.htmlUiTitle);
		ft.setWidget(row++,column, this.htmlWindowInitialDate);
		ft.setWidget(row++,column, this.htmlGradeNumber);
		ft.setWidget(row++,column, this.htmlNeighbors);
		ft.setWidget(row++,column, this.htmlMeasureType);
		ft.setWidget(row++,column, this.htmlRecommenderType);
		ft.setWidget(row++,column, this.htmlUser);
		ft.setWidget(row++,column, this.htmlPrecision);
		ft.setWidget(row++,column, this.htmlRecall);
		ft.setWidget(row++,column, this.htmlResultList);
		
		column++;
		row=0;
		ft.setWidget(row++, column, hp);
		ft.setWidget(row++, column, this.textboxGradeNumber);
		ft.setWidget(row++, column, this.textboxNeighbors);
		ft.setWidget(row++, column, this.listBoxMeasureType);
		ft.setWidget(row++, column, this.listBoxRecommenderType);
		ft.setWidget(row++, column, this.textboxUser);
		ft.setWidget(row++, column, this.htmlPrecisionResult);
		ft.setWidget(row++, column, this.htmlRecallResult);
		ft.setWidget(row++, column, this.htmlResultListResult);
		ft.setStyleName("table table-striped");
		this.vp.add(ft);

		this.vp.add(this.htmlError);
		this.vp.add(this.buttonSend);
		this.buttonSend.addClickHandler(this.controller);
		RootPanel.get("cf").add(this.vp);
		
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
		if(!this.getTextboxNeighbors().getText().matches("[0-9]+")) {
			retorno=false;
			message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfNeighbors() + "\": " + this.constants.uiFieldErrorMessage() + "</li>";
		} else {
			try {
				int value=Integer.parseInt(this.getTextboxNeighbors().getText());
				if(value<constants.cfNeighborsMinValue() || value>constants.cfNeighborsMaxValue()) {
					retorno=false;
					message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfNeighbors() + "\". " + this.constants.uiFieldOutOfBoundsMessage(); 
				}
			} catch (NumberFormatException nfe) {}
		}

		if(!this.getTextboxGradeNumber().getText().matches("[0-9]+")) {
			retorno=false;
			message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfNeighbors() + "\": " + this.constants.uiFieldErrorMessage() + "</li>";
		} else {
			try {
				int value=Integer.parseInt(this.getTextboxGradeNumber().getText());
				if(value<constants.cfNeighborsMinValue() || value>constants.cfNeighborsMaxValue()) {
					retorno=false;
					message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfNeighbors() + "\". " + this.constants.uiFieldOutOfBoundsMessage(); 
				}
			} catch (NumberFormatException nfe) {}
		}

		if(!this.getTextboxUser().getText().matches("[0-9]+")) {
			retorno=false;
			message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfUser() + "\": " + this.constants.uiFieldErrorMessage() + "</li>";
		} else {
			try {
				int value=Integer.parseInt(this.getTextboxUser().getText());
				if(value<Integer.MIN_VALUE || value>Integer.MAX_VALUE) {
					retorno=false;
					message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfUser() + "\". " + this.constants.uiFieldOutOfBoundsMessage(); 
				}
			} catch (NumberFormatException nfe) {}
		}
		
		DateTimeFormat format = DateTimeFormat.getFormat("yyyy/MM/dd");
		try {
			format.parse(this.getTextboxWindowInitialDate().getText());
	 
		} catch (IllegalArgumentException e) {
			retorno=false;
			message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfWindowDate() + "\". " + this.constants.uiFieldOutOfBoundsMessage(); 
		}

		try {
			format.parse(this.getTextboxWindowFinalDate().getText());
	 
		} catch (IllegalArgumentException e) {
			retorno=false;
			message += "<li>" + this.constants.uiFieldError() + "\" " + this.constants.cfWindowDate() + "\". " + this.constants.uiFieldOutOfBoundsMessage(); 
		}

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
	 * @return the htmlDatasetSize
	 */
	public HTML getHtmlDatasetSize() {
		return htmlWindowInitialDate;
	}

	/**
	 * @param htmlDatasetSize the htmlDatasetSize to set
	 */
	public void setHtmlDatasetSize(HTML htmlDatasetSize) {
		this.htmlWindowInitialDate = htmlDatasetSize;
	}

	/**
	 * @return the htmlNeighbors
	 */
	public HTML getHtmlNeighbors() {
		return htmlNeighbors;
	}

	/**
	 * @param htmlNeighbors the htmlNeighbors to set
	 */
	public void setHtmlNeighbors(HTML htmlNeighbors) {
		this.htmlNeighbors = htmlNeighbors;
	}

	/**
	 * @return the htmlGradeNumber
	 */
	public HTML getHtmlGradeNumber() {
		return htmlGradeNumber;
	}

	/**
	 * @param htmlGradeNumber the htmlGradeNumber to set
	 */
	public void setHtmlGradeNumber(HTML htmlGradeNumber) {
		this.htmlGradeNumber = htmlGradeNumber;
	}

	/**
	 * @return the htmlMeasureType
	 */
	public HTML getHtmlMeasureType() {
		return htmlMeasureType;
	}

	/**
	 * @param htmlMeasureType the htmlMeasureType to set
	 */
	public void setHtmlMeasureType(HTML htmlMeasureType) {
		this.htmlMeasureType = htmlMeasureType;
	}

	/**
	 * @return the htmlRecommenderType
	 */
	public HTML getHtmlRecommenderType() {
		return htmlRecommenderType;
	}

	/**
	 * @param htmlRecommenderType the htmlRecommenderType to set
	 */
	public void setHtmlRecommenderType(HTML htmlRecommenderType) {
		this.htmlRecommenderType = htmlRecommenderType;
	}

	/**
	 * @return the htmlUser
	 */
	public HTML getHtmlUser() {
		return htmlUser;
	}

	/**
	 * @param htmlUser the htmlUser to set
	 */
	public void setHtmlUser(HTML htmlUser) {
		this.htmlUser = htmlUser;
	}

	/**
	 * @return the htmlPrecision
	 */
	public HTML getHtmlPrecision() {
		return htmlPrecision;
	}

	/**
	 * @param htmlPrecision the htmlPrecision to set
	 */
	public void setHtmlPrecision(HTML htmlPrecision) {
		this.htmlPrecision = htmlPrecision;
	}

	/**
	 * @return the htmlRecall
	 */
	public HTML getHtmlRecall() {
		return htmlRecall;
	}

	/**
	 * @param htmlRecall the htmlRecall to set
	 */
	public void setHtmlRecall(HTML htmlRecall) {
		this.htmlRecall = htmlRecall;
	}

	/**
	 * @return the htmlResultList
	 */
	public HTML getHtmlResultList() {
		return htmlResultList;
	}

	/**
	 * @param htmlResultList the htmlResultList to set
	 */
	public void setHtmlResultList(HTML htmlResultList) {
		this.htmlResultList = htmlResultList;
	}

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

	public TextBox getTextboxWindowInitialDate() {
		return textboxWindowInitialDate;
	}

	public void setTextboxWindowInitialDate(TextBox textboxWindowInitialDate) {
		this.textboxWindowInitialDate = textboxWindowInitialDate;
	}

	public TextBox getTextboxWindowFinalDate() {
		return textboxWindowFinalDate;
	}

	public void setTextboxWindowFinalDate(TextBox textboxWindowFinalDate) {
		this.textboxWindowFinalDate = textboxWindowFinalDate;
	}

	/**
	 * @return the textboxNeighbors
	 */
	public TextBox getTextboxNeighbors() {
		return textboxNeighbors;
	}

	/**
	 * @param textboxNeighbors the textboxNeighbors to set
	 */
	public void setTextboxNeighbors(TextBox textboxNeighbors) {
		this.textboxNeighbors = textboxNeighbors;
	}

	/**
	 * @return the textboxGradeNumber
	 */
	public TextBox getTextboxGradeNumber() {
		return textboxGradeNumber;
	}

	/**
	 * @param textboxGradeNumber the textboxGradeNumber to set
	 */
	public void setTextboxGradeNumber(TextBox textboxGradeNumber) {
		this.textboxGradeNumber = textboxGradeNumber;
	}

	/**
	 * @return the listBoxMeasureType
	 */
	public String getListBoxMeasureType() {
		return this.listBoxMeasureType.getValue(this.listBoxMeasureType.getSelectedIndex());
	}

	/**
	 * @param listBoxMeasureType the listBoxMeasureType to set
	 */
	public void setListBoxMeasureType(String[] measureTypeValues) {
		for(int i=0;i<measureTypeValues.length;i++) {
			String[] cols=measureTypeValues[i].split(";");
			this.listBoxMeasureType.addItem(cols[1], cols[0]);
		}
	}

	/**
	 * @return the listBoxRecommenderType
	 */
	public String getListBoxRecommenderType() {
		return this.listBoxRecommenderType.getValue(this.listBoxRecommenderType.getSelectedIndex());
	}

	/**
	 * @param listBoxRecommenderType the listBoxRecommenderType to set
	 */
	public void setListBoxRecommenderType(String[] recommenderTypeValues) {
		for(int i=0;i<recommenderTypeValues.length;i++) {
			String[] cols=recommenderTypeValues[i].split(";");
			this.listBoxRecommenderType.addItem(cols[1], cols[0]);
		}
	}

	/**
	 * @return the textboxUser
	 */
	public TextBox getTextboxUser() {
		return textboxUser;
	}

	/**
	 * @param textboxUser the textboxUser to set
	 */
	public void setTextboxUser(TextBox textboxUser) {
		this.textboxUser = textboxUser;
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

	/**
	 * @return the htmlPrecisionResult
	 */
	public HTML getHtmlPrecisionResult() {
		return htmlPrecisionResult;
	}

	/**
	 * @param htmlPrecisionResult the htmlPrecisionResult to set
	 */
	public void setHtmlPrecisionResult(HTML htmlPrecisionResult) {
		this.htmlPrecisionResult = htmlPrecisionResult;
	}

	/**
	 * @return the htmlRecallResult
	 */
	public HTML getHtmlRecallResult() {
		return htmlRecallResult;
	}

	/**
	 * @param htmlRecallResult the htmlRecallResult to set
	 */
	public void setHtmlRecallResult(HTML htmlRecallResult) {
		this.htmlRecallResult = htmlRecallResult;
	}

	/**
	 * @return the htmlResultListResult
	 */
	public HTML getHtmlResultListResult() {
		return htmlResultListResult;
	}

	/**
	 * @param htmlResultListResult the htmlResultListResult to set
	 */
	public void setHtmlResultListResult(HTML htmlResultListResult) {
		this.htmlResultListResult = htmlResultListResult;
	}
}
