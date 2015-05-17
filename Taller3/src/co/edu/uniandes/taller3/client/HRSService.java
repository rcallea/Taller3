package co.edu.uniandes.taller3.client;

import java.util.List;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.ContentParameters;
import co.edu.uniandes.taller3.shared.CFResult;



import co.edu.uniandes.taller3.shared.ContentResult;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("hrsRemoteService")
public interface HRSService extends RemoteService{

	//String[] sendUser(CFParameters data);
	List<ContentResult> getContentBusiness(ContentParameters data, String[] listCF);
	List<ContentResult> getHybridBusiness(CFParameters cfData, CBParametersL cbData, ContentParameters contentData);
	String[] getInformationBusiness(String businessId);

	CFResult initCF(CFParameters data);
	CBResultL initCBL(CBParametersL data);
}
