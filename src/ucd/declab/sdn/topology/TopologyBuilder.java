package ucd.declab.sdn.topology;

import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;

import ucd.declab.sdn.topology.elements.*;
import ucd.declab.sdn.topology.extracts.*;

public class TopologyBuilder {
	
	private Topology topology;
	private TopologyCollection topoCollection;
	
	public TopologyBuilder (ArrayList<Object> rawNodes, ArrayList<Object> rawLinks) {
		ArrayList<TopologyNode> nodes = TopologyUtils.extractNodesInfoFromRawNodes(rawNodes);
		ArrayList<TopologyEdge> edges = TopologyUtils.extractEdgesInfoFromRawLinks(rawLinks);
		this.topology = new Topology(nodes, edges);
		
		ArrayList<Vertex> vertices = TopologyUtils.extractVerticesFromTopology(this.topology);
		ArrayList<Link> links = TopologyUtils.extractLinksFromTopology(this.topology);
		this.topoCollection = new TopologyCollection(vertices, links);
	}
	
	public Topology getTopology() { return this.topology; }
	public TopologyCollection getTopologyCollection() { return this.topoCollection; }
}
