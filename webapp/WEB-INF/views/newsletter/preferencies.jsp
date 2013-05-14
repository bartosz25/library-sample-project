<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<b>couponId : ${couponId}</b>
<b>E-mail saisi : ${email}</b>
<c:forEach items="${preferencies}" var="entry">
  <p>${entry}</p>
</c:forEach> 
<h1>ss ${testPreferencies}</h1>
<h1>ss2 ${testPreferencies2}</h1>
<form:form modelAttribute="newsPreferenciesCredentialsForm">
    <c:forEach items="${newsPreferenciesCredentialsForm.categories}" var="entry">
        <c:set var="code" value="${entry.key}" />
        <p><label>${newsPreferenciesCredentialsForm.translations[code]}</label>
        <select name="preferencies">
        <c:forEach items="${entry.value}" var="choice">
          <option value="${entry.key}-${choice.key}" ${choice.value.attributes}>${choice.value.label}</option>
        </c:forEach>
        </select></p>
    </c:forEach> 
  <button id="save" type="submit" name="_eventId_submit">Save</button>
  <button id="back" type="submit" name="_eventId_previous">Previous</button>
</form:form>