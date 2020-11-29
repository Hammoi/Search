import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Search {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Searching for names...");

		ArrayList<String> names = new ArrayList<>();

		names.addAll(Arrays.asList());

		String fileName = "/Users/HanyiL/Desktop/academicwords.txt";

		File textFile = new File(fileName);

		Scanner in = null;
		try {
			in = new Scanner(textFile);
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		String value = in.next();
		names.add(value);

		in.nextLine();


		while(in.hasNextLine()){
			names.add(in.nextLine());
		}

		in.close();

		System.out.println(names);


		HttpURLConnection request = null;


		ArrayList<String> availableNames = new ArrayList<String>();		

		String sortedFile = fileName.replace(".txt", "-SORTED.txt");

		try {
			File words = new File(sortedFile);
			

			
			if (words.createNewFile()) {
				System.out.println("SYSTEM: File created: " + words.getName());
			} else {
				System.out.println("SYSTEM: Output file already exists. Creating new file.");
				int x = 1;
				while(!words.createNewFile()) {
					sortedFile = sortedFile.replace(".txt", "_" + x + ".txt");
					words = new File(sortedFile);
				}
				
			}
		} catch (IOException e) {
			System.out.println("SYSTEM: An error occurred.");
			e.printStackTrace();
		}

		FileWriter myWriter = null;
		try {
			myWriter = new FileWriter(sortedFile);
			myWriter.write("ALL AVAILABLE NAMES: \n");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


		for(int x = 0; x < names.size();) {
			System.out.println("SYSTEM: Searching for " + names.get(x));
			if(names.get(x).length() > 2 && !names.get(x).contains("-")) {
				try {
					String sURL = "https://api.mojang.com/users/profiles/minecraft/" + names.get(x);
					URL url = new URL(sURL);
					request = (HttpURLConnection) url.openConnection();
					request.connect();
					JsonParser jp = new JsonParser();
					JsonElement json = jp.parse(new InputStreamReader((InputStream) request.getContent()));
					Thread.currentThread();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					x++;
				} catch(IOException e) {
					try {
						if(request.getResponseCode() == 429) { //Checks if Mojang is denying requests (Happens if too many are sent).

							System.out.println("SYSTEM: Cooldown");
							try {
								Thread.currentThread();
								Thread.sleep(10000);
								System.out.println("SYSTEM: Resuming");
								
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							availableNames.add(names.get(x));
							myWriter.write(names.get(x) + "\n");
							myWriter.flush();
							System.out.println("SYSTEM: Found " + names.get(x) + ".");
							x++;
						}
					} catch (IOException e1) {
						System.out.println("SYSTEM: Something happened with name " + names.get(x) + ". SKIPPING.");
						x++;
					}


				}
			} else x++;
		}

		try {
			myWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("SYSTEM: Finished. Found " + availableNames.size() + " names out of " + names.size());

	}
}
