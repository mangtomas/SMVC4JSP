<%@page
language     = "java"
contentType  = "text/html; charset=UTF-8"
pageEncoding = "UTF-8"
%><jsp:include page="./inc.head.jsp" flush="false"/>

<jsp:include page="./inc.menu.jsp" flush="false"/>

<!-- SCRIPT { -->
<jsp:include page="./__controller_ui/dml.test.jsp"/>
<!-- } SCRIPT -->

<!-- CONTENTS { -->
<div id="CONTENTS" class="LAYOUT">
	<form name="search_form" action="./dml.test.jsp">
		<table id="search">
			<tr>
				<td>
					<label for="search_key">KEY</label>
					<input type="text" name="search_key" id="search_key">
					<input type="submit" value="검색">
					<span id="search_msg">KEY를 입력하세요</span>
				</td>
			</tr>
		</table>
	</form>
	<table id="list">
		<tr name="idx" val="">
			<td name="f_idx"></td>
			<td name="f_key"></td>
			<td name="f_val"></td>
		</tr>
	</table>
	<input type="button" value="추가" id="btn_add">
	<input type="button" value="편집" id="btn_mod">
	<input type="button" value="삭제" id="btn_del">
</div>
<!-- } CONTENTS -->

<jsp:include page="./inc.foot.jsp" flush="false"/>