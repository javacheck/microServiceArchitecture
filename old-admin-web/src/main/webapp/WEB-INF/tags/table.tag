<%@ tag language="java" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>

<%@ attribute name="pageSize" required="false"%>
<%@ attribute name="total" required="false"%>
<%@ attribute name="pages" required="false"%>
<%@ attribute name="pageNo" required="false"%>
<%@ attribute name="style" required="false"%>
<%@ attribute name="id" required="false"%>

<table name="listTable" id="${id }"
	class='table table-hover table-striped table-bordered'
	pageSize="${pageSize }" total="${total }" pages="${pages }"
	pageNo="${pageNo }" style="${style}">
	<jsp:doBody />
</table>
