<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form:form modelAttribute="newsletterSubscriber">
  <p><form:label path="email">E-mail</form:label><form:input path="email" /></p>
  <div><form:errors path="email" cssClass="error" /></div>
  <p><form:label path="password">Mot de passe</form:label><form:password path="password" /></p>
  <div><form:errors path="password" cssClass="error" /></div>
  <button id="save" type="submit" name="_eventId_submit">Save</button>
</form:form>