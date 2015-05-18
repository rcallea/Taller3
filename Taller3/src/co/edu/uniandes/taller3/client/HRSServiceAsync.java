package co.edu.uniandes.taller3.client;

import java.util.List;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.CFResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HRSServiceAsync {
	
	void getHybridMovies(CFParameters cfData, CBParametersL cbData, AsyncCallback<String[]> callback);
	void initCF(CFParameters data, AsyncCallback<CFResult> callback);
	void initCBL(CBParametersL data, AsyncCallback<CBResultL> callback);
	
}
