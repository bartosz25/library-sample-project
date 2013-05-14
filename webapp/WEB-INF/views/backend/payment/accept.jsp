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
 
<form:form modelAttribute="payment" id="payment" method="post">

  <form:label path="reference">reference</form:label>
  <form:input path="reference" />
  <div><form:errors path="reference" cssClass="error" /></div>
  
  <form:label path="paymentMethod">paymentMethod</form:label>
  <form:select itemLabel="name" itemValue="id" multiple="false" items="${payment.paymentMethods}" path="paymentMethod"></form:select>
  <div><form:errors path="paymentMethod" cssClass="error" /></div>
  
  <hr />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>