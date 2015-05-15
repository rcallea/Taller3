package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class CBResultL implements Serializable {

	private String[] data;
	private String[] dataInfo;
	private String[] userDocs;
	private String[] otherDocs;
	private float precision;
	private float recall;

	/**
	 * 
	 */
	public CBResultL() {
		super();
	}

	/**
	 * @param data
	 * @param dataInfo
	 * @param userDocs
	 * @param otherDocs
	 * @param precision
	 * @param recall
	 */
	public CBResultL(String[] data, String[] dataInfo, String[] userDocs,
			String[] otherDocs, float precision, float recall) {
		super();
		this.data = data;
		this.dataInfo = dataInfo;
		this.userDocs = userDocs;
		this.otherDocs = otherDocs;
		this.precision = precision;
		this.recall = recall;
	}

	/**
	 * @return the data
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String[] data) {
		this.data = data;
	}

	/**
	 * @return the dataInfo
	 */
	public String[] getDataInfo() {
		return dataInfo;
	}

	/**
	 * @param dataInfo the dataInfo to set
	 */
	public void setDataInfo(String[] dataInfo) {
		this.dataInfo = dataInfo;
	}

	/**
	 * @return the userDocs
	 */
	public String[] getUserDocs() {
		return userDocs;
	}

	/**
	 * @param userDocs the userDocs to set
	 */
	public void setUserDocs(String[] userDocs) {
		this.userDocs = userDocs;
	}

	/**
	 * @return the otherDocs
	 */
	public String[] getOtherDocs() {
		return otherDocs;
	}

	/**
	 * @param otherDocs the otherDocs to set
	 */
	public void setOtherDocs(String[] otherDocs) {
		this.otherDocs = otherDocs;
	}

	/**
	 * @return the precision
	 */
	public float getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(float precision) {
		this.precision = precision;
	}

	/**
	 * @return the recall
	 */
	public float getRecall() {
		return recall;
	}

	/**
	 * @param recall the recall to set
	 */
	public void setRecall(float recall) {
		this.recall = recall;
	}

}
