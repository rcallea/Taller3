package co.edu.uniandes.taller3.shared;

import java.io.Serializable;

public class ContentParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String city="";
	private String category="";
	private String description="";
	private String day = "";
	private String hour = "";
	private double weightName = 0;
	private double weightCategories = 0;
	private double weightAttributes = 0;
	private double weightComments = 0;
	
	/**
	 * @param city
	 * @param category
	 * @param description
	 */
	public ContentParameters()
	{}
	
	public ContentParameters(String city, String category, String description, String day, String hour, double weightName, double weightCategories, double weightAttributes, double weightComments ) {
		super();
		this.city = city;
		this.category = category;
		this.description = description;
		this.day = day;
		this.hour = hour;
		this.weightName = weightName;
		this.weightCategories = weightCategories;
		this.weightAttributes = weightAttributes;
		this.weightComments = weightComments;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
	public double getWeightName() {
		return weightName;
	}

	public void setWeightName(double weightName) {
		this.weightName = weightName;
	}

	public double getWeightCategories() {
		return weightCategories;
	}

	public void setWeightCategories(double weightCategories) {
		this.weightCategories = weightCategories;
	}

	public double getWeightAttributes() {
		return weightAttributes;
	}

	public void setWeightAttributes(double weightAttributes) {
		this.weightAttributes = weightAttributes;
	}

	public double getWeightComments() {
		return weightComments;
	}

	public void setWeightComments(double weightComments) {
		this.weightComments = weightComments;
	}
}
