package co.edu.uniandes.taller3.client;


//HEAD
import java.util.HashMap;
import java.util.List;



import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.ContentParameters;
import co.edu.uniandes.taller3.shared.CFResult;
import co.edu.uniandes.taller3.shared.ContentResult;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
// branch 'master' of https://github.com/rcallea/hrs.git

public class Controller implements ClickHandler, EntryPoint {
	
	private CFView CFView;
	private CBLView cblView;
	private CBLView2 cblView2;
	private ContentView ContentView;
	private HybridView HybridView;
	private RSConstants constants = GWT.create(RSConstants.class);

	//private ArrayList<String> stocks = new ArrayList<String>();
	private HRSServiceAsync hrsSvc = GWT.create(HRSService.class);

	@Override
	public void onModuleLoad() {
		this.CFView=new CFView();
		this.CFView.setController(this);
		this.CFView.generateUI();
		
		this.cblView=new CBLView();
		this.cblView.setController(this);
		this.cblView.generateUI();

		this.cblView2=new CBLView2();
		this.cblView2.setController(this);
		this.cblView2.generateUI();

		this.ContentView=new ContentView();
		this.ContentView.setController(this);
		this.ContentView.generateUI();

		this.HybridView=new HybridView();
		this.HybridView.setController(this);
		this.HybridView.generateUI();
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String sender;
		if(event.getSource() instanceof Button) {
			sender = ((Button) event.getSource()).getText();
			
			if(sender.equals(this.constants.cfSend())) {
				if(this.CFView.validate()) {
					this.sendCFData();
				}
			}
			else if(sender.equals(this.constants.cblSend())) {
				if(this.cblView.validate()) {
					this.sendCBLData();
				}
			}else if(sender.equals(this.constants.cblSend2())) {
				if(this.cblView2.validate()) {
					this.sendCBLData2();
				}
			}else if(sender.equals(this.constants.contentSend())) {
				if(this.ContentView.validate()) {
					ContentView.getTableResultsBusiness().clear();
					this.LoadRecommendationContent();
				}
			}
			else if(sender.equals(this.constants.hybridSend())) {
				if(this.HybridView.validate()) {
					HybridView.getTableResultsBusiness().clear();
					this.LoadRecommendationHybrid();
				}
			}
		}		
		
	}
	
	private void sendCFData() {
		if(hrsSvc==null) hrsSvc = GWT.create(HRSService.class);
		CFParameters data=new CFParameters();
		
		try {
			data=new CFParameters(this.CFView.getTextboxWindowInitialDate().getText(),
					this.CFView.getTextboxWindowFinalDate().getText(),
					this.CFView.getTextboxNeighbors().getText(),
					this.CFView.getTextboxGradeNumber().getText(),
					this.CFView.getListBoxMeasureType(),
					this.CFView.getListBoxRecommenderType(),
					Integer.parseInt(this.CFView.getTextboxUser().getText()));
			this.CFView.getHtmlPrecisionResult().setHTML("<strong>Calculando...</strong>");
			this.CFView.getHtmlRecallResult().setHTML("<strong>Calculando...</strong>");
			this.CFView.getHtmlResultListResult().setHTML("<strong>Sin resultados</strong>");
	} catch (NumberFormatException nfe) {}
		
		AsyncCallback<CFResult> callback = new AsyncCallback<CFResult>() {
			public void onFailure(Throwable caught) {
				//Aquí poner el llamado en caso de que salga mal
			}

			@Override
			public void onSuccess(CFResult result) {
				//Aquí poner el llamado en caso de que salga bien
				updateCFResult(result);
			}
		};

		// Make the call to the stock price service.
		hrsSvc.initCF(data, callback);
	}

	private void updateCFResult(CFResult result) {
		this.CFView.getHtmlPrecisionResult().setText("" + result.getPrecision());
		this.CFView.getHtmlRecallResult().setText("" + result.getRecall());
		String text="Mostrando " + result.getDataInfo().length + " recomendaciones.<br/>";
		String[] resultData=result.getDataInfo();
		text=text + "<table><hr><td>Película</td><td>Géneros</td></hr>";
		for(int i=0;i<resultData.length;i++) {
			String oneData=resultData[i].replaceFirst("-----", "</td><td>");
			text+="<tr><td>" + oneData + "</td></tr>";
		}
		text=text + "</table>";
		this.CFView.getHtmlResultListResult().setHTML(text);
	}

