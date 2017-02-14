package ucd.declab.sdn.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.Viewer;

import ucd.declab.sdn.topology.extracts.*;
import ucd.declab.sdn.utils.*;
import ucd.declab.sdn.flow.extracts.*;

public class GraphBuilder {

	private Graph graph;
	private ArrayList<Vertex> vertices;
	private ArrayList<Link> links;
	private FlowCollection flowCollection;
	private boolean isDirectedEdge = true;
	
	public GraphBuilder(TopologyCollection topoCollection) {
		this.vertices = topoCollection.getVertices();
		this.links = topoCollection.getLinks();
	}
	
	public GraphBuilder(TopologyCollection topoCollection, FlowCollection flowCollection) {
		this.vertices = topoCollection.getVertices();
		this.links = topoCollection.getLinks();
		this.flowCollection = flowCollection;
	}
	
	
	/** Build the GraphStream graph from the collection
	 * 
	 */
	public void buildGraphStreamTopology() {
		this.graph = new MultiGraph("Test_MultiGraph");
		
		for (Vertex vertex : this.vertices) {
			Node n = this.graph.addNode(vertex.getLabel());
			n.setAttribute(Constants.ATTRIBUTE_NODE_TYPE, vertex.getType());
			n.setAttribute("x", vertex.getX());
			n.setAttribute("y", vertex.getY());
		}
		
		for (Link link : this.links) {
			Edge e = this.graph.addEdge(link.getLabel(), link.getV1(), link.getV2(), isDirectedEdge);
			e.addAttribute(Constants.ATTRIBUTE_EDGE_LABEL, link.getLabel());
			e.addAttribute(Constants.ATTRIBUTE_EDGE_TYPE, link.getType());
			e.addAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY, link.getCapacity());
			e.addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, link.getLoad());
		}
	}
	
	
	/**
	 * Display the network topology using a graph-based representation
	 * @param k the graph to be represented with the library
	 * @param disableAutolayout manage the GraphStream autolayout
	 */
	public void displayGraph(Graph graph, boolean disableAutolayout) {
		if (graph == null) {
			System.err.println("Graph isn't initialized...");
			System.exit(-1);
		}
		
		String stylesheet = Utilities.readFile("css/graph.css");
		
		graph.addAttribute("ui.stylesheet", stylesheet);
		
		for (Node n : graph.getNodeSet()) {
			n.addAttribute("ui.label", n.getId());
			String type = n.getAttribute(Constants.ATTRIBUTE_NODE_TYPE);
			if (type.equals(Constants.CORE_ROUTER)) {
				n.addAttribute("ui.class", "core_router");
			}
			else if (type.equals(Constants.CUSTOMER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "customer_edge_router");
			}
			else if (type.equals(Constants.PROVIDER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "provider_edge_router");
			}
		}
		
		for (Edge e : graph.getEdgeSet()) {
			e.addAttribute("ui.label", e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD) + " / " + e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY));
		}
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Viewer viewer = graph.display();
		
		if (disableAutolayout) {
			viewer.disableAutoLayout();
		}
	}
	
	
	/**
	 * Display the network topology using a POOR graph-based representation
	 * @param k the graph to be represented with the library
	 * @param disableAutolayout manage the GraphStream autolayout
	 */
	public void displayPoorGraph(Graph graph, boolean disableAutolayout) {
		if (graph == null) {
			System.err.println("Graph isn't initialized...");
			System.exit(-1);
		}
		
		String stylesheet = Utilities.readFile("css/graph.css");
		graph.addAttribute("ui.stylesheet", stylesheet);
		
		for (Node n : graph.getNodeSet()) {
			String type = n.getAttribute(Constants.ATTRIBUTE_NODE_TYPE);
			if (type.equals(Constants.CORE_ROUTER)) {
				n.addAttribute("ui.class", "core_router");
			}
			else if (type.equals(Constants.CUSTOMER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "customer_edge_router");
			}
			else if (type.equals(Constants.PROVIDER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "provider_edge_router");
			}
		}
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Viewer viewer = graph.display();
		
		if (disableAutolayout) {
			viewer.disableAutoLayout();
		}
	}
	
	
	/**
	 * Display the network topology using a graph-based representation
	 * @param graph the graph to be represented with the library
	 * @param disableAutolayout manage the GraphStream autolayout
	 */
	public void displayGraphWithFlows(Graph graph, FlowCollection fc, boolean disableAutolayout) {
		if (graph == null) {
			System.exit(-1);
		}
		
		String stylesheet = Utilities.readFile("css/graph.css");
		
		graph.addAttribute("ui.stylesheet", stylesheet);
		
		for (Node n : graph.getNodeSet()) {
			n.addAttribute("ui.label", n.getId());
			String type = n.getAttribute(Constants.ATTRIBUTE_NODE_TYPE);
			if (type.equals(Constants.CORE_ROUTER)) {
				n.addAttribute("ui.class", "cro");
			}
			else if (type.equals(Constants.CUSTOMER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "cer");
			}
			else if (type.equals(Constants.PROVIDER_EDGE_ROUTER)) {
				n.addAttribute("ui.class", "per");
			}
		}
		
		HashMap<String, GraphUtils.SpritePair> busy = new HashMap<>();
		for (Edge e : graph.getEdgeSet()) {
			e.addAttribute("ui.label", e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD) + " / " + e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY));
			busy.put(e.getId(), new GraphUtils.SpritePair(1.0, false));
		}
		
		SpriteManager smgr = new SpriteManager(graph);
		int spriteIdx = 1;
		double spriteDistance = 7.0;
		double spriteX = 1.0;
		
		for (FlowInfo f : fc) {
			for (Edge e : f.getPath().getEdgeSet()) {
				Sprite sp = smgr.addSprite("sprite_" + e.getId() + "_" + Integer.toString(new Random().nextInt(15)));
				sp.attachToEdge(e.getId());
				double spriteY = busy.get(e.getId()).getSpritePos() * (-spriteDistance);
				if (busy.get(e.getId()).getSignChange()) {
					spriteY = -spriteY;
					busy.get(e.getId()).incrementSpritePos();
				}
				busy.get(e.getId()).switchSignChange();
				sp.setPosition(Units.PX, spriteX, spriteY, 0);
				sp.addAttribute("ui.class", "S" + spriteIdx);
			}
			if (spriteIdx <= 9) {
				spriteIdx++;
			}
			else {
				spriteIdx = 1;
			}
		}
		
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		Viewer viewer = graph.display();
		
		if (disableAutolayout) {
			viewer.disableAutoLayout();
		}
	}
	
	public void setDirectedEdge(boolean directed) { this.isDirectedEdge = directed; }
	
	public boolean isDirectedEdge() { return this.isDirectedEdge; }
	public Graph getGraph() { return this.graph; }
}
