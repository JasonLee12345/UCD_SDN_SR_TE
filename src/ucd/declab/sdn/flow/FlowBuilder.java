package ucd.declab.sdn.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.google.gson.Gson;

import ucd.declab.sdn.flow.elements.*;
import ucd.declab.sdn.flow.extracts.*;

public class FlowBuilder {

	private FlowCollection flowCollection;
	private Flows flows;
	
	public FlowBuilder(HashMap<String, Object> rawFlows) {
		flowCollection = processRawFlow(rawFlows);
	}
	
	
	public FlowCollection processRawFlow(HashMap<String, Object> rawFlows) {
		for (String key : rawFlows.keySet()) {
			ArrayList<Object> flowJSON = (ArrayList<Object>) rawFlows.get(key);
			FlowDetails fd = new Gson().fromJson(new Gson().toJson(flowJSON.get(2)), FlowDetails.class);
			
			if (fd.getOut() != null) {
				String src = (String) flowJSON.get(0);
				String dst = (String) flowJSON.get(1);
				double bandwidth = fd.getOut().getSize();
				
				String flowID = "";
				if (fd.getId() != null) {
					flowID = fd.getId() + "_out";
				}
				else {
					flowID = src + dst + Integer.toString(new Random().nextInt(20000)) + "_out";
				}
				
				FlowInfo flowInfo = new FlowInfo(flowID, src, dst, bandwidth);
				flowInfo.setRelationID(key);
				flowInfo.setRelationType(FlowInfo.OUT);
				this.flowCollection.add(flowInfo);
			}
			
			
			
		}
		
		
		
		
		return flows;
	}
	
	
}
