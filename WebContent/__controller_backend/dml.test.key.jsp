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

//f_key = URLDecoder.decode( f_key, "utf-8");

out.println( iTest.isKey( f_key));
%>