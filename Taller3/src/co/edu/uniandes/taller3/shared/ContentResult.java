package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class ContentResult implements Serializable {
	
	private String idBusiness;
	private String name;
	private String[] keyWordsName;
	private String[] keyWordsCategories;
	private String[] keyWordsAttributes;
	private String[] keyWordsComments;
	private String[] keyWordsRequest;
	private double similarityName;
	private double similarityCategories;
	private double similarityAttributes;
	private double similarityComments;
	private double similarityTotal;
	/**
	 * 
	 */
	public ContentResult() {
		super();
	}
	
	public ContentResult(String idBusiness, String name, String[] keyWordsName,	String[] keyWordsCategories, String[] keyWordsAttributes,
	String[] keyWordsComments, String[] keyWordsRequest, double similarityName,	double similarityCategories,	double similarityAttributes,	double similarityComments,double similarityTotal) {
		super();
		
		
		this.idBusiness = idBusiness;
		this.name = name;
		this.keyWordsName = keyWordsName;
		this.keyWordsCategories = keyWordsCategories;
		this.keyWordsAttributes = keyWordsAttributes;
		this.keyWordsComments = keyWordsComments; 
		this.keyWordsRequest = keyWordsRequest;
		this.similarityName = similarityName;
		this.similarityCategories = similarityCategories;
		this.similarityAttributes = similarityAttributes;
		this.similarityComments = similarityComments;
		this.similarityTotal = similarityTotal;
	}
	
	
	public String getIdBusiness() {
		return idBusiness;
	}
	public void setIdBusiness(String isBusiness) {
		this.idBusiness = isBusiness;
	}
	public String[] getKeyWordsName() {
		return keyWordsName;
	}
	public void setKeyWordsName(String[] keyWordsName) {
		this.keyWordsName = keyWordsName;
	}
	public String[] getKeyWordsCategories() {
		return keyWordsCategories;
	}
	public void setKeyWordsCategories(String[] keyWordsCategories) {
		this.keyWordsCategories = keyWordsCategories;
	}
	public String[] getKeyWordsAttributes() {
		return keyWordsAttributes;
	}
	public void setKeyWordsAttributes(String[] keyWordsAttributes) {
		this.keyWordsAttributes = keyWordsAttributes;
	}
	public String[] getKeyWordsComments() {
		return keyWordsComments;
	}
	public void setKeyWordsComments(String[] keyWordsComments) {
		this.keyWordsComments = keyWordsComments;
	}
	public double getSimilarityName() {
		return similarityName;
	}
	public void setSimilarityName(double similarityName) {
		this.similarityName = similarityName;
	}
	public double getSimilarityCategories() {
		return similarityCategories;
	}
	public void setSimilarityCategories(double similarityCategories) {
		this.similarityCategories = similarityCategories;
	}
	public double getSimilarityAttributes() {
		return similarityAttributes;
	}
	public void setSimilarityAttributes(double similarityAttributes) {
		this.similarityAttributes = similarityAttributes;
	}
	public double getSimilarityComments() {
		return similarityComments;
	}
	public void setSimilarityComments(double similarityComments) {
		this.similarityComments = similarityComments;
	}
	public double getSimilarityTotal() {
		return similarityTotal;
	}
	public void setSimilarityTotal(double similarityTotal) {
		this.similarityTotal = similarityTotal;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getKeyWordsRequest() {
		return keyWordsRequest;
	}
	public void setKeyWordsRequest(String[] keyWordsRequest) {
		this.keyWordsRequest = keyWordsRequest;
	}
}
