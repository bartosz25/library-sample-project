<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

test main index

${animals.cat}


${books}
<c:forEach items="${books}" var="book">
<b>${book}</b>
</c:forEach>

<hr />

<spring:message code="user.label.login" var="labelLogin"/>
<spring:message code="user.label.password" var="labelPassword"/>
<spring:message code="user.label.passwordRepeat" var="labelPasswordRepeat"/>
<spring:message code="user.label.email" var="labelEmail"/>

<form:form modelAttribute="subscriber" id="registerForm" method="post">

<form:label path="login">
${labelLogin}
</form:label>
<form:input path="login" />

<div>
<form:errors path="login" cssClass="error" />
</div>

<form:label path="labelPassword">
${labelPassword}
</form:label>
<form:input path="labelPassword" /> 
<div>
<form:errors path="labelPassword" cssClass="error" />
</div>

<form:label path="labelEmail">
${labelEmail}
</form:label>
<form:input path="labelEmail" /> 
<div>
<form:errors path="labelEmail" cssClass="error" />
</div>

  <button type="submit">Save</button>
<button type="reset">Reset</button>
</form:form>