<%@page
import= "java.util.HashMap"
import= "java.util.ArrayList"
import="java.net.URLDecoder"

import= "smvcj.Controller"
import= "models.Test"

language     = "java"
contentType  = "text/html; charset=UTF-8"
pageEncoding = "UTF-8"
%><%
Controller ictl  = new Controller( request, response);

Test       iTest = new Test();

String f_key = ictl.getReq( "f_key"); //new String( ictl.getReq( "f_key").getBytes( "8859_1"), "utf-8");
String f_val = ictl.getReq( "f_val"); //new String( ictl.getReq( "f_val").getBytes( "8859_1"), "utf-8");

//f_key = URLDecoder.decode(f_key, "utf-8");
//f_val = URLDecoder.decode(f_val, "utf-8");

out.println( iTest.add( f_key, f_val));
%>