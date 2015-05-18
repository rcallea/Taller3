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
import co.edu.uniandes.taller3.shared.JaccardCoefficient;
import co.edu.uniandes.taller3.shared.ValueComparator;
import co.edu.uniandes.taller3.shared.CFResult;
import com.google.gwt.thirdparty.javascript.jscomp.Result;
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
			ContentBasedL cbl=new ContentBasedL();
			cbl.findSilimar(data);
			return(cbl.getCblr());
			
		} catch (IOException e) {e.printStackTrace();}
		return(new CBResultL());
	}

	public List<String> CFCBMix(CFResult cfResult, CBResultL cbResult) {
		List<String> listTotal = new ArrayList();
		String[] cb = cbResult.getData();
		String[] cf = cfResult.getData();
		
		for(int i = 0; i < cb.length; i++) {
			listTotal.add(cb[i]);
		}
		
		for(int i = 0; i < cf.length; i++) {
			if(!listTotal.contains(cf[i]))
				listTotal.add(cf[i]);
		}
		return listTotal;
	}

	public String[] getHybridMovies(CFParameters cfData, CBParametersL cbData) {
		
		/*CFResult cfResult = new CFResult();
		CBResultL cbResult = new CBResultL();
		
		try {
			
			System.out.print("Paso 1: Colaborativo");
			cfResult = new CollaborativeFiltering().initCF(cfData);
			System.out.print("Fin Paso 1: Colaborativo. Total: " + cfResult.getData().length);
			
			System.out.print("Paso 2: Contenido");
			ContentBasedL cbl = new ContentBasedL();
			cbl.findSilimar(cbData);
			cbResult = cbl.getCblr();
			System.out.print("Fin Paso 2: Contenido. Total: " + cbResult.getData().length);
		
			List<String> lista = new ArrayList();
			lista = this.CFCBMix(cfResult, cbResult);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return cfResult.getDataInfo();*/
		
		return getMoviesDbpedia();
		
		
	}
	
	public String[] getMoviesDbpedia() {
		
		List<String> moviesUser = new ArrayList<String>();
		MySQLQuery query = new MySQLQuery();
		moviesUser = query.getRatingsUser("1", "2005-04-02", "2005-05-02");
		
		List<Movie> moviesFinal = new ArrayList<Movie>();
		MongoDB mongo = new MongoDB();
		moviesFinal = mongo.EncontrarItemsSimilares(moviesUser);
		
		
		
		return null;
	}
}
