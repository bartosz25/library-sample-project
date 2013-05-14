<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<strong>Booking</strong>

<form:form modelAttribute="transferForm" id="transferForm" method="post">
 
<form:errors  />
<hr />
  <form:label path="subscriptionChecked">subscriptionChecked</form:label>
  <form:select itemLabel="formLabel" itemValue="id" multiple="false" items="${transferForm.subscriptions}" path="subscriptionChecked"></form:select>
  <div><form:errors path="subscriptionChecked" cssClass="error" /></div>
  <div><form:errors path="subscriber" cssClass="error" /></div>

  <form:label path="receiver">receiver</form:label>
  <form:input path="receiver" />
  <div><form:errors path="receiver" cssClass="error" /></div>
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>