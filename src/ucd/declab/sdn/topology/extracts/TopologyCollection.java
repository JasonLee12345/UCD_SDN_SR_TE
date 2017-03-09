package ucd.declab.sdn.topology.extracts;

import java.util.ArrayList;

public class TopologyCollection {

	private ArrayList<Vertex> vertices;
	private ArrayList<Link> links;
	
	public TopologyCollection() {
		this.vertices = new ArrayList<Vertex>();
		this.links = new ArrayList<Link>();
	}
	
	public TopologyCollection(ArrayList<Vertex> vertices, ArrayList<Link> links) {
		this.vertices = vertices;
		this.links = links;
	}
	
	public void setVertices(ArrayList<Vertex> vertices) { this.vertices = vertices; }
	public void setLinks(ArrayList<Link> links) { this.links = links; }
	
	public ArrayList<Vertex> getVertices() { return this.vertices; }
	public ArrayList<Link> getLinks() { return this.links; }
}
