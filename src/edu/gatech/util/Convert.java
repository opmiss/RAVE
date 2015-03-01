package edu.gatech.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Convert {
	public static void main(String[] args){
		FileRead fr = new FileRead("data/word2vec/xaf.txt"); 
		ArrayList<String> words = new ArrayList<String>(); 
		ArrayList<String> weights = new ArrayList<String>(); 
		String line; 
		String word; 
		float sum; 
		float p=0; 
		int n=0; 
		int max_num_words = 1000; 
		int num_words = 0; 
		int num_dim=6; 
		while ((line=fr.readLine())!=null && num_words<max_num_words){
			String[] parts = line.split(" "); 
			sum=0; 
			n=0; 
			for (int i=1; i<parts.length; i++){
				p=Float.parseFloat(parts[i]); 
				sum+=p; 
				if (p<0) n++; 
			}
			if (n>20||sum<0) continue; 
			else {
				words.add(parts[0]); 
				float[] w = new float[num_dim]; 
				sum=0; 
				for (int i=0; i<num_dim; i++){
					w[i] = Float.parseFloat(parts[i+1]); 
					if (w[i]<0) w[i] = 0; 
					sum+=w[i]; 
				}
				word=""; 
				for (int i=0; i<num_dim; i++){
					w[i]/=sum; 
					System.out.print(w[i]+" "); 
					word+=w[i]+" "; 
				}
				System.out.println(); 
				weights.add(word); 
				num_words++; 
			}
		}
		System.out.print("\n"); 
		System.out.println("number of words: "+words.size()); 
		System.out.println(words); 
		
		try {
			FileWrite fw = new FileWrite("data/word2vec/xaf.weight");
			FileWrite fd = new FileWrite("data/word2vec/xaf.docmeta"); 
			for (int i=0; i<num_words; i++){
				fw.writeLine(weights.get(i));
				fd.writeLine(words.get(i));
			}
			fw.close(); 
			fd.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
