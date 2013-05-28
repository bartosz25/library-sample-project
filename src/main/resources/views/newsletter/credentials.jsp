<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<b>couponId : ${couponId}</b>
<b>E-mail saisi : ${email}</b> 
<c:forEach items="${preferencies}" var="entry">
  <p>${entry}</p>
</c:forEach>
ezeeesqdqsdsqd ${communs}	sdqsdqsdhgh
<form:form modelAttribute="newsPreferenciesCredentialsForm"> 
<p><form:label path="email">E-mail</form:label><form:input path="email" /></p>
  <div><form:errors path="email" cssClass="error" /></div>
  
  <sec:authorize access="!hasRole('ROLE_USER')">
<p><form:label path="password">Password</form:label><form:password path="password" /></p>
  <div><form:errors path="password" cssClass="error" /></div>
</sec:authorize>
  <button id="save" type="submit" name="_eventId_submit">Save</button>
</form:form>

>>${subscriber}<<