<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<script type="text/javascript" src="<c:url value="/public/js/functionsWebSockets.js" />"></script>
<script type="text/javascript" src="<c:url value="/public/js/fancywebsocket.js" />"></script>
<script type="text/javascript">
var Server = new FancyWebSocket('ws://127.0.0.1:12?j=${chatKey}');
</script>
<script type="text/javascript" src="<c:url value="/public/js/application.js" />"></script>
<p><strong>Etat de connexion : <span id="connState">non connecté</span></strong></p>
<p id="reconnect" style="display:none;"><a href="#" class="reconnect">se connecter au chat</a></p>
<strong>Chat</strong>

<form:form modelAttribute="chat" id="chat" method="post">

<form:errors  />
  <form:label path="text">text</form:label>
  <form:textarea path="text" />
  <div><form:errors path="text" cssClass="error" /></div>
 
  <button id="save" type="submit">Save</button>
  <button type="reset">Reset</button>

</form:form><input id="message" name="input" type="text" />
<div id="log"></div>
<div id="header"></div>
<div id="detect"></div>
<div id="content"></div>
<div id="status"></div>
<ul id="messagesList">

</ul>