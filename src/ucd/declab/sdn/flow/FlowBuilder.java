package ucd.declab.sdn.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.google.gson.Gson;

import ucd.declab.sdn.flow.elements.*;
import ucd.declab.sdn.flow.extracts.*;

public class FlowBuilder {

	private Flows flows;
	private FlowCollection flowCollection;
	
	public FlowBuilder() {
		this.flows = new Flows();
		this.flowCollection = new FlowCollection();
	}
	
	public FlowBuilder(HashMap<String, Object> rawFlows) {
		this.flows = FlowUtils.extractFlowsFromRaw(rawFlows);
		this.flowCollection = FlowUtils.extractFlowInfoFromFlows(this.flows);
	}
	
	public Flows getFlows() { return this.flows; }
	public FlowCollection getFlowCollection() { return this.flowCollection; }
}