	private void LoadRecommendationContent() {
		ContentParameters data=new ContentParameters();
		
		String nameCity = this.ContentView.getListboxCity(); 
		String category = this.ContentView.getTextboxCategory().getText();  
		String description = this.ContentView.getTextboxDescription().getText();
		String day = this.ContentView.getListboxDay();
		String hour = this.ContentView.getListboxHour();
		double weightName = Double.parseDouble(this.ContentView.getTextboxWeightName().getText());
		double weightCat = Double.parseDouble(this.ContentView.getTextboxWeightCat().getText());
		double weightAtt = Double.parseDouble(this.ContentView.getTextboxWeightAttr().getText());
		double weightCom = Double.parseDouble(this.ContentView.getTextboxWeightComm().getText());
		
		data=new ContentParameters(nameCity, category, description, day, hour, weightName, weightCat, weightAtt, weightCom);
		
		AsyncCallback<List<ContentResult>> callback = new AsyncCallback<List<ContentResult>>() {
			public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
			}

			public void onSuccess(List<ContentResult> result) {

				if(result.size() == 0){
					ContentView.getHtmlUiSubTitle().setHTML("<br/><h3> No se encontraron resultados</h3>");
				}
				else{
					ContentView.getHtmlUiSubTitle().setHTML("<br/><h3> Nuestras recomendaciones</h3>");
					int i = 0;
					for (final ContentResult contentResult : result) {
						Hyperlink link = new Hyperlink();  
						link.setText(contentResult.getName());
						link.addClickListener(new ClickListener() {
							public void onClick(Widget sender) {
						        PopupDeatils popup = new PopupDeatils(contentResult);
								popup.setStyleName("demo-popup");
							    popup.show();
						      }
							
						});
						
						ContentView.getTableResultsBusiness().setWidget(i, 0, link);
						i++;
					}
				}
			}
		};
		hrsSvc.getContentBusiness(data, null, callback);

	}
	
	private void LoadRecommendationHybrid() {
		ContentParameters contentData=new ContentParameters();
		
		HybridView.getHtmlUiSubTitle().setHTML("<br/><h3>Calculando</h3>");
		int userId = Integer.parseInt(this.HybridView.getTextboxUser().getText());
		String nameCity = this.HybridView.getListboxCity(); 
		String category = this.HybridView.getTextboxCategory().getText();  
		String description = this.HybridView.getTextboxDescription().getText();
		String day = this.ContentView.getListboxDay();
		String hour = this.ContentView.getListboxHour();
		double weightName = Double.parseDouble(this.ContentView.getTextboxWeightName().getText());
		double weightCat = Double.parseDouble(this.ContentView.getTextboxWeightCat().getText());
		double weightAtt = Double.parseDouble(this.ContentView.getTextboxWeightAttr().getText());
		double weightCom = Double.parseDouble(this.ContentView.getTextboxWeightComm().getText());		
		
		contentData=new ContentParameters(nameCity, category, description, day,hour, weightName, weightCat, weightAtt, weightCom);
		
		CFParameters cfData=new CFParameters();
		cfData=new CFParameters(this.CFView.getTextboxWindowInitialDate().getText(),
				this.CFView.getTextboxWindowFinalDate().getText(),
				this.CFView.getTextboxNeighbors().getText(),
				this.CFView.getTextboxGradeNumber().getText(),
				this.CFView.getListBoxMeasureType(),
				this.CFView.getListBoxRecommenderType(),
				userId);
			
		CBParametersL cbData=new CBParametersL(this.cblView.getListboxDatasetSize(),
				this.cblView.getTextboxWaitTime(),
				this.cblView.getListboxMinTermFrequency(),
				this.cblView.getListboxMinDocFrequency(),
				this.cblView.getListBoxMinWordLen(),
				"" + userId);

		CBParametersL cbData2=new CBParametersL(this.cblView2.getListboxDatasetSize(),
				this.cblView2.getTextboxWaitTime(),
				this.cblView2.getListboxMinTermFrequency(),
				this.cblView2.getListboxMinDocFrequency(),
				this.cblView2.getListBoxMinWordLen(),
				"" + userId);

		AsyncCallback<List<ContentResult>> callback = new AsyncCallback<List<ContentResult>>() {
			public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
			}

			public void onSuccess(List<ContentResult> result) {
				if(result.size() == 0){
					HybridView.getHtmlUiSubTitle().setHTML("<br/><h3> No se encontraron resultados</h3>");
				}
				else{
					HybridView.getHtmlUiSubTitle().setHTML("<br/><h3> Nuestras recomendaciones</h3>");
					int i = 0;
					for (final ContentResult contentResult : result) {
						Hyperlink link = new Hyperlink();  
						link.setText(contentResult.getName());
						link.addClickListener(new ClickListener() {
							public void onClick(Widget sender) {
						        PopupDeatils popup = new PopupDeatils(contentResult);
								popup.setStyleName("demo-popup");
							    popup.show();
						      }
							
						});
						
						HybridView.getTableResultsBusiness().setWidget(i, 0, link);
						i++;
					}
				}
			}
		};
		hrsSvc.getHybridBusiness(cfData, cbData, cbData2, contentData, callback);

	}
	
	private void sendCBLData() {
		if(hrsSvc==null) hrsSvc = GWT.create(HRSService.class);
		CBParametersL data=new CBParametersL();
		
		try {
			data=new CBParametersL(this.cblView.getListboxDatasetSize(),
					this.cblView.getTextboxWaitTime(),
					this.cblView.getListboxMinTermFrequency(),
					this.cblView.getListboxMinDocFrequency(),
					this.cblView.getListBoxMinWordLen(),
					this.cblView.getTextboxUser());
			this.cblView.getHtmlPrecisionResult().setHTML("<strong>Calculando...</strong>");
			this.cblView.getHtmlRecallResult().setHTML("<strong>Calculando...</strong>");
			this.cblView.getHtmlResultListResult().setHTML("<strong>Sin resultados</strong>");
		} catch (NumberFormatException nfe) {}
		
		AsyncCallback<CBResultL> callback = new AsyncCallback<CBResultL>() {
			public void onFailure(Throwable caught) {
				//Aquí poner el llamado en caso de que salga mal
			}

			@Override
			public void onSuccess(CBResultL result) {
				//Aquí poner el llamado en caso de que salga bien
				updateCBLResult(result);
			}
		};

		// Make the call to the stock price service.
		hrsSvc.initCBL(data, callback);
	}

	private void sendCBLData2() {
		if(hrsSvc==null) hrsSvc = GWT.create(HRSService.class);
		CBParametersL data=new CBParametersL();
		
		try {
			data=new CBParametersL(this.cblView2.getListboxDatasetSize(),
					this.cblView2.getTextboxWaitTime(),
					this.cblView2.getListboxMinTermFrequency(),
					this.cblView2.getListboxMinDocFrequency(),
					this.cblView2.getListBoxMinWordLen(),
					this.cblView2.getTextboxUser());
			this.cblView2.getHtmlPrecisionResult().setHTML("<strong>Calculando...</strong>");
			this.cblView2.getHtmlRecallResult().setHTML("<strong>Calculando...</strong>");
			this.cblView2.getHtmlResultListResult().setHTML("<strong>Sin resultados</strong>");
		} catch (NumberFormatException nfe) {}
		
		AsyncCallback<CBResultL> callback = new AsyncCallback<CBResultL>() {
			public void onFailure(Throwable caught) {
				//Aquí poner el llamado en caso de que salga mal
			}

			@Override
			public void onSuccess(CBResultL result) {
				//Aquí poner el llamado en caso de que salga bien
				updateCBLResult2(result);
			}
		};

		// Make the call to the stock price service.
		hrsSvc.initCBL2(data, callback);
	}

	private void updateCBLResult(CBResultL result) {
		this.cblView.getHtmlPrecisionResult().setText("" + result.getPrecision());
		this.cblView.getHtmlRecallResult().setText("" + result.getRecall());
		String text="";
		int tam=10;
		String[] resultData=result.getData();
		for(int i=2;i<resultData.length;i++) {
			text+=resultData[i] + " ";
		}
		
		String[] userText=result.getUserDocs();
		if(tam>userText.length) {
			tam=userText.length;
		}
		
		text="Mostrando " + tam + " reviews de " + userText.length + " para verificar<ul>";
		for(int i=0;i<tam;i++) {
			text=text+"<li>" + userText[i]+"</li>";
		}
		
		tam=50;
		String[] resultText=result.getOtherDocs();
		String[] resultText1=result.getDataInfo();
		if(tam>resultText.length) {
			tam=resultText.length;
		}
		
		text=text + "</ul><hr/>Mostrando " + tam + " reviews de " + resultText.length + " recomendados<ul>";
		for(int i=0;i<tam;i++) {
			text=text + "<li><strong>" + resultText1[i] + "</strong>: " + resultText[i]+"</li>";
		}
		
		text = text + "</ul>";
		this.cblView.getHtmlResultListResult().setHTML(text);
	}

	private void updateCBLResult2(CBResultL result) {
		this.cblView2.getHtmlPrecisionResult().setText("" + result.getPrecision());
		this.cblView2.getHtmlRecallResult().setText("" + result.getRecall());
		String text="";
		int tam=10;
		String[] resultData=result.getData();
		for(int i=2;i<resultData.length;i++) {
			text+=resultData[i] + " ";
		}
		
		String[] userText=result.getUserDocs();
		if(tam>userText.length) {
			tam=userText.length;
		}
		
		text="Mostrando " + tam + " reviews de " + userText.length + " para verificar<ul>";
		for(int i=0;i<tam;i++) {
			text=text+"<li>" + userText[i]+"</li>";
		}
		
		tam=50;
		String[] resultText=result.getOtherDocs();
		String[] resultText1=result.getDataInfo();
		if(tam>resultText.length) {
			tam=resultText.length;
		}
		
		text=text + "</ul><hr/>Mostrando " + tam + " reviews de " + resultText.length + " recomendados<ul>";
		for(int i=0;i<tam;i++) {
			text=text + "<li><strong>" + resultText1[i] + "</strong>: " + resultText[i]+"</li>";
		}
		
		text = text + "</ul>";
		this.cblView2.getHtmlResultListResult().setHTML(text);
	}
}
