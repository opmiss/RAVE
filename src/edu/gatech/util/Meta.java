package edu.gatech.util;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Meta {
	
	ArrayList<DocMeta> doc_meta =null; 
	public static ArrayList<TopicMeta> topic_meta=null; 
	
	public Meta(){
		setTopics(); 
	}
	
	public void setDocs(String term){
		Search s = new Search();
		try {
			doc_meta = s.searchDocsFTS(term); 
			for (DocMeta m:doc_meta){
				 m.setWeight(s.getWeight(m.getId())); 
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
	
	public void setAllDocs(){
		Search s = new Search();
		try {
			doc_meta = s.getAllDocs(); 
			for (DocMeta m:doc_meta){
				System.out.println(m.getId()); 
				 m.setWeight(s.getWeight(m.getId())); 
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
	
	public void setDocs1(){
		Search s = new Search();
		try {
			doc_meta = s.searchDocs1(); 
			for (DocMeta m:doc_meta){
				 m.setWeight(s.getWeight(m.getId())); 
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
	public void setDocs2(){
		Search s = new Search();
		try {
			doc_meta = s.searchDocs2(); 
			for (DocMeta m:doc_meta){
				 m.setWeight(s.getWeight(m.getId())); 
			}
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
	
	public void setTopics(){ //set all 25 topics 
		Search s = new Search();
		try {
			topic_meta = s.getTopics(); 
		//	for (TopicMeta tm:topic_meta) tm.print(); 
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
	
	public void add(DocMeta meta){
		doc_meta.add(meta); 
	}
	
	public void filterTopics(int num){ //reduce the number of topics to num
		if (topic_meta==null||topic_meta.size()<num) return; 
		float[] topics = new float[topic_meta.size()]; 
		for (int i=0; i<topics.length; i++) topics[i]=0;
		for (DocMeta m:doc_meta){
			ArrayList<Float> W = m.getWeight();
			int k=0; 
			for (Float w:W){
				topics[k++]+=w; 
			}
		}
		//find highly relevant topics 
		int[] sortid = sort(topics);
		for (DocMeta m:doc_meta){
			ArrayList<Float> w = m.getWeight();
			ArrayList<Float> nw = new ArrayList<Float>(); 
			for (int i=0; i<num; i++){
				//if (!topic_meta.get(sortid[i]).getName().equals("Junk")) 
					nw.add(w.get(sortid[i])); 
			}
			m.setWeight(getNormalized(nw)); 
		}
		ArrayList<TopicMeta> tm = new ArrayList<TopicMeta>();
		ArrayList<String> print = new ArrayList<String>(); 
		for (int i=0; i<num; i++){
			TopicMeta t = topic_meta.get(sortid[i]); 
			  if (t.getName().equals("Junk")) t.setName("Dummy");
				tm.add(t); 
				print.add(t.getName()); 
		}
		topic_meta = tm; 
		System.out.println("There are "+doc_meta.size()+" docs"); 
		System.out.println("the most relevant topics are: "+ print.toString()); 
	}
	
	public void filterTopicsNoDummy(int num){ 
		if (topic_meta==null||topic_meta.size()<num) return; 
		float[] topics = new float[topic_meta.size()]; 
		for (int i=0; i<topics.length; i++) topics[i]=0;
		for (DocMeta m:doc_meta){
			ArrayList<Float> W = m.getWeight();
			int k=0; 
			for (Float w:W){
				topics[k++]+=w; 
			}
		}
		int[] sortid = sort(topics);
		for (DocMeta m:doc_meta){
			ArrayList<Float> w = m.getWeight();
			ArrayList<Float> nw = new ArrayList<Float>(); 
			for (int i=0; i<num; i++){
				if (!topic_meta.get(sortid[i]).getName().equals("Junk")) 
					nw.add(w.get(sortid[i])); 
			}
			m.setWeight(getNormalized(nw)); 
		}
		ArrayList<TopicMeta> tm = new ArrayList<TopicMeta>();
		ArrayList<String> print = new ArrayList<String>(); 
		for (int i=0; i<num; i++){
			TopicMeta t = topic_meta.get(sortid[i]); 
			if (!t.getName().equals("Junk")) {
				tm.add(t); 
				print.add(t.getName()); 
			}
		}
		topic_meta = tm; 
		System.out.println("There are "+doc_meta.size()+" docs"); 
		System.out.println("the most relevant topics are: "+ print.toString()); 
	}
	
	public void filterTopics(List<String> names){ 
		if (topic_meta==null||topic_meta.size()<names.size()) return; 
		ArrayList<TopicMeta> tm = new ArrayList<TopicMeta>();
		int[] id = new int[names.size()]; 
		int i=0; 
		for (int k=0; k<topic_meta.size(); k++){
			TopicMeta tk = topic_meta.get(k); 
			if (names.contains(tk.getName())){
				id[i++] = k; 
				tm.add(tk); 
			}
		}
		topic_meta = tm; 
		for (DocMeta m:doc_meta){
			ArrayList<Float> w = m.getWeight();
			ArrayList<Float> nw = new ArrayList<Float>(); 
			for (i=0; i<names.size(); i++) nw.add(w.get(id[i])); 
			m.setWeight(getNormalized(nw)); 
		}
	}

	private ArrayList<Float> getNormalized(ArrayList<Float> L){
		ArrayList<Float> R= new ArrayList<Float>(); 
		float sum =0; 
		for (Float f:L){
			sum+=f; 
		}
		for (Float f:L){
			R.add(f/sum); 
		}
		return R; 
	}
	
	public void printAll(){
		System.out.println("number of docs: "+doc_meta.size()); 
		for (DocMeta m:doc_meta){
			m.print(); 
		}
	}

	private static int[] sort(float[] a){//selection sort 
		int n = a.length; 
		int[] id = new int[n]; 
		for (int i=0; i<n; i++) id[i] = i; 
		int iMin; 
		for (int j = 0; j < n-1; j++) {
		    /* find the min element in the unsorted a[j .. n-1] */
		    /* assume the min is the first element */
		    iMin = j;
		    /* test against elements after j to find the smallest */
		    for (int i = j+1; i < n; i++) {
		        /* if this element is less, then it is the new minimum */  
		        if (a[i] < a[iMin]) {
		            /* found new minimum; remember its index */
		            iMin = i;
		        }
		    }
		    /* iMin is the index of the minimum element. Swap it with the current position */
		    if ( iMin != j ) {
		        float t = a[iMin]; 
		        a[iMin] = a[j]; 
		        a[j] = t; 
		        int ti = id[iMin]; 
		        id[iMin] = id[j]; 
		        id[j] = ti; 
		    }
		}
		return id; 
	}
	
	public void saveMeta(String name){
		saveWeights(name); 
		saveDocs(name); 
		saveTopics(name); 
	}
	
	 private void saveWeights(String name){
		 try{
			 FileWrite fw = new FileWrite("data/"+name+"/"+name+".weight"); 
			 for (DocMeta dm:doc_meta){
				 fw.writeLine(dm.getWeightString()); 
			 }
			 fw.close(); 
		 }
		 catch (IOException e){
			 e.printStackTrace(); 
		 }
	 }
	
	 private void saveDocs(String name){
		 try{
			 FileWrite fw = new FileWrite("data/"+name+"/"+name+".docmeta"); 
			 for (DocMeta dm:doc_meta){
				 fw.writeLine(dm.getName());
				// System.out.println(dm.getName()); 
			 }
			 fw.close(); 
		 }
		 catch (IOException e){
			 e.printStackTrace(); 
		 }
	 }
	 
	 private void saveTopics(String name){
		 try{
			 FileWrite fw = new FileWrite("data/"+name+"/"+name+".topicmeta"); 
			 for (TopicMeta tm:topic_meta){
				 fw.writeLine(tm.getName()); 
				// System.out.println(tm.getName()); 
			 }
			 fw.close(); 
		 }
		 catch (IOException e){
			 e.printStackTrace(); 
		 }
	 }
	
	public static void main(String[] args)  {
		Meta collection = new Meta(); 
		/*System.out.println("set docs"); 
		collection.setDocs("visual");
		System.out.println("filter topics");
		collection.filterTopics(5); 
		System.out.println("save meta");
		collection.saveMeta("topicviz_visual");*/
		
		/*collection.setAllDocs();
		System.out.println("filter topics");
		collection.filterTopics(6); 
		System.out.println("save meta");
		collection.saveMeta("topicviz_all"); */
		
		/*collection.setDocs1();
		System.out.println("filter topics");
		String[] names = {"Document understanding", "Discourse", "Graphical models", "Semantic relations", "Translation"}; 
		collection.filterTopics(Arrays.asList(names)); 
		System.out.println("save meta");
		collection.saveMeta("topicviz_case1"); */
		collection.setDocs2();
		System.out.println("filter topics"); 
		collection.filterTopicsNoDummy(6); 
		System.out.println("save meta");
		collection.saveMeta("topicviz_case2"); 
	}
}
