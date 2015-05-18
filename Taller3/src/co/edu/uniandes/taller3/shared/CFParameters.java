package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class CFParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dateI="2014/01/01";
	private String dateF="2014/12/31";
	private String neighbors="";
	private String ratings="";
	private String measureType="";
	private String recommenderType="";
	private int user=0;
	private boolean conHibrido = false; 
	
	public CFParameters() {
		this.dateI="2014/01/01";
		this.dateF="2014/12/31";
		this.neighbors = "10";
		this.ratings = "10";
		this.measureType = "true";
		this.recommenderType = "true";
		this.user = 1;
		this.conHibrido = false;
	}
	
	/**
	 * @param datasetSize
	 * @param neighbors
	 * @param ratings
	 * @param measureType
	 * @param recommenderType
	 * @param user
	 */
	public CFParameters(String dateI, String dateF, String neighbors, String ratings,
			String measureType, String recommenderType, int user, boolean conHibrido) {
		super();
		this.dateI=dateI;
		this.dateF=dateF;
		this.neighbors = neighbors;
		this.ratings = ratings;
		this.measureType = measureType;
		this.recommenderType = recommenderType;
		this.user = user;
		this.conHibrido = conHibrido;
	}


	/**
	 * @return the dateI
	 */
	public String getDateI() {
		return dateI;
	}

	/**
	 * @param dateI the dateI to set
	 */
	public void setDateI(String dateI) {
		this.dateI = dateI;
	}

	/**
	 * @return the dateF
	 */
	public String getDateF() {
		return dateF;
	}

	/**
	 * @param dateF the dateF to set
	 */
	public void setDateF(String dateF) {
		this.dateF = dateF;
	}

	/**
	 * @return the neighbors
	 */
	public String getNeighbors() {
		return neighbors;
	}
	/**
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(String neighbors) {
		this.neighbors = neighbors;
	}
	/**
	 * @return the ratings
	 */
	public String getRatings() {
		return ratings;
	}

	/**
	 * @param ratings the ratings to set
	 */
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}

	/**
	 * @return the measureType
	 */
	public String getMeasureType() {
		return measureType;
	}
	/**
	 * @param measureType the measureType to set
	 */
	public void setMeasureType(String measureType) {
		this.measureType = measureType;
	}
	/**
	 * @return the recommenderType
	 */
	public String getRecommenderType() {
		return recommenderType;
	}
	/**
	 * @param recommenderType the recommenderType to set
	 */
	public void setRecommenderType(String recommenderType) {
		this.recommenderType = recommenderType;
	}
	/**
	 * @return the user
	 */
	public int getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(int user) {
		this.user = user;
	}

	public boolean isConHibrido() {
		return conHibrido;
	}

	public void setConHibrido(boolean conHibrido) {
		this.conHibrido = conHibrido;
	}
	
}
