package co.edu.uniandes.taller3.client;

import java.util.List;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.ContentParameters;
import co.edu.uniandes.taller3.shared.CFResult;

import co.edu.uniandes.taller3.shared.ContentResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HRSServiceAsync {
	//Ojo, lo que va dentro del <> del asyncCallBack debe ser la clase de retorno del método del service
	//void sendUser(CFParameters data, AsyncCallback<String[]> callback);
	void getContentBusiness(ContentParameters data, String[] listCF, AsyncCallback<List<ContentResult>> callback);
	void getHybridBusiness(CFParameters cfData, CBParametersL cbData, CBParametersL cbData2, ContentParameters contentData, AsyncCallback<List<ContentResult>> callback);
	void getInformationBusiness(String businessId, AsyncCallback<String[]> callback);

	void initCF(CFParameters data, AsyncCallback<CFResult> callback);
	void initCBL(CBParametersL data, AsyncCallback<CBResultL> callback);
	void initCBL2(CBParametersL data, AsyncCallback<CBResultL> callback);
}
