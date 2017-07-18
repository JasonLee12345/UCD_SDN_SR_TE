package ucd.declab.sdn.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.graphstream.graph.Node;

import com.google.gson.Gson;

import ucd.declab.sdn.flow.elements.*;
import ucd.declab.sdn.flow.extracts.FlowCollection;
import ucd.declab.sdn.flow.extracts.FlowInfo;

public class FlowUtils {

	/** Extract useful information from the raw data read from JSON file
	 * @param rawFlows, we'll extract info from here
	 * return a HashMap collection of the useful information
	 */
	public static Flows extractFlowsFromRaw(HashMap<String, Object> rawFlows) {
		Flows flows = new Flows();
		for (String key : rawFlows.keySet()) {
			ArrayList<Object> flowJSON = (ArrayList<Object>) rawFlows.get(key);

			String src = (String) flowJSON.get(0);
			String dst = (String) flowJSON.get(1);
			FlowDetails fd = new Gson().fromJson(new Gson().toJson(flowJSON.get(2)), FlowDetails.class);
			Flow flow = new Flow(src, dst, fd);
			flows.put(key, flow);
		}
		
		return flows;
	}
	
	/** Extract useful information from the created flows object
	 * @param flows, we'll extract info from here
	 * return a created ArrayList object where we store all the FlowInfo objects
	 */
	public static FlowCollection extractFlowInfoFromFlows(Flows flows) {
		FlowCollection flowCollection = new FlowCollection();
		
		for (String key : flows.keySet()) {
			Flow flow = flows.get(key);
			String src = flow.getSrc();
			String dst = flow.getDst();
			FlowDetails fd = flow.getFlowDetails();
			
			String id = fd.getId();
			FlowDetailsEssential in = fd.getIn();
			FlowDetailsEssential out = fd.getOut();
			
			if (in != null) {
				double bandwidth = in.getSize();
				if (bandwidth != 0.0) {
					String in_id;
					
					if (id != null) {
						in_id = id + "_in";
					}
					else {
						in_id = src + dst + Integer.toString(new Random().nextInt(20000)) + "_in";
					}
					
					FlowInfo flowInfo = new FlowInfo(in_id, dst, src, bandwidth);
					flowInfo.setRelationID(key);
					flowInfo.setRelationType(FlowInfo.IN);
					flowCollection.add(flowInfo);
				}
			}
			
			if (out != null) {
				double bandwidth = out.getSize();
				if (bandwidth != 0.0) {
					String out_id;
					
					if (id != null) {
						out_id = id + "_out";
					}
					else {
						out_id = src + dst + Integer.toString(new Random().nextInt(20000)) + "_out";
					}
					
					FlowInfo flowInfo = new FlowInfo(out_id, src, dst, bandwidth);
					flowInfo.setRelationID(key);
					flowInfo.setRelationType(FlowInfo.OUT);
					flowCollection.add(flowInfo);
				}
			}
		}
		
		return flowCollection;
	}
	
	/** Extract useful information from the created flows object
	 * @param flows, we'll extract info from here
	 * @param flowUpperBound, Max number of flows that has to be loaded
	 * return a created ArrayList object where we store all the FlowInfo objects
	 */
	public static FlowCollection extractFlowInfoFromFlows(Flows flows, int flowUpperBound) {
		FlowCollection flowCollection = new FlowCollection();
		int count = 0;
		
		for (String key : flows.keySet()) {
			Flow flow = flows.get(key);
			String src = flow.getSrc();
			String dst = flow.getDst();
			FlowDetails fd = flow.getFlowDetails();
			
			String id = fd.getId();
			FlowDetailsEssential in = fd.getIn();
			FlowDetailsEssential out = fd.getOut();
			
			if (in != null) {
				double bandwidth = in.getSize();
				
				if (id != null) {
					id = id + "_in";
				}
				else {
					id = src + dst + Integer.toString(new Random().nextInt(20000)) + "_in";
				}
				
				FlowInfo flowInfo = new FlowInfo(id, src, dst, bandwidth);
				flowInfo.setRelationID(key);
				flowInfo.setRelationType(FlowInfo.IN);
				flowCollection.add(flowInfo);
				
				count ++;
				if (count == flowUpperBound) {
					return flowCollection;
				}
			}
			
			if (out != null) {
				double bandwidth = out.getSize();
				
				if (id != null) {
					id = id + "_out";
				}
				else {
					id = src + dst + Integer.toString(new Random().nextInt(20000)) + "_out";
				}
				
				FlowInfo flowInfo = new FlowInfo(id, src, dst, bandwidth);
				flowInfo.setRelationID(key);
				flowInfo.setRelationType(FlowInfo.OUT);
				flowCollection.add(flowInfo);
				
				count ++;
				if (count == flowUpperBound) {
					return flowCollection;
				}
			}
		}
		
		return flowCollection;
	}
	
}
