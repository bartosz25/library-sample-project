<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 

<strong>Booking</strong>

<form:form modelAttribute="booking" id="bookingForm" method="post">

<spring:eval expression="booking.bookingDate" var="finalDate" />

<form:errors  />
<hr />
  <form:label path="bookingDate">Date</form:label>
  <form:input path="bookingDate" />
  <div><form:errors path="bookingDate" cssClass="error" /></div>
 
  <div><form:errors path="subscriber" cssClass="error" /></div>
 
  <div><form:errors path="bookCopy" cssClass="error" /></div>
  <form:input path="token" /> 
  <form:input path="action" type="hidden" /> 
<form:errors path="token" cssClass="error" />
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>