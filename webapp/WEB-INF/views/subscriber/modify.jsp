<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Register page</strong>

<spring:message code="user.label.login" var="labelLogin"/>
<spring:message code="user.label.password" var="labelPassword"/>
<spring:message code="user.label.passwordRepeat" var="labelPasswordRepeat"/>
<spring:message code="user.label.email" var="labelEmail"/>
<spring:message code="user.register.error" var="registerError"/>

<p><em>error is ${error}</em></p>
<c:if test="${success} == true">
Votre e-mail a été correctement modifié.


</c:if>
<b>Message : ${test}</b>
<form:form modelAttribute="subscriber" id="emailEditForm" method="post">

<hr />
<form:errors  />
<hr /> 
  <form:label path="oldEmail">${labelEmail}</form:label>
  <form:input path="oldEmail" /> 
  <div><form:errors path="oldEmail" cssClass="error" /></div>
  <form:hidden path="email" />   <form:input path="token" /> 
  <form:input path="action" type="hidden" /> 
<form:errors path="token" cssClass="error" />
  
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>