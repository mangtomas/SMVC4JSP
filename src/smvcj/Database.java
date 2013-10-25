package smvcj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Database{

	private Connection							conn;
	private ResultSet							rs  ;
	private ResultSetMetaData 					rsmd;
	private Statement							stmt;
	private HashMap<String, String>				row ;
	private ArrayList<HashMap<String, String>>	rows;

	public Database()
	{
		row  = new HashMap<String, String>();
		rows = new ArrayList<HashMap<String, String>>();

		try
		{
			Class.forName( "com.mysql.jdbc.Driver").newInstance();
		}
		catch(Exception ex)
		{
			Log.logWrite( "ERROR Database::Database() : " + ex.getMessage());
		}

		try
		{
			Config cfg = new Config();

			conn   = DriverManager.getConnection( "jdbc:mysql://" +cfg.__DB_HOST__+ "/" +cfg.__DB_NAME__, cfg.__DB_USER__, cfg.__DB_PASS__);

			Log.logWrite( "INFO  Database::Database() : Database connection success.");
		}
		catch( SQLException ex)
		{
			Log.logWrite( "ERROR Database::Database() : " + ex.getMessage());
		}
	}

	@Override
	public void finalize() throws SQLException
	{
		//if(   rs != null) {  rs.close();  rs = null;}
		//if( stmt != null) {stmt.close();stmt = null;}
		//if( conn != null) {conn.close();conn = null;}
	}

	public void clear()
	{
		row  = new HashMap< String, String>();
		rows = new ArrayList< HashMap< String, String>>();
	}

	public boolean read( String _query)
	{
		clear();

		try
		{
			stmt = conn.createStatement();
			rs   = stmt.executeQuery( _query);
			rsmd = rs  .getMetaData();

			try
			{
				int    cnt = rsmd.getColumnCount();
				String key = new String();
				String val = new String();

				while( rs.next())
				{
					row = new HashMap< String,String>();
					for( int i=1; i<=cnt; i++)
					{
						key  = rsmd.getColumnName( i);
						val  = rs  .getString( i);
						row.put( key, val);
					}

					rows.add( row);
				}

				Log.logWrite( "QUERY Database::read( '" +_query+ "') : Success.");

				return true;
			}
			catch( SQLException ex)
			{
				row  = null;
				rows = null;

				Log.logWrite( "ERROR Database::read( '" +_query+ "') : " +ex.getMessage());

				return false;
			}
		}
		catch(Exception ex)
		{
			Log.logWrite( "ERROR Database::read( '" +_query+ "') :" +ex.getMessage());

			return false;
		}
	}

	public boolean save( String _query)
	{
		clear();

		try
		{
			stmt = conn.createStatement();
			stmt.executeUpdate( _query);

			Log.logWrite( "QUERY Database::save( '" +_query+ "') : Success.");

			return true;
		}
		catch( Exception ex)
		{
			Log.logWrite( "ERROR Database::save( '" +_query+ "') : " + ex.getMessage());

			return false;
		}
	}

	public HashMap<String, String> getRow()
	{
		if( row==null)
		{
			Log.logWrite( "WARN  Database::getRows() : Row is null.");
		}
		return row;
	}

	public ArrayList<HashMap<String, String>> getRows()
	{
		if( rows==null)
		{
			Log.logWrite( "WARN  Database::getRows() : Rows is null.");
		}
		return rows;
	}

	public String getJSON()
	{
		if( rows!=null)
		{
			int		cnt = rows.size();
			String	tmp = new String();
			String	key = new String();
			String	val = new String();
			String	str = new String();

			for(int i=0; i<cnt; i++)
			{
				Iterator<String> itr = row.keySet().iterator();

				while(itr.hasNext())
				{
					key = itr.next();
					val = rows.get(i).get(key);
					str = "'" + key + "':";
					tmp = val;

					if(tmp!=null)
					{
						tmp = tmp.replace("'" , "`" );
						tmp = tmp.replace("\"", "``");
					}

					str += "'" + tmp + "',";
				}

				str += ",";
				str  = str.replaceAll(",,", "");
				str += "},";
			}

			str += ",";
			str  = str.replaceAll(",,", "");

			Log.logWrite( "INFO  Database::getJSON() : Success => " +str);

			return str;
		}
		else
		{
			Log.logWrite( "WARN  Database::getJSON() : Rows is null.");

			return null;
		}
	}

	public String getXML()
	{
		if( rows!=null)
		{
			int          cnt = rows.size();
			String       key = new String();
			String       val = new String();
			StringBuffer str = new StringBuffer();

			str.append("<rows>");

			for(int i=0; i<cnt; i++)
			{
				str.append("<row>");

				Iterator<String> itr = row.keySet().iterator();

				while(itr.hasNext())
				{
					key = itr.next();
					val = rows.get(i).get(key);

					str.append("<"+key+">" + val + "</"+key+">");
				}

				str.append("</row>");
			}

			str.append("</rows>");

			Log.logWrite( "INFO  Database::getXML() : Success => " +str);

			return str.toString();
		}
		else
		{
			Log.logWrite( "WARN  Database::getXML() : Rows is null.");

			return null;
		}
	}
}