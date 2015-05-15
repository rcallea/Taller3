package co.edu.uniandes.taller3.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;

import org.recommender101.data.DataModel;
import org.recommender101.data.DefaultDataLoader;
import org.recommender101.recommender.baseline.NearestNeighbors;

import co.edu.uniandes.taller3.client.HRSService;
import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;
import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.ConnectionDB;
import co.edu.uniandes.taller3.shared.ContentParameters;
import co.edu.uniandes.taller3.shared.ContentResult;
import co.edu.uniandes.taller3.shared.JaccardCoefficient;
import co.edu.uniandes.taller3.shared.ValueComparator;
import co.edu.uniandes.taller3.shared.CFResult;




import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HRSServiceImpl extends RemoteServiceServlet implements HRSService {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2112585968730455525L;

	@Override
	public CFResult initCF(CFParameters data) {
		return(new CollaborativeFiltering().initCF(data)); 
	}

	@Override
	public CBResultL initCBL(CBParametersL data) {
		try {
			return(new ContentBasedL().initCB(data));
		} catch (IOException e) {}
		return(new CBResultL());
	}

	@Override
	public CBResultL initCBL2(CBParametersL data) {
		try {
			return(new ContentBasedL2().initCB(data));
		} catch (IOException e) {}
		return(new CBResultL());
	}

	public List<ContentResult> getContentBusiness(ContentParameters data, String[] listCF)
	{
		System.out.println("Iniciando recomendador conocimiento");
		List<ContentResult> arrResult = new ArrayList<ContentResult>();
		HashMap<String,ContentResult> arrResultAux = new HashMap<String, ContentResult>();
		
		JaccardCoefficient jc = new JaccardCoefficient();
		
		String[] arrReq = ConnectionDB.ProcesarRequerimiento(data.getDescription());
		
		Map<String,List<String[]>> arrCompare = ConnectionDB.getKeyWordsNegociosComparar(data.getCategory(), data.getCity(), data.getDay(), data.getHour(), listCF);
		
		System.out.println("Lista seleccionada "+ arrCompare.size());
		double weightName = data.getWeightName();
		double weightCategories = data.getWeightCategories();
		double weightAttributes = data.getWeightAttributes();
		double weightComments = data.getWeightComments();
		double similarityTotal = 0;
	
		Map<String, Double> arrSimilarity = new HashMap<String, Double>();
		ContentResult contResult = new ContentResult();
		
		for (String business : arrCompare.keySet()) {
			contResult = new ContentResult();
			List<String[]> lst = arrCompare.get(business);
			
			double similarityName = jc.similarity(arrReq, lst.get(0));
			double similarityCategories = jc.similarity(arrReq, lst.get(1));
			double similarityAttributes = jc.similarity(arrReq, lst.get(2));
			double similarityComments = jc.similarity(arrReq, lst.get(3));
			
			similarityTotal = (similarityName * weightName) + (similarityCategories * weightCategories) + (similarityAttributes * weightAttributes) + (similarityComments * weightComments);
			
			arrSimilarity.put(business, similarityTotal);
			
			contResult.setIdBusiness(business);
			contResult.setKeyWordsName(lst.get(0));
			contResult.setKeyWordsCategories(lst.get(1));
			contResult.setKeyWordsAttributes(lst.get(2));
			contResult.setKeyWordsComments(lst.get(3));
			contResult.setKeyWordsRequest(arrReq);
			contResult.setSimilarityName(similarityName);
			contResult.setSimilarityCategories(similarityCategories);
			contResult.setSimilarityAttributes(similarityAttributes);
			contResult.setSimilarityComments(similarityComments);
			contResult.setSimilarityTotal(similarityTotal);
			arrResultAux.put(business, contResult);
		}
		
		ValueComparator bvc =  new ValueComparator(arrSimilarity);
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>(bvc);
		sorted_map.putAll(arrSimilarity);
		
		
		// saco las x primeras
		int i = 0;
		for (String idBusiness : sorted_map.keySet()) {
			ContentResult result = new ContentResult();
			result = arrResultAux.get(idBusiness);
			result.setName(ConnectionDB.getNombreNegocio(idBusiness));
			arrResult.add(result);
			i++;
			if (i == 20) {
				break;
			}
		}
		
		return arrResult; 
	}
	
	public String[] getInformationBusiness(String businessId) {
		return ConnectionDB.getInformationBusiness(businessId);
	}
	
	public String[] CFCBMix(CFResult cfResult, CBResultL cbResult, CBResultL cbResult2) {
		String[] ret={};
		String[] cb=cbResult.getData();
		String[] cb2=cbResult2.getData();
		String[] cf=cfResult.getData();
		ArrayList<String> al=new ArrayList<String>();

		int length=cf.length;
		if(length<cb.length) {
			length=cb.length;
		}
		if(length<cb2.length) {
			length=cb2.length;
		}
		
		for(int i=0;i<length;i++) {
			if(i<cb.length) {
				al.add(cb[i]);
			}
			if(i<cb2.length) {
				al.add(cb2[i]);
			}
			if(i<cf.length) {
				al.add(cf[i]);
			}
		}
		ret=new String[al.size()];
		for(int i=0;i<al.size();i++) {
			ret[i]=al.get(i);
		}
		return ret;
	}

	public List<ContentResult> getHybridBusiness(CFParameters cfData, CBParametersL cbData, CBParametersL cbData2, ContentParameters contentData) {
		String[] listCF={""};
		CFResult cfResult = new CollaborativeFiltering().initCF(cfData);
		System.out.println("Resultados de colaborativo: " + cfResult.getData().length);
		CBResultL cbResult=new CBResultL();
		try {
			cbResult = new ContentBasedL().initCB(cbData);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			cbResult.setData(listCF);
		}
		System.out.println("Resultados de contenido 1: " + cbResult.getData().length);
		CBResultL cbResult2=new CBResultL();;
		try {
			cbResult2 = new ContentBasedL2().initCB(cbData);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			cbResult2.setData(listCF);
		}
		System.out.println("Resultados de contenido 2: " + cbResult2.getData().length);

		listCF=this.CFCBMix(cfResult, cbResult, cbResult2);
		System.out.println(listCF.length);
		return getContentBusiness(contentData, listCF);		
	}
}
