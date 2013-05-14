<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<strong>Penalty</strong>

<form:form modelAttribute="penaltyForm" id="penaltyForm" method="post">
  <c:forEach items="${penaltyForm.modes}" var="entry" >
    <form:radiobutton id="btn-${entry.id}" path="modeChecked" value="${entry.id}" />${entry.code}
  </c:forEach>
  <div><form:errors path="modeChecked" cssClass="error" /></div> 
  
  <form:checkboxes itemLabel="checkboxLabel" itemValue="penaltyPK" items="${penaltyForm.penalties}" path="penaltiesChecked" />
  <div><form:errors path="penaltiesChecked" cssClass="error" /></div>
  
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>