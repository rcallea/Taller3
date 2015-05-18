package co.edu.uniandes.taller3.client;

import java.util.List;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.CFResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("hrsRemoteService")
public interface HRSService extends RemoteService{

	String[] getHybridMovies(CFParameters cfData, CBParametersL cbData);
	CFResult initCF(CFParameters data);
	CBResultL initCBL(CBParametersL data);
}
