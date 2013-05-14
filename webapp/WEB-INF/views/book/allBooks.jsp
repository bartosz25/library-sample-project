<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
All BOOKS

<c:forEach items="${books}" var="book" > <b>> ${book[0]}</b>
 <b>> ${book[1]}</b> <b>> ${book[2]}</b> <b>> ${book[3]}</b>
 <b>> ${book[4]}</b> <b>> ${book[5]}</b> ${book[4].name}
  <h1><a href="${book[4].name}/${book[2].value}/${book[0].id}">${book[2].value}</a></h1>
   $ {book.bookCopies}<br />
</c:forEach>


<div id="pagination">
  <c:forEach var="i" begin="${begin}" end="${end}">
    <c:choose>
      <c:when test="${i == current}">
        <b>${i}</b>
      </c:when>
      <c:otherwise>
        <a href="/books/${i}">${i}</a>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</div>