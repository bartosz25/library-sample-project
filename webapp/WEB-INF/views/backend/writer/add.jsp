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

<form:form modelAttribute="writer" id="categoryForm" method="post">

  <form:label path="firstname">firstname</form:label>
  <form:input path="firstname" /> 
  <div><form:errors path="firstname" cssClass="error" /></div>

  <form:label path="familyname">familyname</form:label>
  <form:input path="familyname" /> 
  <div><form:errors path="familyname" cssClass="error" /></div>

  <form:label path="born">born</form:label>
  <form:input path="born" /> 
  <div><form:errors path="born" cssClass="error" /></div>

  <form:label path="dead">dead</form:label>
  <form:input path="dead" /> 
  <div><form:errors path="dead" cssClass="error" /></div>
<hr />
<form:errors  />
<hr />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>