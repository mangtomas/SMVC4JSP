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

String f_idx = ictl.getReq( "f_idx"); //new String( ictl.getReq( "f_idx").getBytes( "8859_1"), "utf-8");

//f_idx = URLDecoder.decode(f_idx, "utf-8");

out.println( iTest.del( f_idx));
%>