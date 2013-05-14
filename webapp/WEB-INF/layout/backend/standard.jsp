<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="<c:url value="/public/js/jquery-1.8.3.min.js" />"></script>
<script type="text/javascript">
function deleteCategoryLang(ref)
{
  $.getJSON($(ref).attr('href'), {}, function(response) {
        alert(response.question);
		alert(response.category);
    });
  
}
</script>
	<title>Test application for library</title>
</head>
<body>
  <div id="TOP">
  <sec:authorize access="hasRole('ROLE_ADMIN')">
    <a href="/backend/logout">Logout</a>
    <a href="/backend/category/add">Add category</a>
    <a href="/backend/category-lang/add">Add category translations</a>
    <a href="/backend/writer/add">Add writer</a>
    <a href="/backend/writer-lang/add/1">Add writer translations (id 1)</a>
    <a href="/backend/book/add">Add book</a>
    <a href="/backend/category-lang/delete/18/1/question" onclick="deleteCategoryLang(this); return false;">Delete test category</a>
    <a href="/backend/payment/accept/subscription/1">Accept payment (1st subscription)</a>
    <a href="/backend/payment/accept/penalty/1">Accept payment (1st penalty)</a>
    <a href="/backend/chat/consult">Consult chat</a>
    <a href="/backend/export/excel/borrowings">Export borrowings</a>
    <a href="/backend/reply/write/1">Reply to 1st question</a>
    <a href="/backend/question/read/1">Read 1st question</a>
    <a href="/backend/newsletter/write/0">Write newsletter</a>
    </sec:authorize>
  </div><!-- TOP -->
  <div id="CONTAINER">
     <div id="CONTENT">
      <tiles:insertAttribute name="body" />
    </div><!-- CONTENT -->
  </div><!-- CONTAINER -->
</body>
</html>