package co.edu.uniandes.taller3.client;


//HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;








import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.CFResult;

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
			}
			else if(sender.equals(this.constants.hybridSend())) {
				if(this.HybridView.validate()) {
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
					Integer.parseInt(this.CFView.getTextboxUser().getText()), false);
			this.CFView.getHtmlPrecisionResult().setHTML("<strong>Calculando...</strong>");
			this.CFView.getHtmlRecallResult().setHTML("<strong>Calculando...</strong>");
			this.CFView.getHtmlResultListResult().setHTML("<strong>Sin resultados</strong>");
	} catch (NumberFormatException nfe) {}
		
		AsyncCallback<CFResult> callback = new AsyncCallback<CFResult>() {
			public void onFailure(Throwable caught) {
				//Aqu� poner el llamado en caso de que salga mal
			}

			@Override
			public void onSuccess(CFResult result) {
				//Aqu� poner el llamado en caso de que salga bien
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
		text=text + "<table><hr><td>Pel\u00EDcula</td><td>G\u00E9neros</td></hr>";
		for(int i=0;i<resultData.length;i++) {
			String oneData=resultData[i].replaceFirst("-----", "</td><td>");
			text+="<tr><td>" + oneData + "</td></tr>";
		}
		text=text + "</table>";
		this.CFView.getHtmlResultListResult().setHTML(text);
	}

	private void LoadRecommendationHybrid() {
		
		HybridView.getHtmlUiSubTitle().setHTML("<br/><h3>Calculando</h3>");
		int userId = Integer.parseInt(this.HybridView.getTextboxUser().getText());
		
		
		CFParameters cfData=new CFParameters();
		cfData=new CFParameters(
				this.CFView.getTextboxWindowInitialDate().getText(),
				this.CFView.getTextboxWindowFinalDate().getText(),
				this.CFView.getTextboxNeighbors().getText(),
				this.CFView.getTextboxGradeNumber().getText(),
				this.CFView.getListBoxMeasureType(),
				this.CFView.getListBoxRecommenderType(),
				userId, true);
		
		
		CBParametersL cbData=new CBParametersL(
				this.cblView.getTextboxWindowInitialDate().getText(),
				this.cblView.getTextboxWindowFinalDate().getText(),
				this.cblView.getTextboxWaitTime(),
				this.cblView.getListboxMinTermFrequency(),
				this.cblView.getListboxMinDocFrequency(),
				this.cblView.getListBoxMinWordLen(),
				userId);

		AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {

			public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
			}

			public void onSuccess(String[] result) {
				if(result.length == 0){
					HybridView.getHtmlUiSubTitle().setHTML("<br/><h3> No se encontraron resultados</h3>");
				}
				else{
					
					String text = "<table><hr><td>Pel�cula</td><td>G�neros</td></hr>";
					for (String nombre : result) {
						String oneData = nombre.replaceFirst("-----", "</td><td>");
						text+="<tr><td>" + oneData + "</td></tr>";
					}
					text += "</table>";
					System.out.println(text);
					HybridView.getHtmlResultListResult().setHTML(text);
					
				}
			}
		};
		hrsSvc.getHybridMovies(cfData, cbData, callback);

	}
	
	private void sendCBLData() {
		if(hrsSvc==null) hrsSvc = GWT.create(HRSService.class);
		CBParametersL data=new CBParametersL();
		
		try {
			data=new CBParametersL(this.cblView.getTextboxWindowInitialDate().getText(),
					this.cblView.getTextboxWindowFinalDate().getText(),
					this.cblView.getTextboxWaitTime(),
					this.cblView.getListboxMinTermFrequency(),
					this.cblView.getListboxMinDocFrequency(),
					this.cblView.getListBoxMinWordLen(),
					Integer.parseInt(this.cblView.getTextboxUser()));
			this.cblView.getHtmlPrecisionResult().setHTML("<strong>Calculando...</strong>");
			this.cblView.getHtmlRecallResult().setHTML("<strong>Calculando...</strong>");
			this.cblView.getHtmlResultListResult().setHTML("<strong>Sin resultados</strong>");
		} catch (NumberFormatException nfe) {}
		
		AsyncCallback<CBResultL> callback = new AsyncCallback<CBResultL>() {
			public void onFailure(Throwable caught) {
				//Aqu� poner el llamado en caso de que salga mal
			}

			@Override
			public void onSuccess(CBResultL result) {
				//Aqu� poner el llamado en caso de que salga bien
				updateCBLResult(result);
			}
		};

		// Make the call to the stock price service.
		hrsSvc.initCBL(data, callback);
	}

	private void updateCBLResult(CBResultL result) {
		this.cblView.getHtmlPrecisionResult().setText("" + result.getPrecision());
		this.cblView.getHtmlRecallResult().setText("" + result.getRecall());
		String text="";
		int tam=10;
		
		tam=50;
		String[] resultText=result.getOtherDocs();
		String[] resultText1=result.getDataInfo();
		if(tam>resultText.length) {
			tam=resultText.length;
		}
		
		text=text + "</ul><hr/>Mostrando " + tam + " pel\u00EDculas recomendadas<ul>";
		text=text + "<table><hr><td>Pel\u00EDcula</td><td>Resumen</td></hr>";
		for(int i=0;i<tam;i++) {
			text=text + "<li><strong>" + resultText1[i] + "</strong>: " + resultText[i]+"</li>";
		}
		text=text + "</table>";
		
		text = text + "</ul>";
		this.cblView.getHtmlResultListResult().setHTML(text);
	}
}
