<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:choose>
  <c:when test="${notFound}">
    Borrowing was not found.
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${result}">
        Book was correctly returned.
      </c:when>
      <c:otherwise>
        Book wasn't correctly returned.
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>