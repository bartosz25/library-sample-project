<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Register page</strong>

<spring:message code="user.label.passwordOld" var="labelOldPassword"/>
<spring:message code="user.label.password" var="labelPassword"/>
<spring:message code="user.label.passwordRepeat" var="labelPasswordRepeat"/>
<spring:message code="user.register.error" var="registerError"/>

<p><em>error is ${error}</em></p>
<c:if test="${error} == true">err
  ${registerError}
  <br />
  ${errors}
</c:if>
<b>Message : ${test}</b>
<form:form modelAttribute="subscriber" id="passwordForm" method="post">

<hr />
<form:errors  />
<hr /> 
  <form:label path="oldPassword">${labelOldPassword}</form:label>
  <form:password path="oldPassword" /> 
  <div><form:errors path="oldPassword" cssClass="error" /></div>
  
  <form:label path="password">${labelPassword}</form:label>
  <form:password path="password" value="" /> 
  <div><form:errors path="password" cssClass="error" /></div>

  <form:label path="passwordRepeated">${labelPasswordRepeat}</form:label>
  <form:password path="passwordRepeated" /> 
  <div><form:errors path="passwordRepeated" cssClass="error" /></div>  
<form:input path="token" /> 
  <form:input path="action" type="hidden" /> 
<form:errors path="token" cssClass="error" />
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>