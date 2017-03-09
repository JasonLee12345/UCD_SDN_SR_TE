package ucd.declab.sdn.topology.extracts;

public class Link {

	private String v1;
	private String v2;
	private String label;
	private String type;
	private double capacity;
	private double load;
	
	public Link() {
		this.v1 = "";
		this.v2 = "";
		this.label = "";
		this.type = "";
		this.capacity = 1;
		this.load = 0;
	}
	
	public Link(String vertex1, String vertex2, String edgeLabel, String edgeType) {
		this.v1 = vertex1;
		this.v2 = vertex2;
		this.label = edgeLabel;
		this.type = edgeType;
		this.capacity = 1;
		this.load = 0;
	}
	
	public Link(String vertex1, String vertex2, String edgeLabel, String edgeType, double edgeCapacity, double edgeLoad) {
		this(vertex1, vertex2, edgeLabel, edgeType);
		this.capacity = edgeCapacity;
		this.load = edgeLoad;
	}

	public void setV1(String vertex1) { this.v1 = vertex1; }
	public void setV2(String vertex2) { this.v2 = vertex2; }
	public void setLabel(String edgeLabel) { this.label = edgeLabel; }
	public void setType(String edgeType) { this.type = edgeType; }
	public void setCapacity(double edgeCapacity) { this.capacity = edgeCapacity; }
	public void setLoad(double edgeLoad) { this.load = edgeLoad; }
	
	public String getV1() { return this.v1; }
	public String getV2() { return this.v2; }
	public String getLabel() { return this.label; }
	public String getType() { return this.type; }
	public double getCapacity() { return capacity; }
	public double getLoad() { return load; }
}
