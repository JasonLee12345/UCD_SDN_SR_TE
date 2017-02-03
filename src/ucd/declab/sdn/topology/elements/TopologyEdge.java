package ucd.declab.sdn.topology.elements;

public class TopologyEdge {
	
	private String src;
	private String dst;
	private TopologyEdgeDetails edgeDetails;
	
	public TopologyEdge() {
		this.src = "";
		this.dst = "";
		this.edgeDetails = new TopologyEdgeDetails();
	}
	
	public TopologyEdge(String src, String dst) {
		this.src = src;
		this.dst = dst;
		this.edgeDetails = new TopologyEdgeDetails();
	}
	
	public TopologyEdge(String src, String dst, TopologyEdgeDetails edgeDetails) {
		this.src = src;
		this.dst = dst;
		this.edgeDetails = edgeDetails;
	}
	
	public void setSrc(String src) { this.src = src; }
	public void setDst(String dst) { this.dst = dst; }
	public void setSrcDst(String src, String dst) { this.src = src; this.dst = dst; }
	public void setTopologyEdgeDetails(TopologyEdgeDetails edgeDetails) { this.edgeDetails = edgeDetails; }
	
	public String getSrc() { return this.src; }
	public String getDst() { return this.dst; }
	public TopologyEdgeDetails getTopologyEdgeDetails() { return this.edgeDetails; }
}
