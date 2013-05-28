<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function() {
    $('#save').click(function() {
        $.post($('#newsPreferenciesCredentialsForm').attr('action'), $('#newsPreferenciesCredentialsForm').serialize(),
            function(response)
            {
                if(response.edited)
                {
                    alert("Edited correctly");
                }
                else
                {
                    alert(response.msg);
                }
            }, "json"
        );
        return false;
    });
});
</script>
// TODO : voir si l'on peut inclure une partie de template (extraire form:form dans un JSP et l'inclure ici + en rajoutant les champs e-mail et password cachés)
${wasFound}
<form:form modelAttribute="newsPreferenciesCredentialsForm" action="/newsletter-preferencies/modify">
<p><form:label path="email">Nouvel e-mail</form:label><form:input path="email" /></p>
  <div><form:errors path="email" cssClass="error" /></div>
// TODO : modifier seulement pour non connectés
<p><form:label path="password">Nouveau mot de passe</form:label><form:password path="password" /></p>
  <div><form:errors path="password" cssClass="error" /></div>
    <c:forEach items="${newsPreferenciesCredentialsForm.categories}" var="entry">
        <c:set var="code" value="${entry.key}" />
        <p><label>${newsPreferenciesCredentialsForm.translations[code]}</label>
        <select name="preferencies">
        <c:forEach items="${entry.value}" var="choice">
          <option value="${entry.key}-${choice.key}" ${choice.value.attributes}>${choice.value.label}</option>
        </c:forEach>
        </select></p>
    </c:forEach>
  <form:hidden path="credEmail" />
  <div><form:errors path="credEmail" cssClass="error" /></div>
  <form:hidden path="credPassword" />
  <div><form:errors path="credPassword" cssClass="error" /></div>
  <button id="save" type="submit" name="_eventId_submit">Save</button>
</form:form>