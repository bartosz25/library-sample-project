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

<c:if test="${success} == true">
Livre correctement ajouté
</c:if>

<spring:message code="backend.label.alias" var="labelAlias"/>

<form:form modelAttribute="bookForm" id="bookForm" method="post">
<p>Alias : <form:input path="book.alias" />
	<form:errors path="book.alias" cssClass="error" htmlEscape="false" /></p> 
    <p><form:checkboxes items="${bookForm.categories}" path="categoriesChecked" itemValue="id" itemLabel="alias" />
	<form:errors path="categoriesChecked" cssClass="error" /></p>
    <p><form:checkboxes items="${bookForm.writers}" path="writersChecked" itemValue="id" itemLabel="fullname" />
	<form:errors path="writersChecked" cssClass="error" /></p>

    <c:forEach items="${bookForm.translations}" var="langEntry">
        <c:forEach items="${langEntry.value}" var="entry">
        <div>${entry.key}
<form:textarea path="translations['${langEntry.key}']['${entry.key}']" />
	<form:errors path="translations['${langEntry.key}']['${entry.key}']" cssClass="error" />
		<!--<textarea name="translations['${langEntry.key}']['${entry.key}']">${entry.value}</textarea>--></div>
      </c:forEach>
    </c:forEach>

    <c:forEach items="${bookForm.copies}" var="copy"  varStatus="i">
      Code <form:input path="copies[${i.index}].code" />
	<form:errors path="copies[${i.index}].code" cssClass="error" />
      State 
<form:radiobuttons path="copies[${i.index}].state" items="${bookStates}" itemValue="id" itemLabel="label" />
	<form:errors path="copies[${i.index}].state" cssClass="error" />
      Condition
    <form:radiobuttons path="copies[${i.index}].condition" items="${bookConditions}" itemValue="id" itemLabel="label"  />  
	<form:errors path="copies[${i.index}].condition" cssClass="error" />
    </c:forEach>
<form:errors path="token" cssClass="error" />
  <form:input path="token" /> 
  <form:input path="action"  /> 
  <hr />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>