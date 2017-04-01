package ucd.declab.sdn.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import java.util.concurrent.ThreadLocalRandom;

import ucd.declab.sdn.flow.elements.*;
import ucd.declab.sdn.topology.elements.TopologyNodeDetails;

public class CreateJSONForRandomFlows {
	
	private int flowNum;
	private int minNode;
	private int maxNode;
	private boolean isBiDirec;
	
	public CreateJSONForRandomFlows(int flowNum, int minNode, int maxNode) {
		this.flowNum = flowNum;
		this.minNode = minNode;
		this.maxNode = maxNode;
	}

	/**
	 * If flows should be generated bidirectional.
	 * @param isBidirectional
	 * @param str, from "a" to "z". "a" means 2^1 nodes in the BT tree, "b" mean 2^2, ...
	 */
	public void generateFlows(boolean isBidirectional, String str) {
		HashMap<String, ArrayList<Object>> myflows = new HashMap<String, ArrayList<Object>>();
		int fNum = flowNum;
		if (isBidirectional)	fNum /= 2;
		
		for (int i = 0; i < fNum; ++i) {
			String src = Integer.toString(ThreadLocalRandom.current().nextInt(minNode, maxNode + 1));
			String dst = Integer.toString(ThreadLocalRandom.current().nextInt(minNode, maxNode + 1));
			while (src.equals(dst))	dst = Integer.toString(ThreadLocalRandom.current().nextInt(minNode, maxNode + 1));
			
			
			FlowDetailsEssential fdeIn = new FlowDetailsEssential();
			FlowDetailsEssential fdeOut = new FlowDetailsEssential();
			if (isBidirectional)	fdeIn.setSize(ThreadLocalRandom.current().nextDouble(0.0, 30.0));
			fdeOut.setSize(ThreadLocalRandom.current().nextDouble(0.0, 30.0));
			
			FlowDetails fd = new FlowDetails("10" + Integer.toString(i), fdeIn, fdeOut);
			
			ArrayList<Object> innerList = new ArrayList<Object>();
			innerList.add(src);
			innerList.add(dst);
			innerList.add(fd);
			myflows.put(Integer.toString(i), innerList);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(myflows);
		//System.out.print(json);
		
		try {
			   //write converted json data to a file named "CountryGSON.json"
			   FileWriter writer = new FileWriter("flow/experiments/" + str + "/" + str + Integer.toString(flowNum) + ".flow_catalogue.json");
			   writer.write(json);
			   writer.close();
		} catch (IOException e) {
			   e.printStackTrace();
		}
	}
}
