<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<strong>Suggestion</strong>

<script type="text/javascript">
$(document).ready(function() {
  $("#ifExists").click(function() {
    $.ajax({
      type: "POST",
      url: $(this).attr('href'),
      data: {title : $("#title").val()},
      dataType: "json",
      success: function(ret)
      {
        // alert("======> " + ret.size); 
        // alert("======> " + ret.list); 
        if(ret.size > 0)
        {
            for(i in ret.list)
            {
                var entry = ret.list[i];
                $('#results').append('<p><a href="javascript: chooseEntry(\'#choose-'+i+'\'); " id="choose-'+i+'" class="choose">'+entry+'</a></p>');
            }
        }
      }
    });
    return false;
  });
});
function chooseEntry(id)
{
    $('#title').val($(id).html());
}
</script>
<a href="/suggestions/if-exists" id="ifExists">check if exists</a>
<form:form modelAttribute="suggestion" id="suggestion" method="post">
  <form:errors  />
  <hr />
  <div id="results">
    
  </div>
  <form:label path="title">Title</form:label>
  <form:input path="title" />
  <div><form:errors path="title" cssClass="error" /></div>
  <button type="submit" id="valid">Save</button>
  <button type="reset">Reset</button>
</form:form>