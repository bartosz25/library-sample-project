<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<b>E-mail saisi : ${email}</b>
<b>couponId : ${couponId}</b>
<c:forEach items="${preferencies}" var="entry">
  <p>${entry}</p>
</c:forEach>

if newsPreferenciesCredentialsForm != null : faire ça
<c:if test="${newsPreferenciesCredentialsForm != null}">
  <form:form modelAttribute="newsPreferenciesCredentialsForm">  
  TODO : faire un test et ne pas afficher password si newsPreferenciesCredentialsForm.isSubscriber == true
<p><form:label path="password">Password</form:label><form:password path="password" /></p>
  <div><form:errors path="password" cssClass="error" /></div>

</form:form>
</c:if>
<c:if test="${newsPreferenciesCredentialsForm == null}">
<a href="${flowExecutionUrl}&_eventId=submit">confirm</a>
</c:if>
<a href="${flowExecutionUrl}&_eventId=credentials">correct e-mail</a>
<a href="${flowExecutionUrl}&_eventId=preferencies">correct preferencies</a>
