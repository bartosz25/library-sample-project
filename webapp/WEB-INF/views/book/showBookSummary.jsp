<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="<c:url value="/public/js/jquery-1.8.3.min.js" />"></script>
    <script type="text/javascript" src="<c:url value="/public/css/main.css" />"></script>
	<title>Test application for library</title>
</head>
<body>
<div id="bookSummary">
<c:forEach items="${book}" var="bookO" >
<c:forEach items="${bookO}" var="bookN" >
1. ${bookN}
ddddddddd
</c:forEach>
</c:forEach>
<h1>${book[0][0]}</h1>
<h1>${book[1]}</h1>d

<c:forEach items="${book[0][0].bookCopies}" var="copy" >
1. ${copy} ${copy.isAvailableForBook()}
  <c:if test="${copy.isAvailableForBook()}">
   <a href="/borrowing/do/${copy.id}">borrow</a>
  </c:if> 
</c:forEach>
</div>
</body>
</html>