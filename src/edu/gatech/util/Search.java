package edu.gatech.util;
import java.util.ArrayList;
import java.util.Vector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Search {
	
	public enum NodeType {
		paper(0), topic(1), word(2);
	    private final int intType;
	    NodeType(int intType) { this.intType = intType; }
	    public int getValue() { return this.intType; }
	}
	
	public enum Column {
		id("id"), type("type"), name("name"), authors("authors"), venue("venue"), year("year"), in_degree("in_degree"),
		out_degree("out_degree"), degree("degree"), pagerank("pagerank");
	    private final String strColumn;
	    Column(String strColumn) { this.strColumn = strColumn; }
	    public String getValue() { return this.strColumn; }
	}
	
	public class Predicate {
		public final Column column;
		public final String strValue;
		public final boolean exactMatch;
		public final boolean isString;
		
		public Predicate(Column column, String value, boolean exactMatch) {
			this.column = column;
			this.strValue = value;
			this.exactMatch = exactMatch;
			this.isString = true;
		}
		public Predicate(Column column, int value) {
			this.column = column;
			this.strValue = new Integer(value).toString();
			this.isString = false;
			this.exactMatch = true;
		}
		public Predicate(Column column, double value) {
			this.column = column;
			this.strValue = new Double(value).toString();
			this.isString = false;
			this.exactMatch = true;
		}
	}
	public class PredicateSet {
		private Vector<Predicate> pSet =  new Vector<Predicate>();
		public void add(Column column, String value, boolean exactMatch) {
			pSet.add(new Predicate(column, value, exactMatch));
		}
		public void add(Column column, int value) {
			pSet.add(new Predicate(column, value));
		}
		public void add(Column column, double value) {
			pSet.add(new Predicate(column, value));
		}
		public Vector<Predicate> getPredicateSet() {
			return this.pSet;
		}
	}
	private Connection connection = null;
	
	public Search() {
		try {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");
			// create a database connection
			this.connection = DriverManager.getConnection("jdbc:sqlite:data/Aan.db");
			// This gives you the list of all tables;
			// rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;");
			// This gives you the sql that creates the table (you can find all
			// column names here)
			//rs = statement.executeQuery("SELECT sql FROM sqlite_master WHERE tbl_name in (SELECT name FROM sqlite_master ORDER BY name)");
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
	}
	
	public void cleanup() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			// connection close failed.
			System.err.println(e);
		}
	}
	
	public Vector<Integer> getNodeIdByMultiplepPredicates(NodeType nodeType, PredicateSet pSet) 
			throws SQLException {
		Vector<Integer> result = new Vector<Integer>();
		if (pSet.getPredicateSet().size() < 1) {
			throw new SQLException("PredicateSet is empty.");
		}
		String strSql = "SELECT id FROM nodes WHERE type = " + nodeType.getValue() + " AND ";
		for (int i = 0 ; i < pSet.getPredicateSet().size(); i++) {
			Predicate p = pSet.getPredicateSet().get(i);
			if (p.isString) {
				strSql += p.column + (p.exactMatch ? " = '" + p.strValue + "' " : " LIKE " + "'%" + p.strValue + "%' "); 
			} else {
				strSql += p.column + " = " + p.strValue;
			}
			if (i != pSet.getPredicateSet().size() - 1) {
				// if not last predicate, pad "AND"
				strSql += " AND ";
			}
		}
		strSql += " ORDER BY id;";
		System.out.println(strSql);
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		ResultSet rs = statement.executeQuery(strSql);
		while (rs.next()) {
			result.add(rs.getInt("id"));
		}
		return result;
	}
	
	public void printPaperNames() throws SQLException {
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		ResultSet rs = statement.executeQuery("SELECT * FROM nodes WHERE type = 0 AND name LIKE '%Visual%' ORDER BY id;");
		while (rs.next()) {
			System.out.println(rs.getString("name") + " | "+ rs.getString("authors")+
					" | "+rs.getInt("year")+" | "+rs.getString("venue")
					);
		}
	}
	
	public ArrayList<DocMeta> searchDocs(String Keyword) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		String Sql = "SELECT * FROM nodes WHERE type = 0 AND name LIKE '%"+Keyword+"%' ORDER BY id;";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<DocMeta> docs = new ArrayList<DocMeta>(); 
		while (rs.next()) {
			docs.add(new DocMeta(rs.getInt("id"), rs.getString("name"), rs.getString("authors"), 
					rs.getString("venue"), rs.getInt("year"))); 
		}
		return docs; 
	}
	
	public ArrayList<DocMeta> getAllDocs() throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		String Sql = "SELECT * FROM nodes WHERE type = 0 ORDER BY id;";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<DocMeta> docs = new ArrayList<DocMeta>(); 
		while (rs.next()) {
			docs.add(new DocMeta(rs.getInt("id"), rs.getString("name"), rs.getString("authors"), 
					rs.getString("venue"), rs.getInt("year"))); 
		}
		return docs; 
	}
	
	public ArrayList<DocMeta> searchDocs1() throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		//String Sql = "SELECT * FROM nodes WHERE type = 0 AND (name LIKE '%Interact%' OR name LIKE '%Visual%' OR name LIKE '%Geom%') ORDER BY id;";
		String Sql = "SELECT * FROM nodes WHERE type = 0 AND (name LIKE '%French%' OR name LIKE '%Chinese%') ORDER BY id;";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<DocMeta> docs = new ArrayList<DocMeta>(); 
		while (rs.next()) {
			docs.add(new DocMeta(rs.getInt("id"), rs.getString("name"), rs.getString("authors"), 
					rs.getString("venue"), rs.getInt("year"))); 
		}
		return docs; 
	}
	
	public ArrayList<DocMeta> searchDocs2() throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		//String Sql = "SELECT * FROM nodes WHERE type = 0 AND (name LIKE '%Interact%' OR name LIKE '%Visual%' OR name LIKE '%Geom%') ORDER BY id;";
		String Sql = "SELECT * FROM nodes WHERE type = 0 AND (name LIKE '%interact%' OR name LIKE '%visual%' OR name LIKE '%geom%') ORDER BY id;";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<DocMeta> docs = new ArrayList<DocMeta>(); 
		while (rs.next()) {
			docs.add(new DocMeta(rs.getInt("id"), rs.getString("name"), rs.getString("authors"), 
					rs.getString("venue"), rs.getInt("year"))); 
		}
		return docs; 
	}
	
	public ArrayList<DocMeta> searchDocsFTS(String Keyword) throws SQLException {
		ArrayList<DocMeta> result = new ArrayList<DocMeta>();
		String strSql = "SELECT * FROM search WHERE type = 0 AND search match '"+Keyword+"'";
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		ResultSet rs = statement.executeQuery(strSql);
		while (rs.next()) {
			result.add(new DocMeta(rs.getInt("id"), rs.getString("name"), rs.getString("authors"), 
					rs.getString("venue"), rs.getInt("year")));
		}
		return result;
	}
	
	public ArrayList<TopicMeta> getTopics() throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		String Sql = "SELECT * FROM nodes WHERE type = 1";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<TopicMeta> topics = new ArrayList<TopicMeta>(); 
		while (rs.next()) {
			topics.add(new TopicMeta(rs.getInt("id"), rs.getString("name"))); 
		}
		return topics; 
	}
	
	public ArrayList<Float> getWeight(int id) throws SQLException{
		Statement statement = this.connection.createStatement();
		statement.setQueryTimeout(30); // set timeout to 30 sec.
		String Sql = "SELECT * FROM edges WHERE target_id = "+id+" AND type=0 ORDER BY source_id;";
		ResultSet rs = statement.executeQuery(Sql);
		ArrayList<Float> ret = new ArrayList<Float>(); 
		while (rs.next()) {
			ret.add(rs.getFloat("weight")); 
		}
		return ret; 
	}
	
	public static void main(String[] args)  {
		Search s = new Search();
		try { 
			/*ArrayList<DocMeta> papers = s.searchDocsFTS("jacob");
			System.out.println("Find "+papers.size()+" papers."); 
			for (DocMeta dm : papers) {
				dm.print(); 
			}*/
			ArrayList<DocMeta> papers = s.getAllDocs();
			System.out.println("Find "+papers.size()+" papers."); 
		}
		catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		s.cleanup();
	}
}