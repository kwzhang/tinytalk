package com.designcraft.api.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCaseMaker {
	public Map<String, TestCase> makeTcMap(String path) throws IOException {
		Map<String, TestCase> theMap = new HashMap<String, TestCase>();
		String url = null;
		String method = null;
		List<Pair> headers = null;
		String body = null;
		String response = null;
		String apiId = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		String line = null;
		boolean on = false;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			
			if (!on && line.startsWith("FUNCTION_ID\t")) {
				on = true;
			}
			
			else if (on && line.startsWith("API_ID\t")) {
				apiId = line.substring(line.indexOf('\t') + 1);
			}
			
			else if (on && line.startsWith("URL\t")) {
				url = line.substring(line.indexOf('\t') + 1);
			}
			
			else if (on && line.startsWith("HEADER\t")) {
				headers = new ArrayList<Pair>();
				String temp = line.substring(line.indexOf('\t') + 1);
				String[] splits = temp.split(";");
				for (String split : splits) {
					String[] splits2 = split.split("=");
					headers.add(new Pair(splits2[0].trim(), splits2[1].trim()));
				}
			}
			
			else if (on && line.startsWith("METHOD\t")) {
				method = line.substring(line.indexOf('\t') + 1);
			}
			
			if (on && line.equals("BODY")) {
				body = readMultiLine(br, "RESPONSE");
				line = "RESPONSE";
			}
			
			if (on && line.equals("RESPONSE")) {
				response = readMultiLine(br, "-------");
				line = "-------";
				theMap.put(apiId, new TestCase(url, method, headers, body, response));
				on = false;
			}
		}
		br.close();
		
		return theMap;
	}
	
	public TestCase getTc(String apiId) throws IOException {
		Map<String, TestCase>  theMap = makeTcMap("../rest_api_examples.txt");
		return theMap.get(apiId);
	}

	private String readMultiLine(BufferedReader br, String breaker) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#")) {
				continue;
			}
			if (line.startsWith(breaker)) {
				break;
			}
			sb.append(line + "\n");
		}
		return sb.toString().trim();
	}

	public static void main(String[] args) throws IOException {

	}

}
