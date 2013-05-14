<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<strong>Question</strong>

<form:form modelAttribute="question" id="question" method="post">

<form:errors  />
<hr />
  <form:label path="title">title</form:label>
  <form:input path="title" />
  <div><form:errors path="title" cssClass="error" /></div>
  <form:label path="content">content</form:label>
  <form:textarea path="content" />
  <div><form:errors path="content" cssClass="error" /></div>
 
  <div><form:errors path="subscriber" cssClass="error" /></div>
 
  <div><form:errors path="lang" cssClass="error" /></div>
  <form:input path="token" /> 
  <form:input path="action" type="hidden" /> 
<form:errors path="token" cssClass="error" />
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>