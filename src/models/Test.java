package models;

import java.util.ArrayList;
import java.util.HashMap;

import smvcj.Model;

public class Test
{
	private final Model model;

	public Test()
	{
		model = new Model( "t_test", null);
	}

	public boolean isKey( String _key)
	{
		String where = "WHERE f_key='" +_key+ "'";

		model.select( "f_idx", where);

		return ( model.getRows().size()>0);
	}

	public ArrayList< HashMap< String, String>> get( String _key,  String _offset, String _limit)
	{
		StringBuffer where = new StringBuffer();
		if( _key!=null && !_key.equals( ""))
		{
			where.append( "WHERE f_key='" +_key+ "' ");
		}

		if(_offset==null || _offset.equals( ""))
		{
			_offset = new String( "0");
		}

		if(_limit!=null && _limit.equals(""))
		{
			where.append( "LIMIT " +_offset +_limit);
		}

		if( model.select( "", where.toString()))
		{
			return model.getRows();
		}
		else
		{
			return null;
		}
	}

	public boolean add( String _key, String _val)
	{
		HashMap< String, String> map = new HashMap< String, String>();

		map.put( "f_key", _key);
		map.put( "f_val", _val);

		model.setColArr( map);

		return model.insert();
	}

	public boolean mod( String _idx, String _key, String _val)
	{
		HashMap< String, String> map = new HashMap< String, String>();

		map.put("f_key", _key);
		map.put("f_val", _val);

		model.setPKVal(_idx);
		model.setColArr(map);

		return model.update(null);
	}

	public boolean del( String _idx)
	{
		model.setPKVal( _idx);

		return model.delete( null);
	}
}