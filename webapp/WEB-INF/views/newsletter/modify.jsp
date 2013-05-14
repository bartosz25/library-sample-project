<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript">
$(document).ready(function() {
    $('#modify').click(function() {
        $.post($('#getPreferencies').attr('action'), $('#getPreferencies').serialize(),
            function(response)
            {
                $('#preferencies').html(response);
                $('#getPreferencies').hide();
                // TODO : copier après e-mail et password dans le formulaire
            }
        );
        return false;
    });
});
</script>
<form id="getPreferencies" method="post" action="/newsletter-preferencies/get-values-form">
    <p>E-mail : <input type="text" name="email" id="email" value="${email}" /></p> 
  <sec:authorize access="!hasRole('ROLE_USER')">
<p><input type="password" name="password" id="password" /></p>
</sec:authorize> 
    <input type="submit" name="modify" id="modify" value="Modifier" />
</form>

<div id="preferencies">


</div><!-- preferencies -->