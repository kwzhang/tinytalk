package com.designcraft.api;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.designcraft.api.common.Pair;
import com.designcraft.api.common.TestCase;

public class TestCaseMaker {
	public List<TestCase> makeTcList(String path) throws IOException {
		List<TestCase> theList = new ArrayList<TestCase>();
		String url = null;
		String method = null;
		List<Pair> headers = null;
		String body = null;
		String response = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
		String line = null;
		boolean on = false;
		while ((line = br.readLine()) != null) {
			line = line.trim().toLowerCase();
			if (line.startsWith("#")) {
				continue;
			}
			
			if (!on && line.startsWith("id\t")) {
				on = true;
			}
			
			else if (on && line.startsWith("url\t")) {
				url = line.substring(line.indexOf('\t') + 1);
			}
			
			else if (on && line.startsWith("header\t")) {
				headers = new ArrayList<Pair>();
				String temp = line.substring(line.indexOf('\t') + 1);
				String[] splits = temp.split(";");
				for (String split : splits) {
					String[] splits2 = split.split("=");
					headers.add(new Pair(splits2[0], splits2[1]));
				}
			}
			
			else if (on && line.startsWith("method\t")) {
				method = line.substring(line.indexOf('\t') + 1);
			}
			
			if (on && line.equals("body")) {
				body = readMultiLine(br, "response");
				line = "response";
			}
			
			if (on && line.equals("response")) {
				response = readMultiLine(br, "-------");
				line = "-------";
				theList.add(new TestCase(url, method, headers, body, response));
				on = false;
			}
		}
		br.close();
		
		return theList;
	}

	private String readMultiLine(BufferedReader br, String breaker) throws IOException {
		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.toLowerCase().trim();
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
		TestCaseMaker tcMaker = new TestCaseMaker();
		List<TestCase> tcList = tcMaker.makeTcList("../rest_api_examples.txt");
		for (TestCase tc : tcList) {
			System.out.println(tc);
		}
	}

}
