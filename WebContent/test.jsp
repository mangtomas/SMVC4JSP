<%@page
import= "java.util.HashMap"
import= "java.util.Iterator"

import= "smvcj.Model"
import= "smvcj.Controller"

language     = "java"
contentType  = "text/html; charset=UTF-8"
pageEncoding = "UTF-8"
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
	<title>Insert title here</title>
</head>

<body>
<%
Model      imdl  = new Model( "t_test", null);
Controller ictl  = new Controller( request, response);
imdl.select( "*",  new String( ""));

String idx = new String();
String key = new String();
String val = new String();

for( HashMap< String, String> row_ : imdl.getRows())
{
	idx = row_.get( "f_idx");
	key = row_.get( "f_key");
	val = row_.get( "f_val");
	out.println( key+ " => " +val);
	out.println( "<br/>");
}

out.println( ictl.getCook( "JSESSIONID"));
out.println( "<br/>");

ictl.setSess( "sess_test", "ok");

out.println( ictl.getSess( "sess_test"));
out.println( "<br/>");

Iterator< String> itr = ictl.getCookArr().keySet().iterator();

while( itr.hasNext()){
	key = itr.next();
	val = ictl.getCookArr().get( key);
	
	out.append( "COOKIE : " +key+ " => " +val);
	out.println( "<br/>");
}

itr = ictl.getSessArr().keySet().iterator();

while( itr.hasNext()){
	key = itr.next();
	val = ictl.getSessArr().get( key);
	
	out.append( "SESSION : " +key+ " => " +val);
	out.println( "<br/>");
}

HashMap< String, String> varArr = new HashMap< String, String>();
varArr.put( "aa","aa");
varArr.put( "bb","bb");

out.println( ictl.vars2input( varArr));
%>

</body>
</html>