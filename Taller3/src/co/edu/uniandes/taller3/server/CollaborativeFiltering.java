package co.edu.uniandes.taller3.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.recommender101.data.DataModel;
import org.recommender101.data.Rating;
import org.recommender101.recommender.baseline.NearestNeighbors;

import co.edu.uniandes.taller3.shared.CFParameters;
import co.edu.uniandes.taller3.shared.CFResult;

public class CollaborativeFiltering {
	private float precision=0;
	private float recall=0;

	public CFResult initCF(CFParameters data) {
		CFResult ret=new CFResult();

		// Simple test method.
		System.out.println("Iniciando CF: " + new Date());
		try {
			DataModel dm = new DataModel();
			DefaultDataLoader loader = new DefaultDataLoader();
			loader.setMinNumberOfRatingsPerUser(data.getRatings());
			loader.setFromDate(data.getDateI());
			loader.setToDate(data.getDateF());
			System.out.println("Cargando datamodel: " + new Date());
			loader.loadData(dm);
			NearestNeighbors rec = new NearestNeighbors();
			rec.setDataModel(dm);
			rec.setCosineSimilarity(data.getRecommenderType());
			rec.setItemBased(data.getMeasureType());
			rec.setNeighbors(data.getNeighbors());

			System.out.println("Iniciando recomendador CF: " + new Date());
			rec.init();
			
			System.out.println("Calculando recomendaciones CF: " + new Date());
			ArrayList<Integer> recommendations=(ArrayList<Integer>)rec.recommendItems(data.getUser());
			
			System.out.println("Adicionando resultados a la lista CF: " + new Date());

			int maxDataSize=50;
			String[] retListSearch;
			if(recommendations.size()<=maxDataSize) {
				maxDataSize=recommendations.size();
			}
			
			String[] retList=new String[maxDataSize];
			String[] retListData;

			for(int i=0;i<maxDataSize;i++) {
				retList[i]=""  + recommendations.get(i);
			}
			
			retListData=loader.getMovieInfo(retList);

			System.out.println("Calculando precision y recall a CF: " + new Date());
			this.precisionRecallCF(retList, loader.getNextMovies(data.getUser()));
			ret=new CFResult(retList, retListData, this.precision, this.recall);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Fin de CF");

		return(ret);
	}
	
	public void precisionRecallCF(String[] recommended, String[] futureLoaded) {
		this.precision=0;
		this.recall=0;
		System.out.println("Iniciando mediciones");

		System.out.println("Iniciando comparación para mediciones");
		int found=0;
		for(int i=0;i<recommended.length;i++) {
			for(int j=0;j<futureLoaded.length;j++) {
				if(recommended[i].equals(futureLoaded[j])) {
					j=futureLoaded.length;
					found++;
				}
			}
		}

		this.precision = ((float)found)/((float)(found + recommended.length));
		this.recall = ((float)found)/((float)(found + futureLoaded.length));

//		System.out.println("Fin de Mediciones");
	}	
	
}
