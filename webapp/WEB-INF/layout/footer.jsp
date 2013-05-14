<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="FOOTER">
  <ul class="menu">
  <c:forEach items="${communs.pages.footer}" var="page">
      <li><a href="/s_${page.url}">${page.title}</a></li>
  </c:forEach>
  </ul>
</div><!-- FOOTER -->