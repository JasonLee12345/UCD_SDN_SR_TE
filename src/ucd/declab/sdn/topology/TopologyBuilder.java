package ucd.declab.sdn.topology;

import java.util.ArrayList;
import java.util.Random;

import ucd.declab.sdn.topology.elements.*;

public class TopologyBuilder {
	
	private TopologyCollection topology;
	
	public TopologyBuilder (ArrayList<Object> rawNodes, ArrayList<Object> rawLinks) {
		
		
	}
	
	/** Return a set of topology nodes, from a specified TopologyCatalogue
	 * @param catalogue topology catalogue
	 * @return a set of network nodes
	 */
	public static ArrayList<TopologyNode> extractNodesFromRawNodes(ArrayList<Object> rawNodes) {
		ArrayList<TopologyNode> retNodes = new ArrayList<TopologyNode>();
		ArrayList<String> tmp = new ArrayList<String>();
		
		for (TopologyCatalogueElement tce : catalogue) {
			if (!tmp.contains(tce.getSrc())) {
				TopologyNode tn = new TopologyNode(tce.getSrc(), GraphConstant.CORE_ROUTER);
				tn.setX(new Random().nextDouble() * 200.0);
				tn.setY(new Random().nextDouble() * 200.0);
				retNodes.add(tn);
				
				tmp.add(tce.getSrc());
			}
			
			if (!tmp.contains(tce.getDst())) {
				TopologyNode tn = new TopologyNode(tce.getDst(), GraphConstant.CORE_ROUTER);
				tn.setX(new Random().nextDouble() * 200.0);
				tn.setY(new Random().nextDouble() * 200.0);
				retNodes.add(tn);
				
				tmp.add(tce.getDst());
			}
		}
		
		return retNodes;
	}
	
	/** Return a set of topology edges, from a specified TopologyCatalogue
	 * @param catalogue topology catalogue
	 * @return a set of network edges
	 */
	public static ArrayList<TopologyEdge> extractEdgesFromRawLinks(TopologyCatalogue catalogue) {
		ArrayList<TopologyEdge> retEdges = new ArrayList<TopologyEdge>();
		
		for (TopologyCatalogueElement tce : catalogue) {
			String src = tce.getSrc();
			String dst = tce.getDst();
			String label = "";
			if (tce.getFeatures().getId() != null) {
				label = tce.getFeatures().getId();
			}
			else {
				label = src + dst + Integer.toString(new Random().nextInt(20000));
			}
			double capacity = tce.getFeatures().getCapacity();
			double load = tce.getFeatures().getAllocated();
			
			TopologyEdge te = new TopologyEdge(src, dst, label, "", capacity, load);
			retEdges.add(te);
		}
		
		return retEdges;
	}
}
