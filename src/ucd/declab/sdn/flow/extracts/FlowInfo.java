package ucd.declab.sdn.flow.extracts;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Path;

public class FlowInfo {

	public static final String OUT = "out";
	public static final String IN = "in";
	
	private String id;
	private String nodeSource;
	private String nodeDestination;
	private double bandwidth;
	private Path path = null;
	private String relationID;
	private String relationType;
	
	public FlowInfo() {
		this.id = "";
		this.nodeSource = "";
		this.nodeDestination = "";
		this.bandwidth = 0.0;
		this.relationID = "";
		this.relationType = "";
	}
	
	public FlowInfo(String flowID, String nodeSrc, String nodeDest, double bandwidth) {
		this.id = flowID;
		this.nodeSource = nodeSrc;
		this.nodeDestination = nodeDest;
		this.bandwidth = bandwidth;
		this.relationID = "";
		this.relationType = "";
	}
	
	public FlowInfo(String flowID, String nodeSrc, String nodeDest, double bandwidth, Path p) {
		this(flowID, nodeSrc, nodeDest, bandwidth);
		this.path = p;
	}
	
	public void setId(String flowID) { this.id = flowID; }
	public void setNodeSource(String nodeSrc) { this.nodeSource = nodeSrc; }
	public void setNodeDestination(String nodeDest) { this.nodeDestination = nodeDest; }
	public void setBandwidth(double bandwidth) { this.bandwidth = bandwidth; }
	public void setPath(Path p) { this.path = p; }
	public void setRelationID(String relationID) { this.relationID = relationID; }
	public void setRelationType(String relationType) { this.relationType = relationType; }

	public String getId() { return this.id; }
	public String getNodeSource() { return this.nodeSource; }
	public String getNodeDestination() { return this.nodeDestination; }
	public double getBandwidth() { return this.bandwidth; }
	public Path getPath() { return this.path; }
	public String getRelationID() { return this.relationID; }
	public String getRelationType() { return this.relationType; }
	
	public void addEdgeToPath(Edge e) { this.path.add(e); }
	
	public String toString() {
		return "ID: " + this.id + ": SRC: " + this.nodeSource + " , DST: " + this.nodeDestination + " , BW: " + this.bandwidth;
	}
}
