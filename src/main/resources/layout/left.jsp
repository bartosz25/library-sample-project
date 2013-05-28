<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="LEFT">
    <p><spring:message code="menu.categories" /></p>
    <ul class="menu">
    <c:forEach items="${communs.categories}" var="category">
        <li><a href="/categories/${category}">${category}</a></li>
    </c:forEach>
    </ul>
    <p><spring:message code="menu.pages" /></p>
    <ul class="menu">
    <c:forEach items="${communs.pages.left}" var="page">
        <li><a href="/s_${page.url}">${page.title}</a></li>
    </c:forEach>
    </ul>
</div><!-- LEFT -->
