<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Add Reply</strong>

<p><em>error is ${error}</em></p>
<c:if test="${error} == true">err
  ${registerError}
  <br />
  ${errors}
</c:if>

<c:if test="${success} == true">
Livre correctement ajouté
</c:if>

<spring:message code="backend.label.alias" var="labelAlias"/>

<form:form modelAttribute="reply" id="reply" method="post">

  <form:label path="content">content</form:label>
  <form:textarea path="content" />
  <div><form:errors path="content" cssClass="error" /></div>
  <div><form:errors path="admin" cssClass="error" /></div>
 
  <div><form:errors path="question" cssClass="error" /></div>

  <hr />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>