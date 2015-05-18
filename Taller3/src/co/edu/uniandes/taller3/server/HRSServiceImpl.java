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
import co.edu.uniandes.taller3.shared.Movie;
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

	public String[] CFCBMix(CFResult cfResult, CBResultL cbResult) {
		String[] ret={};
		String[] cb=cbResult.getData();
		String[] cf=cfResult.getData();
		ArrayList<String> al=new ArrayList<String>();

		int length=cf.length;
		if(length<cb.length) {
			length=cb.length;
		}
		
		for(int i=0;i<length;i++) {
			if(i<cb.length) {
				al.add(cb[i]);
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

	public String[] getHybridMovies(CFParameters cfData, CBParametersL cbData) {
		
		CFResult cfResult = new CFResult();
		CBResultL cbResult = new CBResultL();
		String[] listaMixed = null;
		String[] listaFinal = null;
		try {
			
			System.out.print("-----------------Inicio Hibrido-------------------");
			
			System.out.print("Paso 1: Colaborativo");
			cfResult = new CollaborativeFiltering().initCF(cfData);
			System.out.print("Fin Paso 1: Colaborativo. Total: " + cfResult.getData().length);
			
			System.out.print("Paso 2: Contenido");
			ContentBasedL cbl = new ContentBasedL();
			cbl.findSilimar(cbData);
			cbResult = cbl.getCblr();
			System.out.print("Fin Paso 2: Contenido. Total: " + cbResult.getData().length);
		
			listaMixed = this.CFCBMix(cfResult, cbResult);
			co.edu.uniandes.taller3.server.DefaultDataLoader dl = new co.edu.uniandes.taller3.server.DefaultDataLoader();
			
			listaFinal = new String[listaMixed.length]; 
			listaFinal = dl.getMovieInfo(listaMixed);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listaFinal;
		
	}
	
	public List<Movie> getOntologyMovies(int userId, String fechaInicial,	String fechaFinal) {
		
		System.out.println("Inicio Ontologia");
		List<String> moviesUser = new ArrayList<String>();
		MySQLQuery query = new MySQLQuery();
		moviesUser = query.getRatingsUser(String.valueOf(userId) , fechaInicial, fechaFinal);
		System.out.println("Fin Ontologia");
		MongoDB mongo = new MongoDB();
		return mongo.EncontrarItemsSimilares(moviesUser);
		
		
	}
}
