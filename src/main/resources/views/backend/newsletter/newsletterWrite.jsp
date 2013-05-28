<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

 
<form:form modelAttribute="newsletterWriteForm">
    <form:label path="title">title</form:label><form:input path="title" /><div><form:errors path="title" cssClass="error" /></div>
    <form:label path="text">text</form:label><form:textarea path="text" /><div><form:errors path="text" cssClass="error" /></div>
  <form:label path="startTime">Date</form:label>
  <input type="text" name="startTime" value="<spring:eval expression="newsletterWriteForm.startTime" />" />
  <div><form:errors path="startTime" cssClass="error" /></div>
	<p>Receivers criterion</p>
    <c:forEach items="${newsletterWriteForm.categories}" var="entry">
        <c:set var="code" value="${entry.key}" />
        <p><label>${newsletterWriteForm.translations[code]}</label>
        <select name="preferencies">
          <option value="">choose criteria</option>
        <c:forEach items="${entry.value}" var="choice">
          <option value="${entry.key}-${choice.key}" ${choice.value.attributes}>${choice.value.label}</option>
        </c:forEach>
        </select></p>
    </c:forEach> <div><form:errors path="preferencies" cssClass="error" /></div>
  <button id="save" type="submit" name="_eventId_submit">Save</button>
  <button id="back" type="submit" name="_eventId_previous">Previous</button>
</form:form>