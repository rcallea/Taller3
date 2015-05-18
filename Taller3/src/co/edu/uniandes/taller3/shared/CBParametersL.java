package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class CBParametersL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 868367783437745573L;
	private String dateI="2014/01/01";
	private String dateF="2014/12/31";
	private float waitTime=0;
	private int minTermFrequency=0;
	private int minDocFrequency=0;
	private int minWordLen=0;
	private int user=0;
	private boolean conHibrido = false;
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
	public CBParametersL(String dateI, String dateF, float waitTime,
			int minTermFrequency, int minDocFrequency, int minWordLen, int user, boolean conHibrido) {
		super();
		this.dateI = dateI;
		this.dateF = dateF;
		this.waitTime = waitTime;
		this.minTermFrequency = minTermFrequency;
		this.minDocFrequency = minDocFrequency;
		this.minWordLen = minWordLen;
		this.user = user;
		this.conHibrido = conHibrido;
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
