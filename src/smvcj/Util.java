package smvcj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Util
{
	public String hashMapToJSON( HashMap< String, String> _src)
	{
		StringBuffer rtn = new StringBuffer();

		if( _src.size()>0)
		{
			Iterator< String> itr = _src.keySet().iterator();

			String key = new String();
			String val = new String();

			rtn.append( "{");

			while( itr.hasNext())
			{
				key = itr.next();
				val = _src.get( key);

				rtn.append( "'" +key+ "' : '" +val+ "'");

				if( itr.hasNext()){
					rtn.append( ",");
				}
			}

			rtn.append( "}");

			return rtn.toString();
		}
		else
		{
			return null;
		}
	}

	public String arrayListHashMapToJSON( ArrayList< HashMap< String, String>> _src)
	{
		int size = _src.size();

		if( size>0)
		{
			StringBuffer rtn = new StringBuffer();

			rtn.append( "[");

			int i = 0;

			for( HashMap< String, String> map : _src)
			{
				rtn.append( hashMapToJSON( map));

				i++;

				if( i<size)
				{
					rtn.append( ",");
				}
			}

			rtn.append( "]");

			return rtn.toString();
		}
		else
		{
			return null;
		}
	}

}
