<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:choose> 
  <c:when test="!${canBorrow}">
  This book can't be booked.
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${canBorrow}">
      DO BORROW
        <c:if test="${cantBookAnymore}">
        CAN'T BOOK ANYMORE
        </c:if>
      </c:when>
      <c:otherwise>
	  DON'T DO BORROW
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>