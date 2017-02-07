package ucd.declab.sdn.topology;

import java.util.ArrayList;
import java.util.Random;

import ucd.declab.sdn.graph.GraphUtils;
import ucd.declab.sdn.topology.elements.Topology;
import ucd.declab.sdn.topology.elements.TopologyEdge;
import ucd.declab.sdn.topology.elements.TopologyEdgeDetails;
import ucd.declab.sdn.topology.elements.TopologyNode;
import ucd.declab.sdn.topology.elements.TopologyNodeDetails;
import ucd.declab.sdn.topology.extracts.Link;
import ucd.declab.sdn.topology.extracts.Vertex;

import com.google.gson.Gson;

public class TopologyUtils {
	
	/** Return a set of topology nodes, from a specified TopologyCatalogue
	 * @param rawNodes, read data from the JSON file
	 * @return a set of network nodes
	 */
	public static ArrayList<TopologyNode> extractNodesInfoFromRawNodes(ArrayList<Object> rawNodes) {
		ArrayList<TopologyNode> retNodes = new ArrayList<TopologyNode>();
		
		for (Object obj : rawNodes) {
			ArrayList<Object> nodeJSON = (ArrayList<Object>) obj;
			String nodeLabel = (String) nodeJSON.get(0);
			TopologyNodeDetails tnd = new Gson().fromJson(nodeJSON.get(1).toString(), TopologyNodeDetails.class);
			retNodes.add(new TopologyNode(nodeLabel, tnd));
		}
		
		return retNodes;
	}
	
	/** Return a set of topology edges
	 * @param rawLinks, read data from the JSON file
	 * @return a set of network edges
	 */
	public static ArrayList<TopologyEdge> extractEdgesInfoFromRawLinks(ArrayList<Object> rawLinks) {
		ArrayList<TopologyEdge> retEdges = new ArrayList<TopologyEdge>();
		
		for (Object obj : rawLinks) {
			ArrayList<Object> nodeJSON = (ArrayList<Object>) obj;
			String src = (String) nodeJSON.get(0);
			String dst = (String) nodeJSON.get(1);
			TopologyEdgeDetails ted = new Gson().fromJson(nodeJSON.get(2).toString(), TopologyEdgeDetails.class);
			retEdges.add(new TopologyEdge(src, dst, ted));
		}
		
		return retEdges;
	}
	
	/** Return a set of topology nodes, from a specified Topology created
	 * @param topology topology that has been created 
	 * @return a set of network vertices
	 */
	public static ArrayList<Vertex> extractVerticesFromTopology(Topology topo) {
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (TopologyEdge te : topo.getEdges()) {
			if (!tmp.contains(te.getSrc())) {
				Vertex v = new Vertex(te.getSrc(), GraphUtils.CORE_ROUTER);
				v.setX(new Random().nextDouble() * 200.0);
				v.setY(new Random().nextDouble() * 200.0);
				vertices.add(v);
				
				tmp.add(te.getSrc());
			}
			
			if (!tmp.contains(te.getDst())) {
				Vertex v = new Vertex(te.getDst(), GraphUtils.CORE_ROUTER);
				v.setX(new Random().nextDouble() * 200.0);
				v.setY(new Random().nextDouble() * 200.0);
				vertices.add(v);
				
				tmp.add(te.getDst());
			}
		}
		
		return vertices;
	}
	
	/** Return a set of topology links, from a specified Topology created
	 * @param topology that has been created
	 * @return a set of network links
	 */
	public static ArrayList<Link> extractLinksFromTopology(Topology topo) {
		ArrayList<Link> links = new ArrayList<Link>();
		
		for (TopologyEdge te : topo.getEdges()) {
			TopologyEdgeDetails details = te.getTopologyEdgeDetails();
			String src = te.getSrc();
			String dst = te.getDst();
			String label = "";
			if (details.getId() != null) {
				label = details.getId();
			}
			else {
				label = src + dst + Integer.toString(new Random().nextInt(20000));
			}
			double capacity = details.getCapacity();
			double load = details.getAllocated();
			
			Link link = new Link(src, dst, label, "", capacity, load);
			links.add(link);
		}
		
		return links;
	}
}
