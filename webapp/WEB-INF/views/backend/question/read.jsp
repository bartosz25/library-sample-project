<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Read question</strong>

<p>${question.title}</p>
<p>${question.content}</p>
<p><spring:eval expression="question.date" /></p>
<p>${question.subscriber.login}</p>