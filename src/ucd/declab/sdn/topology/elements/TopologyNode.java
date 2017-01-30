package ucd.declab.sdn.topology.elements;


public class TopologyNode {
	
	private String label;
	private TopologyNodeDetails nodeDetails;
	
	public TopologyNode() {
		this.label = "";
		this.nodeDetails = new TopologyNodeDetails();
	}
	
	public TopologyNode(String label) {
		this.label = label;
		this.nodeDetails = new TopologyNodeDetails();
	}
	
	public TopologyNode(String label, TopologyNodeDetails nodeDetails) {
		this.label = label;
		this.nodeDetails = nodeDetails;
	}
	
	public void setLabel(String label) { this.label = label; }
	public void setTopologyNodeDetails (TopologyNodeDetails nodeDetails) { this.nodeDetails = nodeDetails; }
	
	public String getLabel() { return this.label; }
	public TopologyNodeDetails getTopologyNodeDetails() { return this.nodeDetails; }
}
