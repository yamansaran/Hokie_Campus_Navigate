package com.biaszebra.hokiecampusnavigate;


import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;



public class CSVReader {

	public ArrayList<String> name1 = new ArrayList<>();
	public ArrayList<String> name2 = new ArrayList<>();
	public ArrayList<Integer> cost = new ArrayList<>();
	private String path = "Node Map.csv.txt";
	InputStream is = null;
	int entries = 0;

	InputStream stream;

	CSVReader(InputStream is) {
		this.stream = is;
	}

	public void CSVIni() {

		InputStream is = stream;
		try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8.name())) {
			while (sc.hasNextLine()) {
				String str = sc.nextLine();
				System.out.println(str);
				entries++;
				System.out.println(str+"`````````````````````````````````````````````````````");
				String[] res = str.split("[,]", 0);
				name1.add(res[0]);
				name2.add(res[1]);
				Integer costInt = Integer.valueOf(res[2]);
				cost.add(costInt);


			}
		}

	}

}
	
	

