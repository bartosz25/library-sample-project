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


<form:form modelAttribute="categoryLangForm" id="categoryLangForm" method="post">
Translations for ${category.alias} : <br />
    <c:forEach items="${categoryLangForm.categoryLang}" var="langForm" varStatus="status">
<p>
<c:set var="errorCode" value="categoryLang[${status.index}].name"/>
<em>ERR<small>${errorsMap[errorCode]}</small></em>
<c:set var="langCode" value="${langForm.categoryLangPK.idLang}"/>
    <label>${langs[langCode].name}</label>  <input name="categoryLang[${status.index}].name" value="${langForm.name}" />
    <input type="hidden" name="categoryLang[${status.index}].transIdLang" value="${langForm.categoryLangPK.idLang}" />
    <input type="hidden" name="categoryLang[${status.index}].transIdCategory" value ="${langForm.categoryLangPK.idCategory}" />
</p>
</c:forEach>
 
  <button type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form>