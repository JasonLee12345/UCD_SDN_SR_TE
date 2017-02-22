package ucd.declab.sdn.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.SinkAdapter;

import ucd.declab.sdn.flow.extracts.FlowCollection;
import ucd.declab.sdn.flow.extracts.FlowInfo;
import ucd.declab.sdn.utils.Constants;

public class FlowAssignmentAlgorithm extends SinkAdapter implements DynamicAlgorithm {

	private boolean terminate = false;
	
	private Graph g = null;
	private Graph inner = null;
	
	private FlowCollection flows = null;
	private FlowCollection partialFlows = null;
	
	private double sigma = 0.0;
	private double Tglob = 0.0;
	private double Tfin = 0.0;
	
	private boolean directedEdge = true;

	@Override
	public void compute() {
		// TODO Auto-generated method stub
		initialFlowAllocation();
		Tglob = averageDelayCalculation(inner);
		finalTimeAllocation(Tglob);
		
		boolean iterate = true;
		while (iterate) {
			for (int i = 0; i < partialFlows.size(); i++) {				
				flowAllocationLoop(partialFlows.get(i));
			}
			
			iterate = timesComparison();
		}
	}

	@Override
	public void init(Graph graph) {
		// TODO Auto-generated method stub
		g = graphCopy(graph, graph.getId() + "_INIT");
		g.addSink(this);
		
		partialFlows = new FlowCollection();
	}

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		g.removeSink(this);
		terminate = true;
	}
	
	// This function need to be specifically called before the computation.
	public void setTrafficFlows(FlowCollection flows) {
		this.flows = flows;
	}
	
	private void finalTimeAllocation(double glob) {
		Tfin = glob;
	}
	
	public FlowCollection getFlowAssignment() {
		if (terminate)
			return partialFlows;
		
		return null;
	}
	
	public Graph getUpdatedGraph() {
		return graphCopy(inner, inner.getId() + "_COPY");
	}
	
	private double averageDelayCalculation(Graph graph) {
		sigma = 0.0;
		
		for (FlowInfo f : partialFlows) {
			sigma += f.getBandwidth();
		}
		
		double localTglob = 0.0;
		for (Edge e : graph.getEdgeSet()) {
			double capacity = e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY);
			double load = e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
			
			// Anyway, it's saying if the load is big, the delay is correspondingly high.
			localTglob +=  load / (capacity - load);
		}
		
		localTglob = (1 / sigma) * localTglob;
		
		return localTglob;
	}
	
	/**
	 * The naive flow assignment algorithm.
	 */
	private void initialFlowAllocation() {
		inner = graphCopy(g, g.getId() + "_INNER");
		
		double BIGK = 0.0;
		
		//Find the largest capacity and store it in the variable BIGK.
		for (Edge e : inner.getEdgeSet()) {
			double nominalCapacity = e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY);
			if (nominalCapacity > BIGK) {
				BIGK = nominalCapacity;
			}
		}
		
		//For each edge, we create a container to store all the flows.
		HashMap<String, FlowCollection> edgesUtilization = new HashMap<String, FlowCollection>();
		for (Edge e : inner.getEdgeSet()) {
			String key = "(" + e.getSourceNode().getId() + "," + e.getTargetNode().getId() + ")";
			edgesUtilization.put(key, new FlowCollection());
		}
		
		//Now let's deal with all the flows one by one.
		for (FlowInfo f : flows) {
			Graph innerTMP = new MultiGraph("TMPGraph");
			
			// Copy all the nodes and their attributes into the new temporary graph.
			for (Node node : inner.getNodeSet()) {
				Node n = innerTMP.addNode(node.getId());
				for (String attribute : node.getAttributeKeySet()) {
					n.addAttribute(attribute, node.getAttribute(attribute));
				}
			}
			
			// Dealing with all the edges. Only store those edges with enough capacities into innerTMP graph. Otherwise ignore.
			for (Edge e : inner.getEdgeSet()) {
				double nominalCapacity = e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY);
				double load = e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
				double residualCapacity = nominalCapacity - load;
				
				double linkCost = BIGK / residualCapacity;
				
				e.addAttribute(Constants.ATTRIBUTE_EDGE_COST, linkCost);
				
				// If there is enough capacity, we add this edge to a temporary graph
				double flowBW = f.getBandwidth();
				if ((residualCapacity - flowBW) >= 0.0) {
					Edge eTmp = innerTMP.addEdge(e.getId(), e.getSourceNode().getId(), e.getTargetNode().getId(), this.directedEdge);
					for (String attribute : e.getAttributeKeySet()) {
						eTmp.addAttribute(attribute, e.getAttribute(attribute));
					}
				}
			}
			
			Node source = innerTMP.getNode(f.getNodeSource());
			
			Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, "result", Constants.ATTRIBUTE_EDGE_COST);
			d.init(innerTMP);
			d.setSource(source);
			d.compute();
				
			Node dest = innerTMP.getNode(f.getNodeDestination());
			
			String path = "";
			for (Edge pathEdge : d.getPathEdges(dest)) {
				double load = inner.getEdge(pathEdge.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
				load += f.getBandwidth();
				inner.getEdge(pathEdge.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
				String key = "(" + pathEdge.getSourceNode().getId() + "," + pathEdge.getTargetNode().getId() + ")";
				path = "," + key + path;
				edgesUtilization.get(key).add(f);
			}
			
			f.setPath(d.getPath(dest));
			
			if (f.getPath().size() > 0) {
				partialFlows.add(f);
			}
			
			innerTMP.clearSinks();
			innerTMP.clearElementSinks();
			innerTMP.clearAttributeSinks();
			innerTMP.clearAttributes();
			innerTMP.clear();
		}
		
		// Copy all the assigned flow bandwidths and add them together into the graph that previously was used.
		for (Edge e : g.getEdgeSet()) {
			String key = "(" + e.getSourceNode().getId() + "," + e.getTargetNode().getId() + ")";
			if (edgesUtilization.get(key) != null) {
				double count = 0;
				for (FlowInfo fi : edgesUtilization.get(key)) {
					count += fi.getBandwidth();
				}
				e.addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, count);
			}
		}
	}
	
	private void flowAllocationLoop(FlowInfo cfe) {
		Graph tmp = graphCopy(inner, inner.getId() + "_STEP567");
		
		// Deallocate current flow from temporary graph
		for (Edge e : cfe.getPath().getEdgeSet()) {
			double load = tmp.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
			load = load - cfe.getBandwidth();
			tmp.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
		}
		
		// Prune edges that doesn't support the current flow
		ArrayList<Edge> pruned = new ArrayList<Edge>();
		for (Edge e : tmp.getEdgeSet()) {
			double capacity = e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY);
			double load = e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
			load += cfe.getBandwidth();
			if (capacity <= load) {
				pruned.add(e);
				tmp.removeEdge(e);
			}
		}
		
		// Edge length calculation, with the UniRoma2 formula
		for (Edge e : tmp.getEdgeSet()) {
			double capacity = e.getAttribute(Constants.ATTRIBUTE_EDGE_CAPACITY);
			double load = e.getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
			double length = capacity / (Math.pow(capacity - load, 2));
			length = (1 / sigma) * length;
			e.addAttribute(Constants.ATTRIBUTE_EDGE_LENGTH, length);
		}
		
		Node source = tmp.getNode(cfe.getNodeSource());
		
		// CSPF --> Dijkstra
		Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, "result", Constants.ATTRIBUTE_EDGE_LENGTH);
		d.init(tmp);
		d.setSource(source);
		d.compute();
		
		Node dest = tmp.getNode(cfe.getNodeDestination());
		
		int count = 0;
		for (Path p : d.getAllPaths(dest)) {
			count++;
		}
		
		Path path = cfe.getPath();
		double pathLength = 0.0;
		for (Edge e : path.getEdgeSet()) {
			pathLength += (double) tmp.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LENGTH);
		}
		
		if (count > 1) {
			for (Path p : d.getAllPaths(dest)) {
				double pl = 0.0;
				for (Edge e : p.getEdgeSet()) {
					pl += (double) tmp.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LENGTH);
				}
				
				if ((pl < pathLength) && (!pathEquals(p, path))) {
					path = p;
					pathLength = pl;					
				}
			}
			
			// Sum discovered path to temporary graph
			for (Edge e : path.getEdgeSet()) {
				double load = tmp.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
				load += cfe.getBandwidth();
				tmp.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
			}
			
			// BEFORE re-calculate the delay time, re-add previously pruned edges to temporary graph
			for (Edge e : pruned) {
				Edge ne = tmp.addEdge(e.getId(), e.getSourceNode(), e.getTargetNode(), this.directedEdge);
				for (String keyAttribute : e.getAttributeKeySet()) {
					ne.addAttribute(keyAttribute, e.getAttribute(keyAttribute));
				}
			}
			
			double localTglob = averageDelayCalculation(tmp);
			if (localTglob < Tfin) {
				finalTimeAllocation(localTglob);
				
				// Delete OLD path for the current flow from the graph
				for (Edge e : cfe.getPath().getEdgeSet()) {
					double load = inner.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
					load -= cfe.getBandwidth();
					inner.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
				}
				
				// Add NEW path for the current flow to the graph
				for (Edge e : path.getEdgeSet()) {
					double load = inner.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
					load += cfe.getBandwidth();
					inner.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
				}
				
				// Add NEW path to the current flow
				cfe.setPath(path);
			}
		}
		else {
			Path found = d.getPath(dest);
			double dijkstraPathLength = 0.0;
			for (Edge e : found.getEdgeSet()) {
				dijkstraPathLength += (double) e.getAttribute(Constants.ATTRIBUTE_EDGE_LENGTH);
			}
			
			if (dijkstraPathLength < pathLength) {				
				// Sum discovered path to temporary graph
				for (Edge e : found.getEdgeSet()) {
					double load = tmp.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
					load += cfe.getBandwidth();
					tmp.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
				}
				
				// BEFORE re-calculate the delay time, re-add previously pruned edges to temporary graph
				for (Edge e : pruned) {
					Edge ne = tmp.addEdge(e.getId(), e.getSourceNode(), e.getTargetNode(), this.directedEdge);
					for (String keyAttribute : e.getAttributeKeySet()) {
						ne.addAttribute(keyAttribute, e.getAttribute(keyAttribute));
					}
				}
				
				double localTglob = averageDelayCalculation(tmp);
				if (localTglob < Tfin) {
					finalTimeAllocation(localTglob);
					
					// Delete OLD path for the current flow from the graph
					for (Edge e : path.getEdgeSet()) {
						double load = inner.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
						load -= cfe.getBandwidth();
						inner.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
					}
					
					// Add NEW path for the current flow to the graph
					for (Edge e : found.getEdgeSet()) {
						double load = inner.getEdge(e.getId()).getAttribute(Constants.ATTRIBUTE_EDGE_LOAD);
						load += cfe.getBandwidth();
						inner.getEdge(e.getId()).addAttribute(Constants.ATTRIBUTE_EDGE_LOAD, load);
					}
					
					// Add NEW path to the current flow
					cfe.setPath(found);
					path = found;
					pathLength = dijkstraPathLength;
				}
			}
		}
	}
	
	private boolean timesComparison() {
		if (Tfin < Tglob) {
			Tglob = Tfin;
			return true;
		}
		else if (Tfin == Tglob) {
			return false;
		}
		
		return true;
	}
	
	private Graph graphCopy(Graph graph, String name) {
		Graph cpy = new MultiGraph(name);
		
		for (Node node : graph.getNodeSet()) {
			Node n = cpy.addNode(node.getId());
			for (String attribute : node.getAttributeKeySet()) {
				n.addAttribute(attribute, node.getAttribute(attribute));
			}
		}
		
		for (Edge edge : graph.getEdgeSet()) {
			Edge e = cpy.addEdge(edge.getId(), edge.getSourceNode().getId(), edge.getTargetNode().getId(), this.directedEdge);
			for (String attribute : edge.getAttributeKeySet()) {
				e.addAttribute(attribute, edge.getAttribute(attribute));
			}
		}
		
		return cpy;
	}
	
	private static boolean pathEquals(Path p1, Path p2) {
		if (p1.size() != p2.size()) {
			return false;
		}
		
		Iterator<Edge> p1_i = p1.getEdgeIterator();
		Iterator<Edge> p2_i = p2.getEdgeIterator();
		while (p1_i.hasNext()) {
			if (!p1_i.next().getId().equals(p2_i.next().getId())) {
				return false;
			}
		}
		
		return true;
	}
}
