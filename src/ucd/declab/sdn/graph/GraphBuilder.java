package ucd.declab.sdn.graph;

import java.util.ArrayList;

import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import ucd.declab.sdn.topology.extracts.*;
import ucd.declab.sdn.utils.Utilities;
import ucd.declab.sdn.flow.extracts.*;

public class GraphBuilder {

	private Graph graph;
	private ArrayList<Vertex> vertices;
	private ArrayList<Link> links;
	private FlowCollection flowCollection;
	
	private boolean directedEdge = true;
	
	public GraphBuilder(TopologyCollection topoCollection) {
		this.vertices = topoCollection.getVertices();
		this.links = topoCollection.getLinks();
	}
	
	public GraphBuilder(TopologyCollection topoCollection, FlowCollection flowCollection) {
		this.vertices = topoCollection.getVertices();
		this.links = topoCollection.getLinks();
		this.flowCollection = flowCollection;
	}
	
	public void buildGraphStreamTopology() {
		this.graph = new MultiGraph("Test_MultiGraph");
		
		for (Vertex vertex : this.vertices) {
			Node n = this.graph.addNode(vertex.getLabel());
			n.setAttribute(GraphUtils.ATTRIBUTE_NODE_TYPE, vertex.getType());
			n.setAttribute("x", vertex.getX());
			n.setAttribute("y", vertex.getY());
		}
		
		for (Link link : this.links) {
			String id = link.getLabel();
			Edge e = this.graph.addEdge(id, link.getV1(), link.getV2(), directedEdge);
			e.addAttribute(GraphUtils.ATTRIBUTE_EDGE_LABEL, link.getLabel());
			e.addAttribute(GraphUtils.ATTRIBUTE_EDGE_TYPE, link.getType());
			e.addAttribute(GraphUtils.ATTRIBUTE_EDGE_CAPACITY, link.getCapacity());
			e.addAttribute(GraphUtils.ATTRIBUTE_EDGE_LOAD, link.getLoad());
		}
	}
	
	/**
	 * Display the network topology using a graph-based representation
	 * @param k the graph to be represented with the library
	 * @param disableAutolayout manage the GraphStream autolayout
	 */
	public void displayGraph(Graph g, boolean disableAutolayout) {
		if (g == null) {
			System.exit(-1);
		}
		
		String stylesheet = Utilities.readFile("css/graph.css");
		
		g.addAttribute("ui.stylesheet", stylesheet);
		
		for (Node n : g.getNodeSet()) {
			n.addAttribute("ui.label", n.getId());
			String type = n.getAttribute(GraphUtils.ATTRIBUTE_NODE_TYPE);
			if (type.equals(GraphUtils.CORE_ROUTER)) {
				n.addAttribute("ui.class", "cro");
			}
			else if (type.equals(GraphUtils.CUSTOMER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "cer");
			}
			else if (type.equals(GraphUtils.PROVIDER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "per");
			}
		}
		
		for (Edge e : g.getEdgeSet()) {
			e.addAttribute("ui.label", e.getAttribute(GraphUtils.ATTRIBUTE_EDGE_LOAD) + " / " + e.getAttribute(GraphUtils.ATTRIBUTE_EDGE_CAPACITY));
		}
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Viewer viewer = g.display();
		
		if (disableAutolayout) {
			viewer.disableAutoLayout();
		}
	}
	
	/**
	 * Display the network topology using a POOR graph-based representation
	 * @param k the graph to be represented with the library
	 * @param disableAutolayout manage the GraphStream autolayout
	 */
	public void displayPoorGraph(Graph k, boolean disableAutolayout) {
		if (k == null) {
			System.exit(-1);
		}
		
		String stylesheet = Utilities.readFile("css/graph.css");
		
		k.addAttribute("ui.stylesheet", stylesheet);
		
		for (Node n : k.getNodeSet()) {
			String type = n.getAttribute(GraphUtils.ATTRIBUTE_NODE_TYPE);
			if (type.equals(GraphUtils.CORE_ROUTER)) {
				n.addAttribute("ui.class", "cro");
			}
			else if (type.equals(GraphUtils.CUSTOMER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "cer");
			}
			else if (type.equals(GraphUtils.PROVIDER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "per");
			}
		}
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Viewer viewer = k.display();
		
		if (disableAutolayout) {
			viewer.disableAutoLayout();
		}
	}
	
	public void setDirectedEdge(boolean directed) { this.directedEdge = directed; }
	
	public boolean isDirectedEdge() { return this.directedEdge; }
	public Graph getGraph() { return this.graph; }
}
