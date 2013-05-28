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


<form:form modelAttribute="writerLangForm" id="writerLangForm" method="post">
Translations for "${writer.firstname} ${writer.familyname}" : <br />
<c:forEach items="${writerLangForm.writerLang}" var="entry" varStatus="status">
<p>LANG </p>[${status.index}][${statusField.index}] ${entry}
      <c:forEach items="${entry.value}" var="field" varStatus="statusField">
<p><label>${field.key}</label><input name="writerLang['${entry.key}']['${field.key}']" value="${field.value}" />
</c:forEach>
 
</c:forEach>
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>