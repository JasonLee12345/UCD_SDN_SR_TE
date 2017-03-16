package ucd.declab.sdn.topology.elements;

import com.google.gson.annotations.SerializedName;

public class TopologyNodeDetails {

	private String type;
	private String country;
	private String city;
	@SerializedName("x") private double longitude;
	@SerializedName("y") private double latitude;

	public TopologyNodeDetails() {
		//this.type = "";
		//this.country = "";
		//this.city = "";
		//this.longitude = 0.0;
		//this.latitude = 0.0;
	}
	
	public TopologyNodeDetails(String type) {
		this();
		this.type = type;
	}
	
	public TopologyNodeDetails(String type, String country, String city) {
		this.type = type;
		this.country = country;
		this.city = city;
		this.longitude = 0.0;
		this.latitude = 0.0;
	}
	
	public TopologyNodeDetails(String type, String country, String city, double longitude, double latitude) {
		this.type = type;
		this.country = country;
		this.city = city;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public void setType(String type) { this.type = type; }
	public void setCountry(String country) { this.country = country; }
	public void setCity(String city) { this.city = city; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	
	public String getType() { return this.type; }
	public String getCountry() { return this.country; }
	public String getCity() { return this.city; }
	public double getLongitude() { return this.longitude; }
	public double getLatitude() { return this.latitude; }
}
