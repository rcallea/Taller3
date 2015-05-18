package co.edu.uniandes.taller3.client;

import java.util.List;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.CFResult;
import co.edu.uniandes.taller3.shared.Movie;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HRSServiceAsync {
	
	void getOntologyMovies(int userId, String fechaInicial,	String fechaFinal, AsyncCallback<List<Movie>> callback);
	void getHybridMovies(CFParameters cfData, CBParametersL cbData, AsyncCallback<String[]> callback);
	void initCF(CFParameters data, AsyncCallback<CFResult> callback);
	void initCBL(CBParametersL data, AsyncCallback<CBResultL> callback);
	
}
