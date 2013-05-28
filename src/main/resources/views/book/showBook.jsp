<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:forEach items="${book}" var="bookO" >
<c:forEach items="${bookO}" var="bookN" >
1. ${bookN}
<hr />
</c:forEach>
</c:forEach>
<h1>${book[0][0]}</h1>
<h1>${book[1]}</h1>

<c:forEach items="${book[0][0].bookCopies}" var="copy" >
1. ${copy} ${copy.isAvailableForBook()}
  <c:if test="${copy.isAvailableForBook()}">
   <a href="/borrowing/do/${copy.id}">borrow</a>
  </c:if>
<hr />
</c:forEach>
