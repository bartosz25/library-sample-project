<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <script type="text/javascript" src="<c:url value="/public/js/jquery-1.8.3.min.js" />"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/public/css/main.css" />" /> 
	<title>Test application for library</title>
</head>
<body>
  <div id="CONTAINER">
    <tiles:insertAttribute name="top" />
    <div id="BODY">
      <tiles:insertAttribute name="left" />
      <div id="CONTENT">
      <tiles:insertAttribute name="body" />
      </div><!-- CONTENT --->
    </div><!-- BODY --->
    <tiles:insertAttribute name="footer" />
  </div><!-- CONTAINER -->
</body>
</html>