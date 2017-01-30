package ucd.declab.sdn.topology.elements;

public class TopologyEdge {
	
	private String v1;
	private String v2;
	private TopologyEdgeDetails edgeDetails;
	
	public TopologyEdge() {
		this.v1 = "";
		this.v2 = "";
		this.edgeDetails = new TopologyEdgeDetails();
	}
	
	public TopologyEdge(String v1, String v2) {
		this.v1 = v1;
		this.v2 = v2;
		this.edgeDetails = new TopologyEdgeDetails();
	}
	
	public TopologyEdge(String v1, String v2, TopologyEdgeDetails edgeDetails) {
		this.v1 = v1;
		this.v2 = v2;
		this.edgeDetails = edgeDetails;
	}
	
	public void setV1(String v1) { this.v1 = v1; }
	public void setV2(String v2) { this.v2 = v2; }
	public void setV1V2(String v1, String v2) { this.v1 = v1; this.v2 = v2; }
	public void setTopologyEdgeDetails(TopologyEdgeDetails edgeDetails) { this.edgeDetails = edgeDetails; }
	
	public String getV1() { return this.v1; }
	public String getV2() { return this.v2; }
	public TopologyEdgeDetails getTopologyEdgeDetails() { return this.edgeDetails; }
}
