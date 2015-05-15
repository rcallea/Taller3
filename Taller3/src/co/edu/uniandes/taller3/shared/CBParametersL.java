package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class CBParametersL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 868367783437745573L;
	private String datasetSize="";
	private float waitTime=0;
	private int minTermFrequency=0;
	private int minDocFrequency=0;
	private int minWordLen=0;
	private String user="";
	/**
	 * 
	 */
	public CBParametersL() {
		super();
	}
	/**
	 * @param datasetSize
	 * @param waitTime
	 * @param minTermFrequency
	 * @param minDocFrequency
	 * @param minWordLen
	 * @param user
	 */
	public CBParametersL(String datasetSize, float waitTime,
			int minTermFrequency, int minDocFrequency, int minWordLen, String user) {
		super();
		this.datasetSize = datasetSize;
		this.waitTime = waitTime;
		this.minTermFrequency = minTermFrequency;
		this.minDocFrequency = minDocFrequency;
		this.minWordLen = minWordLen;
		this.user = user;
	}
	/**
	 * @return the datasetSize
	 */
	public String getDatasetSize() {
		return datasetSize;
	}
	/**
	 * @param datasetSize the datasetSize to set
	 */
	public void setDatasetSize(String datasetSize) {
		this.datasetSize = datasetSize;
	}
	/**
	 * @return the waitTime
	 */
	public float getWaitTime() {
		return waitTime;
	}
	/**
	 * @param waitTime the waitTime to set
	 */
	public void setWaitTime(float waitTime) {
		this.waitTime = waitTime;
	}
	/**
	 * @return the minTermFrequency
	 */
	public int getMinTermFrequency() {
		return minTermFrequency;
	}
	/**
	 * @param minTermFrequency the minTermFrequency to set
	 */
	public void setMinTermFrequency(int minTermFrequency) {
		this.minTermFrequency = minTermFrequency;
	}
	/**
	 * @return the minDocFrequency
	 */
	public int getMinDocFrequency() {
		return minDocFrequency;
	}
	/**
	 * @param minDocFrequency the minDocFrequency to set
	 */
	public void setMinDocFrequency(int minDocFrequency) {
		this.minDocFrequency = minDocFrequency;
	}
	/**
	 * @return the minWordLen
	 */
	public int getMinWordLen() {
		return minWordLen;
	}
	/**
	 * @param minWordLen the minWordLen to set
	 */
	public void setMinWordLen(int minWordLen) {
		this.minWordLen = minWordLen;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
}
