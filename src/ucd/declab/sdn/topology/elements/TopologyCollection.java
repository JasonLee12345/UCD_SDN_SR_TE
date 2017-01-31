package ucd.declab.sdn.topology.elements;

import java.util.ArrayList;

public class TopologyCollection {

	private ArrayList<TopologyNode> nodes;
	private ArrayList<TopologyEdge> edges;
	
	public TopologyCollection() {
		this.nodes = new ArrayList<TopologyNode>();
		this.edges = new ArrayList<TopologyEdge>();
	}
	
	public TopologyCollection(ArrayList<TopologyNode> nodes, ArrayList<TopologyEdge> edges) {
		this.nodes = nodes;
		this.edges = edges;
	}
	
	public void setNodes(ArrayList<TopologyNode> nodes) { this.nodes = nodes; }
	public void setEdges(ArrayList<TopologyEdge> edges) { this.edges = edges; }
	
	public ArrayList<TopologyNode> getNodes() { return this.nodes; }
	public ArrayList<TopologyEdge> getEdges() { return this.edges; }
}
