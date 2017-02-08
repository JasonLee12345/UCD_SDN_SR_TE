package ucd.declab.sdn.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class Utilities {
	
	/*
	nodes_file=topology/nodes.json
	links_file=topology/links.json
	flows_file=flow/flow_catalogue.json
	debug=true
	*/
	
	public static String CMD_NODES = "nodes_file";
	public static String CMD_LINKS = "links_file";
	public static String CMD_FLOWS = "flows_file";
	public static String DEBUG = "debug";
	
	
	/** Parse the command line parameters and return a list.
	 * @param args the command-line arguments
	 * @return a list of parameters, in a <key,value> fashion
	 */
	public static HashMap<String, String> parseCMD(String[] args) {
		HashMap<String, String> tmp = new HashMap<String, String>();
		for (String s : args) {
			String[] cmd = s.split("=");
			tmp.put(cmd[0].toLowerCase(), cmd[1]);
		}
		
		return tmp;
	}
	
	
	/** Read a JSON file 
	 * @param jsonFile, the name of the file to be opened
	 * @return the file content, with an ArrayList object
	 */
	public static ArrayList<Object> readJSONFileAsArrays(String jsonFile) {
		ArrayList<Object> file = new ArrayList<Object>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			file = new Gson().fromJson(br, ArrayList.class);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	
	/** Read a JSON file
	 * @param jsonFile, the name of the file
	 * @return the file content, with a HashMap object
	 */
	public static HashMap<String, Object> readJSONFileAsPairs(String jsonFile) {
		HashMap<String, Object> file = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			file = new Gson().fromJson(br, HashMap.class);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return file;
	}
	
	
	/** Mainly used for reading CSS files
	 * @param file The name of the css file
	 */
	public static String readFile(String file) {
		String content = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
	    
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        
	        content = sb.toString();
	        
	        br.close();
	    }
		catch (Exception e) {
			e.printStackTrace();
		}
	    
	    return content;
	}
}
