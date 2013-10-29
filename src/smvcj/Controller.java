package smvcj;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

public class Controller
{
	private final HttpServletRequest      request  ;
	private final HttpServletResponse     response ;

	private final String                  method;

	private final HashMap<String, String> req      ;
	private final HashMap<String, String> cook     ;
	private final HashMap<String, String> sess     ;

	private final Util util;

	public Controller( HttpServletRequest _request, HttpServletResponse _response)
	{
		req  = new HashMap< String, String>();
		sess = new HashMap< String, String>();
		cook = new HashMap< String, String>();


		request  = _request;
		response = _response;
		method   = request.getMethod();

		util     = new Util();


		//====================================================================================================
		HashMap< String, String> $_SERVER = new HashMap< String, String>();

		$_SERVER.put( "AUTH_TYPE"      , request.getAuthType());
		$_SERVER.put( "CONTENT_LENGTH" , Integer.toString( request.getContentLength()));
		$_SERVER.put( "CONTENT_TYPE"   , request.getContentType());
		$_SERVER.put( "DOCUMENT_ROOT"  , request.getServletContext().getRealPath("/") );
		$_SERVER.put( "HTTP_XXX_YYY"   , request.getHeader("Xxx-Yyy"));
		$_SERVER.put( "PATH_INFO"      , request.getPathInfo());
		$_SERVER.put( "PATH_TRANSLATED", request.getPathTranslated());
		$_SERVER.put( "QUERY_STRING"   , request.getQueryString());
		$_SERVER.put( "REMOTE_ADDR"    , request.getRemoteAddr());
		$_SERVER.put( "REMOTE_HOST"    , request.getRemoteHost());
		$_SERVER.put( "REMOTE_USER"    , request.getRemoteUser());
		$_SERVER.put( "REQUEST_METHOD" , request.getMethod());
		$_SERVER.put( "SCRIPT_NAME"    , request.getServletPath());
		$_SERVER.put( "SERVER_NAME"    , request.getServerName());
		$_SERVER.put( "SERVER_PORT"    , Integer.toString( request.getServerPort()));
		$_SERVER.put( "SERVER_PROTOCOL", request.getProtocol());
		$_SERVER.put( "SERVER_SOFTWARE", request.getServletContext().getServerInfo());

		StringBuffer line = new StringBuffer();

		line.append( "==================================================");
		line.append( line.toString());

		Log.logWrite( line.toString());

		Iterator< String> $itr = $_SERVER.keySet().iterator();

		String $txt = new String();
		String $key = new String();
		String $val = new String();

		while( $itr.hasNext())
		{
			$key = $itr.next();
			$val = $_SERVER.get( $key);

			$txt = "| " +String.format( "%16s", $key)+ " : " +$val;

			Log.logWrite( $txt);
		}

		Log.logWrite( line.toString());
		//====================================================================================================


		Iterator< String>    itr = _request.getParameterMap().keySet().iterator();
		Enumeration< String> enm = _request.getSession().getAttributeNames();
		String               key = new String();
		String               val = new String();

		if( !itr.hasNext())
		{
			Log.logWrite( "WARN  Controller::Controller( 'request', 'response') : There is NO REQUESTS.");
		}

		while( itr.hasNext())
		{
			key = itr.next();
			val = request.getParameter( key);

			req.put( key,val);
		}

		if( !enm.hasMoreElements())
		{
			Log.logWrite( "WARN  Controller::Controller( 'request', 'response') : There is NO SESSIONS.");
		}

		while( enm.hasMoreElements())
		{
			key = enm.nextElement();
			val = ( String) request.getSession().getAttribute( key);

			sess.put( key, val);
		}

		try
		{
			for( Cookie cookie : request.getCookies())
			{
				cook.put( cookie.getName(), cookie.getValue());
			}
		}
		catch( Exception ex){
			Log.logWrite( "WARN  Controller->Controller( 'request', 'response') : There is NO COOKIES.");
		}

		ArrayList< HashMap< String, String>> arr = new ArrayList< HashMap< String, String>>();
		HashMap< String, String>             map = new HashMap< String, String>();

		map.put( "method", method);

		arr.add( map );
		arr.add( req );
		arr.add( cook);
		arr.add( sess);

		Log.logWrite( "INFO  Controller::Controller( 'request', 'response') : " +util.arrayListHashMapToJSON( arr) );
	}

