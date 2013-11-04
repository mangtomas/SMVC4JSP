<%@page
import = "java.util.HashMap"
import = "java.util.ArrayList"

import = "smvcj.Util"
import = "smvcj.Config"
import = "smvcj.Controller"
import = "models.Test"

language     = "java"
pageEncoding = "UTF-8"
%><%
Config      cfg = new Config();

Controller ictl = new Controller( request, response);

String search_key = ictl.getReq( "search_key")!=null ? ictl.getReq( "search_key") : "";

Test iTest = new Test();

ArrayList< HashMap< String, String>> info = new ArrayList< HashMap< String, String>>();

info = iTest.get( search_key, null, null);

HashMap< String, String> map  = new HashMap< String, String>();

map.put( "search_key", search_key);

//out.println( ictl.vars2input( map));
//out.println( ictl.makeTable ( "list", "f_idx", info));

Util util = new Util();

String varsJSON = util.hashMapToJSON( map);
String infoJSON = util.arrayListHashMapToJSON( info);
%>
<script>
//////////////////////////////////////////////////
//
//	Initial
//
//////////////////////////////////////////////////

$(function(){
	<%= "var json = " +varsJSON+ "\n" %>
	json2dom( json);
	
	<%= "var info = " +infoJSON+ "\n" %>
	$( "#list").makeList( "f_idx", info);
});

//////////////////////////////////////////////////
//
//	Controll
//
//////////////////////////////////////////////////

$(function(){
	$( "#btn_add").click(function(){
		var tmp;
		if( tmp = prompt( "key,value")){
			tmp = tmp.split( ',');
			var key = tmp[0];
			var val = tmp[1];
			$.post(
				"<%= cfg.__URL_CTL_BACKEND__ +"add.test.jsp" %>",
				{
					"f_key" : key, //encodeURIComponent( key),
					"f_val" : val  //encodeURIComponent( val)
				}
			)
			.done( function( data){
				location.reload();
			})
			.fail( function(){
				alert( "network error");
			});
		}
	});

	$( "#btn_mod").click(function(){
		var tmp;
		if( idx = prompt( "idx")){
			if( typeof $( "tr[name=idx][value="+idx+"]")[0]!="undefined"){

				init_str
					= $( "tr[name=idx][value="+idx+"]").children( "[name=f_key]").text()
					+ ", "
					+ $( "tr[name=idx][value="+idx+"]").children( "[name=f_val]").text();

				if( tmp = prompt( "idx : " + idx + "\n\n" + "key,value", init_str)){
					tmp = tmp.split( ',');
					var key = tmp[0];
					var val = tmp[1];
					$.post(
						"<%= cfg.__URL_CTL_BACKEND__ +"mod.test.jsp" %>",
						{
							"f_idx" : idx, //encodeURIComponent( idx),
							"f_key" : key, //encodeURIComponent( key),
							"f_val" : val  //encodeURIComponent( val)
						}
					)
					.done(function(data){
						location.reload();
					})
					.fail(function(){
						alert( "network error");
					});
				}
			} else {
				alert( "idx out of range");
			}
		}
	});

	$( "#btn_del").click(function(){
		var tmp;
		if( tmp = prompt( "idx")){
			var idx = tmp;
			$.post(
				"<%= cfg.__URL_CTL_BACKEND__ +"del.test.jsp" %>",
				{
					"f_idx" : idx  //encodeURIComponent( idx)
				}
			)
			.done(function( data){
				location.reload();
			})
			.fail(function(){
				alert( "network error");
			});
		}
	});

	$( "#search_key").keyup(function(){
		$.post(
			"<%= cfg.__URL_CTL_BACKEND__ +"dml.test.key.jsp" %>",
			{
				"f_key" : $( this).val()  //encodeURIComponent( $( this).val())
			}
		)
		.done(function( data){
			if( data.substring( 0,4)=="true"){
				$( "#search_msg").text( "KEY가 존재합니다.");
			}else{
				$( "#search_msg").text( "KEY가 존재하지 않습니다.");
			}
		})
		.fail(function(){

		});
	});
});
</script>