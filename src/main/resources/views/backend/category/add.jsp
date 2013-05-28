<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Add category page</strong>

<p><em>error is ${error}</em></p>
<c:if test="${error} == true">err
  ${registerError}
  <br />
  ${errors}
</c:if>


<spring:message code="backend.label.alias" var="labelAlias"/>

<form:form modelAttribute="category" id="categoryForm" method="post">

  <form:label path="alias">${labelAlias}</form:label>
  <form:input path="alias" /> 
  <div><form:errors path="alias" cssClass="error" /></div>
<hr />
<form:errors  />
<hr />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>