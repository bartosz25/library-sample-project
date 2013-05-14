<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
All BOOKS

<c:forEach items="${books}" var="book" > <b>> ${book[0]}</b>
 <b>> ${book[1]}</b> <b>> ${book[2]}</b> <b>> ${book[3]}</b>${book[2].lang}
 <c:set var="translations" value="${book[3].getTranslationsByLang(book[2].lang)}" />
 
	<p><a href="/books/${book[2].name}/${translations.titl}/${book[3].id}">${translations.titl}</a></p>
  ${book[3].bookData.titl}
</c:forEach>