	@Override
	public void finalize() throws SQLException
	{

	}

	public String getMethod()
	{
		Log.logWrite( "INFO  Controller::getMethod() : " +method);

		return method;
	}


	public String getReq( String _key)
	{
		Log.logWrite( "INFO  Controller::getReq( '" +_key+ "') : " +req.get( _key));

		return req.get( _key);
	}

	public String getCook( String _key)
	{
		Log.logWrite( "INFO  Controller::getCook( '" +_key+ "') : " +cook.get( _key));

		return cook.get( _key);
	}

	public String getSess(String _key)
	{
		Log.logWrite( "INFO  Controller::getSess( '" +_key+ "') : " +sess.get( _key));

		return sess.get(_key);
	}


	public HashMap<String, String> getReqArr()
	{
		if( req.size()>0)
		{
			Log.logWrite( "INFO  Controller::getReqArr() : " +util.hashMapToJSON( req));
		}
		else
		{
			Log.logWrite( "WARN  Controller::getReqArr() : Request array is empty.");
		}

		return req;
	}

	public HashMap<String, String> getCookArr()
	{
		if( cook.size()>0)
		{
			Log.logWrite( "INFO  Controller::getCookArr() : " +util.hashMapToJSON( cook));
		}
		else
		{
			Log.logWrite( "WARN  Controller::getCookArr() : Cookie array is empty.");
		}

		return cook;
	}

	public HashMap<String, String> getSessArr()
	{
		if( sess.size()>0)
		{
			Log.logWrite( "INFO  Controller::getSessArr() : " +util.hashMapToJSON( sess));
		}
		else
		{
			Log.logWrite( "WARN  Controller::getSessArr() : Session array is empty.");
		}

		return sess;
	}


	public boolean setCook( String _key, String _val, int _expire)
	{
		try
		{
			cook.put(_key, _val);

			Cookie cookie = new Cookie(_key, _val);

			cookie.setMaxAge(_expire);

			response.addCookie(cookie);

			Log.logWrite("INFO  Controller::setCook() : Success.");

			return true;
		}
		catch(Exception ex)
		{
			Log.logWrite("ERROR Controller::setCook() : Fail => " +ex.getMessage());

			return false;
		}
	}

	public boolean setSess(String _key, String _val)
	{
		try
		{
			sess.put(_key, _val);

			HttpSession session = request.getSession();

			session.setAttribute(_key, _val);

			Log.logWrite("INFO  Controller::setSess() : Success.");

			return true;
		}
		catch(Exception ex)
		{
			Log.logWrite("ERROR Controller::setSess() : Fail => " +ex.getMessage());

			return false;
		}
	}


	@SuppressWarnings( "rawtypes")
	public boolean upload( String _dstPath)
	{
		try
		{
			MultipartRequest    mreq      = new MultipartRequest( request, _dstPath);
			Enumeration         formNames = mreq.getFileNames();
			String              formName  = new String();
			String              fileName  = new String();

			while( formNames.hasMoreElements())
			{
				formName = (String) formNames.nextElement();

				fileName = mreq.getFilesystemName( formName);

				if( fileName!=null)
				{
					Log.logWrite( "INFO  Controller::upload( '" +_dstPath+ "') : Success => " +fileName);
				}
				else
				{
					Log.logWrite( "WARN  Controller::upload( '" +_dstPath+ "') : Success => " +fileName);
				}
			}

			return true;

		}
		catch ( IOException ex)
		{
			Log.logWrite( "ERROR Controller->upload : Fail => " +ex.getMessage());

			return false;
		}
	}

