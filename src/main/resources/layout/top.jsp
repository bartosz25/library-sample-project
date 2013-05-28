<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div id="TOP">
  <ul class="menu">
  <c:forEach items="${communs.pages.top}" var="page">
    <li><a href="/s_${page.url}">${page.title}</a></li>
  </c:forEach>
  </ul>
  <div class="account">
    <ul class="items">
      <sec:authorize access="!hasRole('ROLE_USER')">
      <li><a href="/login"><spring:message code="top.login" /></a></li>
      <li><a href="/register"><spring:message code="top.register" /></a></li>
      </sec:authorize>
      <sec:authorize access="hasRole('ROLE_USER')">
      <li><a href="/logout"><spring:message code="top.logout" /></a></li>
      <li><a href="/account/modify"><spring:message code="top.account" /></a></li>
      <li><a href="/account/modify-password"><spring:message code="top.password" /></a></li>
      <li><a href="/chat/room"><spring:message code="top.chat" /></a></li>
      </sec:authorize>
      <li><a href="/newsletter-preferencies?_eventId=credentials"><spring:message code="top.subNews" /></a></li>
      <li><a href="/newsletter-preferencies/modify"><spring:message code="top.modNews" /></a></li>
      <li><a href="/newsletter/unsubscribe"><spring:message code="top.unsNews" /></a></li>
    </ul>
    <ul class="langs">
      <li><a href="/?language=en"><spring:message code="top.lang.en" /></a></li>
      <li><a href="/?language=fr"><spring:message code="top.lang.fr" /></a></li>
      <li><a href="/?language=pl"><spring:message code="top.lang.pl" /></a></li>
    </ul>
    
  </div>
</div><!-- TOP -->

<!--
<a href="/login">Login</a>
<a href="/logout">Logout</a>
<a href="/account/modify">Account settings</a>
<a href="/account/modify-password">Change password</a>
<a href="/chat/room">Chat</a>
<c:out value="${flowExecutionUrl}"/>
<a href="/newsletter-preferencies?_eventId=credentials">Subscribe to newsletter</a> 
| <a href="/newsletter-preferencies/modify">Modify my newsletter preferencies</a> 
| <a href="/newsletter/unsubscribe">Unsubscribe from newsletter</a>

<a href="?language=en">English</a> | 
<a href="?language=fr">French</a> | 
<a href="?language=pl">Polish</a> | -->