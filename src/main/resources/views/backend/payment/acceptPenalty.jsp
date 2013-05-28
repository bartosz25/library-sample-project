<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Add payment</strong>

<p><em>error is ${error}</em></p>
<c:if test="${error} == true">err
  ${registerError}
  <br />
  ${errors}
</c:if>

<c:if test="${cantAccept} == true">
Pas possible d'accepter le payement
</c:if>
 

<form:form modelAttribute="backendPenaltyForm" id="backendPenaltyForm" method="post">
  
  <form:label path="reference">reference</form:label>
  <form:input path="reference" />
  <div><form:errors path="reference" cssClass="error" /></div>
  <c:forEach items="${backendPenaltyForm.modes}" var="entry" >
    <form:radiobutton id="btn-${entry.id}" path="modeChecked" value="${entry.id}" />${entry.code}
  </c:forEach>
  <div><form:errors path="modeChecked" cssClass="error" /></div> 
  
  <form:checkboxes itemLabel="checkboxLabel" itemValue="penaltyPK" items="${backendPenaltyForm.penalties}" path="penaltiesChecked" />
  <div><form:errors path="penaltiesChecked" cssClass="error" /></div>
  
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>