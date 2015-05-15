package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class CFResult implements Serializable {
	
	private String[] data;
	private String[] dataInfo;
	private float precision;
	private float recall;
	/**
	 * 
	 */
	public CFResult() {
		super();
	}
	/**
	 * @param data
	 * @param dataInfo
	 * @param precision
	 * @param recall
	 */
	public CFResult(String[] data, String[] dataInfo, float precision,
			float recall) {
		super();
		this.data = data;
		this.dataInfo = dataInfo;
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
