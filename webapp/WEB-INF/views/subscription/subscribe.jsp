<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<strong>Booking</strong>

<form:form modelAttribute="subscribeForm" id="subscribeForm" method="post">
 
<form:errors  />
<hr />
  <p>mode</p> 
    <form:radiobuttons  path="modeChecked" items="${subscribeForm.modes}" itemLabel="name" itemValue="id" />
  <div><form:errors path="modeChecked" cssClass="error" /></div> 
  <p>type</p>
  <select name="typeChecked">
  <c:forEach items="${subscribeForm.types}" var="entry" > 
    <option value="${entry.key}">${entry.value.code}</option>
  </c:forEach>
  </select>
  <div><form:errors path="typeChecked" cssClass="error" /></div> 
  <p>date de début</p>
  <form:label path="startDate">Date</form:label>
  <input type="text" name="startDate" value="<spring:eval expression="subscribeForm.startDate" />" />
  <div><form:errors path="startDate" cssClass="error" /></div>
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>