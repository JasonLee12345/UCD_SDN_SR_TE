package ucd.declab.sdn.topology;

import java.util.ArrayList;

import com.google.gson.Gson;

import ucd.declab.sdn.topology.elements.*;

public class TopologyBuilder {
	
	private TopologyCollection topology;
	
	public TopologyBuilder (ArrayList<Object> rawNodes, ArrayList<Object> rawLinks) {
		ArrayList<TopologyNode> nodes = extractNodesInfoFromRawNodes(rawNodes);
		ArrayList<TopologyEdge> edges = extractEdgesInfoFromRawLinks(rawLinks);
		
		/*
		 * For tests uses only
		 */
		/*
		for (TopologyNode node : nodes) {
			System.out.println(node.getLabel());
			System.out.println(node.getTopologyNodeDetails().getCity());
		}
		
		for (TopologyEdge edge : edges) {
			System.out.println(edge.getV1());
			System.out.println(edge.getV2());
			System.out.println(edge.getTopologyEdgeDetails().getId());
			System.out.println(edge.getTopologyEdgeDetails().getCapacity());
			System.out.println(" ");
		}*/
		
		topology = new TopologyCollection(nodes, edges);
	}
	
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
	
	public TopologyCollection getTopologyCollection() { return this.topology; }
}