	public boolean thumbnail( String _srcPath, String _srcFileName, String _dstPath, String _dstFileName, int _maxWidth, int _maxHeight)
	{
		StringBuffer args = new StringBuffer();

		args.append( "'");args.append( _srcPath)    ;args.append( "'");args.append( ", ");
		args.append( "'");args.append( _srcFileName);args.append( "'");args.append( ", ");
		args.append( "'");args.append( _dstPath)    ;args.append( "'");args.append( ", ");
		args.append( "'");args.append( _dstFileName);args.append( "'");args.append( ", ");
		args.append( "'");args.append( _maxWidth)   ;args.append( "'");args.append( ", ");
		args.append( "'");args.append( _maxHeight)  ;args.append( "'");

		try
		{
			int srcWidth  = 0;
			int srcHeight = 0;
			int dstWidth  = 0;
			int dstHeight = 0;
			int dx1       = 0;
			int dy1       = 0;

			BufferedImage   src = ImageIO.read( new File( _srcPath+_srcFileName));
			//Image size calculation
			{

				srcWidth  = src.getWidth();
				srcHeight = src.getHeight();

				if( srcWidth>_maxWidth || srcHeight>_maxHeight)
				{
					if( srcWidth==srcHeight)
					{
						dstWidth  = _maxWidth;
						dstHeight = _maxHeight;

					}
					else
					if( srcWidth>srcHeight)
					{
						dstWidth  = _maxWidth;
						dstHeight = ( int) Math.ceil( ( _maxWidth/srcWidth)*srcHeight);
					}
					else
					{
						dstHeight = _maxHeight;
						dstWidth  = ( int) Math.ceil( ( _maxHeight/srcHeight)*srcWidth);
					}
				}
				else
				{
					dstWidth  = srcWidth;
					dstHeight = srcHeight;
				}

				if( dstWidth <_maxWidth ) dx1 = ( int) Math.ceil( ( _maxWidth -dstWidth )/2); else dx1 = 0;
				if( dstHeight<_maxHeight) dy1 = ( int) Math.ceil( ( _maxHeight-dstHeight)/2); else dy1 = 0;
			}

			Image           tmp = src.getScaledInstance( dstWidth, dstHeight, Image.SCALE_SMOOTH);
			BufferedImage   dst = new BufferedImage( dstWidth, dstHeight, BufferedImage.TYPE_INT_BGR);
			Graphics2D      g2d = dst.createGraphics();

			g2d.drawImage( tmp, dx1, dy1, dstWidth, dstHeight, 0, 0, srcWidth, srcHeight, null);

			ImageIO.write( dst, "jpg", new File( _dstPath+_dstFileName));

			Log.logWrite("INFO  Controller::thumbnail( " +args.toString()+ ") : Success.");

			return true;
		}
		catch(IOException ex)
		{
			Log.logWrite("ERROR Controller::thumbnail( " +args.toString()+ ") : Fail => " +ex.getMessage());

			return false;
		}
	}


	public String vars2input( HashMap< String,String> _allocArr)
	{
		Iterator<String> itr = _allocArr.keySet().iterator();
		StringBuffer     rtn = new StringBuffer();
		String           key = new String();
		String           val = new String();

		rtn.append( "$(function(){\n");

		while( itr.hasNext())
		{
			key = itr.next();
			val = _allocArr.get( key);

			rtn.append("	if( $( '[name=" +key+ "]').get( 0).tagName=='INPUT' && $( '[name=" +key+ "]').attr( 'type')=='radio')\n");
			rtn.append("	{\n");
			rtn.append("		$( '[name=" +key+ "][value=" +val+ "]').prop( 'checked', true);\n");
			rtn.append("	}\n");
			rtn.append("	else\n");
			rtn.append("	if( $( '[name=" +key+ "]').get( 0).tagName=='INPUT' && $( '[name=" +key+ "]').attr( 'type') == 'checkbox')\n");
			rtn.append("	{\n");
			rtn.append("		$( '[name=" +key+ "][value=" +val+ "]').prop( 'checked', true);\n");
			rtn.append("	}\n");
			rtn.append("	else\n");
			rtn.append("	if( $( '#" +key+ "').get( 0).tagName=='INPUT' && $( '#" +key+ "').attr( 'type')=='text')\n");
			rtn.append("	{\n");
			rtn.append("		$( '#" +key+ "').val( '" +val+ "');\n");
			rtn.append("	}\n");
			rtn.append("	else\n");
			rtn.append("	if( $( '#" +key+ "').get( 0).tagName=='SELECT')\n");
			rtn.append("	{\n");
			rtn.append("		$( '#" +key+ "').val( '" +val+ "');\n");
			rtn.append("	}\n");
			rtn.append("	else\n");
			rtn.append("	{\n");
			rtn.append("		$( '#" +key+ "').html( '" +val+ "');\n");
			rtn.append("	}\n");
		}

		rtn.append("});\n");

		Log.logWrite( "INFO  Controller::vars2input( array) : Success.");

		return rtn.toString();
	}

