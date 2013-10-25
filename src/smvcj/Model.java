package smvcj;

import java.util.HashMap;
import java.util.Iterator;

public class Model extends Database
{
	private HashMap<String, String>	colArr   ;
	private String                  tableName;
	private String                  PKName   ;

	private final Util              util     ;

	public Model( String _tableName, String _PKVal)
	{
		util = new Util();

		setTable( _tableName, _PKVal);
	}

	public boolean setTable( String _tableName, String _PKVal)
	{
		String query = "SHOW COLUMNS FROM " + "`" +_tableName+ "`" + ";";

		if( this.read( query))
		{
			tableName            = _tableName;
			colArr               = new HashMap< String, String>();
			for( HashMap< String, String> row_ : getRows()){
				String Key  = row_.get( "COLUMN_KEY");
				String Name = row_.get( "COLUMN_NAME");
				if( Key.equals( "PRI"))
				{
					PKName = Name;
				}

				colArr.put( Name, "");
			}

			if( PKName==null || PKName.equals(""))
			{
				Log.logWrite( "WARN  Model::setTable ( '" +_tableName+ "', '" +_PKVal+ "') : Primary key is no exist.");
			}

			Log.logWrite( "INFO  Model::setTable ( '" +_tableName+ "', '" +_PKVal+ "') : Table '" +_tableName+ "' read success. => " +PKName+ "," +util.hashMapToJSON( colArr));

			return true;
		}
		else
		{
			Log.logWrite( "INFO  Model::setTable ( '" +_tableName+ "', '" +_PKVal+ "') : Table '" +_tableName+ "' read fail.");

			return true;
		}

	}

	public String getPKName()
	{
		if( PKName==null || PKName.equals( ""))
		{
			Log.logWrite( "WARN  Model::getPKName() : Primary key is not exist.");
		}

		return PKName;
	}

	public String getPKVal()
	{
		String PKVal = colArr.get( PKName);

		if(PKVal==null || PKVal.equals( ""))
		{
			Log.logWrite( "WARN  Model::getPKVal : Primary key value is empty.");
		}

		return PKVal;
	}

	public HashMap< String, String> getColArr()
	{
		if( colArr.size()>0)
		{
			Log.logWrite( "INFO  Model::getColArr() : Success => " +util.hashMapToJSON( colArr));
		}
		else
		{
			Log.logWrite( "WARN  Model::getColArr() : Column array is empty.");
		}

		return colArr;
	}

	public String getColVal( String _key)
	{
		if( colArr.containsKey( _key))
		{
			Log.logWrite( "INFO  Model::getColVal( '" +_key+ "') : Column '" +_key+ "' value is '" +colArr.get( _key)+ "'.");

			return colArr.get( _key);
		}
		else
		{
			Log.logWrite( "WARN  Model::getColVal( '" +_key+ "') : Column '" +_key+ "' value is not exist.");

			return null;
		}
	}

	public boolean setCol( String _key, String _val)
	{
		if( colArr.containsKey( _key))
		{
			colArr.put( _key,_val);

			Log.logWrite( "INFO  Model::setCol( '" +_key+ "', '" +_val+ "') : '" +_key+ "' => '" +colArr.get( _key)+ "'");

			return true;
		}
		else
		{
			Log.logWrite( "WARN  Model::setCol( '" +_key+ "', '" +_val+ "') : Column '" +_key+ "' is not exist.");

			return false;
		}
	}

	public void setColArr( HashMap< String, String> _colArr)
	{
		if( colArr.containsKey( PKName))
		{
			_colArr.put( PKName, getPKVal());
		}

		Log.logWrite( "INFO  Model::setColArr( array) : Success => " +util.hashMapToJSON( _colArr));

		colArr = _colArr;

		return;
	}

	public boolean setPKVal( String _val)
	{
		if( PKName!=null && !PKName.equals( ""))
		{
			colArr.put( PKName, _val);

			Log.logWrite( "INFO  Model::setPKVal( '" +_val+ "') : '" +PKName+ "'=>'" +_val+ "'");

			return true;
		}
		else
		{
			Log.logWrite( "WARN  Model::setPKVal( '" +_val+ "') : Primary key is not exist.");

			return false;
		}
	}

	public boolean select( String _colNames, String _where)
	{
		StringBuffer colNames = new StringBuffer();
		int          cnt      = 0;

		if( _where==null || _where.equals( ""))
		{
			if( getPKVal()!=null && !getPKVal().equals( ""))
			{
				_where = "WHERE " +PKName+ "=" + "'" +getPKVal()+ "'";
			}
		}

		if( _colNames==null || _colNames.equals( ""))
		{
			Iterator<String>  itr        = colArr.keySet().iterator();
			String            key        = new String();

			while( itr.hasNext())
			{
				key = itr.next();

				colNames.append( key);

				if( itr.hasNext()) colNames.append( ",");

				cnt++;
			}
		}
		else
		{
			colNames = new StringBuffer( _colNames);
			cnt      = 1;
		}

		String query = "SELECT " +colNames.toString()+ " FROM " +tableName+ " " +_where+ ";";

		if( cnt>0)
		{
			if( read( query))
			{
				Log.logWrite( "INFO  Model::select( '" +_colNames+ "', '" +_where+ "') : Success => " +query);

				return true;
			}
			else
			{
				Log.logWrite( "ERROR Model::select( '" +_colNames+ "', '" +_where+ "') : Fail =>" +query);

				return false;
			}
		}
		else
		{
			Log.logWrite( "ERROR Model::select( '" +_colNames+ "', '" +_where+ "') : Fail => " +query);

			return false;
		}
	}

