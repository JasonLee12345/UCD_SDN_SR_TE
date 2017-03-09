package ucd.declab.sdn.topology.extracts;

public class Vertex {

	private String label;
	private String type;
	private double x;
	private double y;
	
	public Vertex() {
		this.label = "";
		this.type = "";
		this.x = 0.0;
		this.y = 0.0;
	}
	
	public Vertex(String nodeLabel, String nodeType) {
		this.label = nodeLabel;
		this.type = nodeType;
		this.x = 0.0;
		this.y = 0.0;
	}

	public void setLabel(String nodeLabel) { this.label = nodeLabel; }
	public void setType(String nodeType) { this.type = nodeType; }
	public void setX(double posX) { this.x = posX; }
	public void setY(double posY) { this.y = posY; }
	
	public String getLabel() { return this.label; }
	public String getType() { return this.type; }
	public double getX() { return x; }
	public double getY() { return y; }
}
