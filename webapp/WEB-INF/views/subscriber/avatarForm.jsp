<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Register page</strong>

<c:if test="${fileTooBig} == true">
Votre fichier est trop grand.
</c:if>
<p><em>error is ${error}</em></p>
<c:if test="${success} == true">
Votre e-mail a été correctement modifié.
</c:if>
<form:form modelAttribute="subscriber" method="post" enctype="multipart/form-data">

  <form:label path="avatarFile">Avatar</form:label>
  <form:input path="avatarFile" type="file" /> 
  <div><form:errors path="avatarFile" cssClass="error" /></div>
  <form:input path="token" /> 
  <form:input path="action" type="hidden" /> 
<form:errors path="token" cssClass="error" />
  
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>