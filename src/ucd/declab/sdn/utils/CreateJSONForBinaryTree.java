package ucd.declab.sdn.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.*;
import java.util.ArrayList;
import com.google.gson.Gson;

import scala.Int;
import ucd.declab.sdn.topology.elements.*;

public class CreateJSONForBinaryTree {

	// How many levels in the binary tree.
	private int level;
	
	/**
	 * One additional node 0 will be created linked to the root of the tree.
	 * The purpose of this is to create a B-Tree with exact 2^n nodes.
	 * So that the number of direct links in a B-Tree is 2^n-1.
	 * Additionally, the graphs are bidirectional graphs, 
	 * so that the final number of all links are 2*(2^n-1).
	 * @param level, the power of 2, which is represent as n in the equation.
	 */
	public CreateJSONForBinaryTree(int level) {
		this.level = level;
	}
	
	public void generateAll() {
		generateNodes();
		generateEdges();
	}
	
	public void generateNodes() {
		double nodeNum = Math.pow(2, level);
		//System.out.println("Math.pow(2, level): " + nodeNum);
		ArrayList<ArrayList> myList= new ArrayList<ArrayList>();
		
		for (int i = 0; i < nodeNum; ++i) {
			ArrayList<Object> innerList = new ArrayList<Object>();
			String id = new String(Integer.toString(i));
			TopologyNodeDetails tnd = new TopologyNodeDetails();
			innerList.add(id);
			innerList.add(tnd);
			myList.add(innerList);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(myList);
		//System.out.print(json);
		
		try {
			   //write converted json data to a file named "CountryGSON.json"
			   FileWriter writer = new FileWriter("topology/experiments/" + "BT" + Integer.toString(level) + ".nodes.json");
			   writer.write(json);
			   writer.close();
		} catch (IOException e) {
			   e.printStackTrace();
		}
	}
	
	public void generateEdges() {
		double edgeNum = Math.pow(2, level) - 1;
		ArrayList<ArrayList> myList= new ArrayList<ArrayList>();
		
		for (int i = 0; i < edgeNum; ++i) {
			// Because it's a bidirectional graph.
			ArrayList<Object> innerList = new ArrayList<Object>();
			ArrayList<Object> innerListReverse = new ArrayList<Object>();
			String src;
			String dst;
			
			if (i == 0) {
				src = "0";
				dst = "1";
			} else {
				int dstInt = i + 1;
				int srcInt = dstInt / 2;
				src = Integer.toString(srcInt);
				dst = Integer.toString(dstInt);
			}
			
			String id = new String("1000" + Integer.toString(i));
			TopologyEdgeDetails ted = new TopologyEdgeDetails(0.0, 1000.0, id);
			String id1 = new String("1001" + Integer.toString(i));
			TopologyEdgeDetails ted1 = new TopologyEdgeDetails(0.0, 1000.0, id1);
			
			innerList.add(src);
			innerList.add(dst);
			innerList.add(ted);
			innerListReverse.add(dst);
			innerListReverse.add(src);
			innerListReverse.add(ted1);
			myList.add(innerList);
			myList.add(innerListReverse);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(myList);
		//System.out.print(json);
		
		try {
			   //write converted json data to a file named "CountryGSON.json"
			   FileWriter writer = new FileWriter("topology/experiments/" + "BT" + Integer.toString(level) + ".links.json");
			   writer.write(json);
			   writer.close();
		} catch (IOException e) {
			   e.printStackTrace();
		}
	}
	
}