	public String makeTable( String _tableName, String _PKName, ArrayList< HashMap< String,String>> _rows)
	{
		StringBuffer      rtn = new StringBuffer();

		rtn.append( "$(function(){\n");
		rtn.append( "	var tbl = $('#" +_tableName+ "');\n");
		rtn.append( "	var tr0 = tbl.find( 'tr[name=idx]'   ).clone();\n");
		rtn.append( "	var trX = tbl.find( 'tr[name=noRows]').clone();\n");
		rtn.append( "	tbl.find( 'tr[name=idx]'   ).remove();\n");
		rtn.append( "	tbl.find( 'tr[name=noRows]').remove();\n");

		if( _rows.size()>0)
		{
			String idx = new String();

			Iterator< String> itr = null;

			String colName  = new String();
			String colValue = new String();

			for( HashMap< String, String> row : _rows)
			{
				idx = row.get( _PKName);

				rtn.append( "	var tr = tr0.clone();\n");
				rtn.append( "	tr.attr( 'value', '" +idx+ "');\n");

				itr = row.keySet().iterator();

				while( itr.hasNext())
				{
					colName  = itr.next();
					colValue = row.get( colName);

					rtn.append( "	tr.find( '[name=" +colName+ "]').html('" +colValue+ "');\n");
				}

				rtn.append( "	tbl.append(tr);\n");
			}
		}
		else
		{
			rtn.append( "	tbl.append(trx);\n");
		}

		rtn.append( "});\n");

		Log.logWrite( "INFO  Controller::makeTable( '" +_tableName+ "', '" +_PKName+ "', array) : Success.");

		return rtn.toString();
	}

	public String makeSelect( String _selectName, String _valueColName, String _textColName, ArrayList< HashMap< String,String>> _rows, String _firstText)
	{
		StringBuffer      rtn = new StringBuffer();

		rtn.append( "$(function(){\n");
		rtn.append( "	var sel  = $('#" +_selectName+ "');\n");
		rtn.append( "	var opt0 = sel.find( 'option').eq( 0).clone();\n");
		rtn.append( "	sel.children( 'option').remove();\n");

		if( _firstText!=null && !_firstText.equals( ""))
		{
			rtn.append( "	var opt = opt0.clone();\n");
			rtn.append( "	opt.attr( 'value', '');\n");
			rtn.append( "	opt.text( '" +_firstText+ "');\n");
			rtn.append( "	sel.append( opt);\n");
		}

		if( _rows.size()>0)
		{
			String val = new String();
			String txt = new String();

			for( HashMap< String, String> row : _rows)
			{
				val = row.get( _valueColName);
				txt = row.get( _textColName);
				rtn.append( "	var opt = opt0.clone();\n");
				rtn.append( "	opt.attr( 'value', '" +val+ "');\n");
				rtn.append( "	opt.text( '" +txt+ "');\n");
				rtn.append( "	sel.append( opt);\n");
			}
		}

		rtn.append( "});\n");

		Log.logWrite( "INFO  Controller::makeTable( '" +_selectName+ "', '" +_valueColName+ "', '" +_textColName+ "', array, '" +_firstText+ "') : Success.");

		return rtn.toString();
	}


	public void header( String _type)
	{
		String contentType = new String();

		switch( _type.toLowerCase())
		{
			case "javascript":
				contentType = "application/javascript;";
				break;
			case "json":
				contentType = "application/json;";
				break;
			case "xml" :
				contentType = "text/xml;";
				break;
			case "html":
				contentType = "text/html;";
				break;
			default    :
				contentType = "text/html;";
				break;
		}

		response.setHeader( "Content-type", contentType + "charset=UTF-8;");

		Log.logWrite( "INFO  Controller::header( '" +_type+ "') : Success => " +contentType);
	}
}