	public boolean update( String _where)
	{
		StringBuffer colNames = new StringBuffer();
		int          cnt      = 0;

		if( _where==null)
		{
			if( getPKVal()!=null && !getPKVal().equals( ""))
			{
				_where = "WHERE " +PKName+ "=" + "'" +getPKVal()+ "'";
			}
		}

		Iterator<String>  itr = colArr.keySet().iterator();
		String            key = new String();
		String            val = new String();

		while( itr.hasNext())
		{
			key = itr.next();
			val = colArr.get( key);

			if( !PKName.equals( key))
			{
				if( val.length()>4)
				{
					if( val.substring( 0, 4).equals( "|FN|"))
					{
						val.replace( "|FN|", "");
						colNames.append( key);
						colNames.append( "=");
						colNames.append( val);
					}
					else
					{
						colNames.append( key);
						colNames.append( "=");
						colNames.append( "'");
						colNames.append( val);
						colNames.append( "'");
					}
				}
				else
				{
					colNames.append( key);
					colNames.append( "=");
					colNames.append( "'");
					colNames.append( val);
					colNames.append( "'");
				}

				cnt++;

				if( itr.hasNext()){
					colNames.append( ",");
				}
			}
		}

		String query = "UPDATE " +tableName+ " SET " +colNames +_where+ ";";

		if( cnt>0 && _where!=null && !_where.equals( ""))
		{
			if( save( query))
			{
				Log.logWrite( "INFO  Model::update( '" +_where+ "') : Success => " +query);

				return true;
			}
			else
			{
				Log.logWrite( "ERROR Model::update( '" +_where+ "') : Fail => " +query);

				return false;
			}
		}

		Log.logWrite( "ERROR Model::update( '" +_where+ "') : Fail => " +query);

		return false;
	}

	public boolean insert()
	{
		StringBuffer      colNames = new StringBuffer();
		StringBuffer      vals     = new StringBuffer();
		int               cnt      = 0;

		Iterator<String>  itr      = colArr.keySet().iterator();
		String            key      = new String();
		String            val      = new String();

		while( itr.hasNext())
		{
			key = itr.next();
			val = colArr.get( key);

			if( !PKName.equals( key))
			{
				colNames.append( key);

				if( val.length()>4)
				{
					if( val.substring(0, 4).equals( "|FN|"))
					{
						val.replace( "|FN|", "");
						vals.append( val);
					}
					else
					{
						vals.append( "'");
						vals.append( val);
						vals.append( "'");
					}
				}
				else
				{
					vals.append( "'");
					vals.append( val);
					vals.append( "'");
				}

				cnt++;

				if( itr.hasNext())
				{
					colNames.append( ",");
					vals    .append( ",");
				}
			}
		}

		String  query   = "INSERT INTO " +tableName+ "(" +colNames+ ")" + " VALUES " + "(" +vals+ ")" + ";";

		if( cnt>0)
		{
			String  last_id = new String();

			if( save( query))
			{
				if( read( "SELECT LAST_INSERT_ID() as id"))
				{
					last_id = getRow().get( "id");

					colArr.put( PKName, last_id);

					Log.logWrite( "INFO  Model::insert() : Success => " last_id+ "/" +query);

					return true;
				}
				else
				{
					Log.logWrite( "WARN  Model::insert() : Fail get last inserted id. => " +query);

					return false;
				}
			}
			else
			{
				Log.logWrite( "ERROR Model::insert() : Fail => " +query);

				return false;
			}
		}
		else
		{
			Log.logWrite( "ERROR Model::insert() : Fail => " +query);

			return false;
		}
	}

	public boolean delete( String _where)
	{
		if( _where==null || _where.equals( ""))
		{
			if( PKName!=null && !PKName.equals( ""))
			{
				_where = "WHERE " +PKName+ "=" + "'" +colArr.get( PKName)+ "'";
			}
		}

		String query = "DELETE FROM " +tableName+ " " +_where+ ";";

		if( _where!=null && !_where.equals(""))
		{
			if( save( query))
			{
				Log.logWrite( "INFO  Model::delete( '" +_where+ "') : Success =>" +query);

				return true;
			}
			else
			{
				Log.logWrite( "ERROR Model::delete( '" +_where+ "') : Fail =>" +query);

				return false;
			}
		}
		else
		{
			Log.logWrite( "ERROR Model::delete( '" +_where+ "') : Fail =>" +query);

			return false;
		}
	}
}