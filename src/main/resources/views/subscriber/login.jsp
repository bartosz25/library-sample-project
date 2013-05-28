<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<strong>Login page</strong>
	<form name="f" action="<c:url value="j_spring_security_check" />" method="POST">
		<table>
			<tr>
				<td>User:</td>
				<td><input type="text" name="login" value="">
				</td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" name="password" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><input type="checkbox" name="_spring_security_remember_me"  />Remember me<br /><input name="submit" type="submit"
					value="submit" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><input name="reset" type="reset" />
				</td>
			</tr>
		</table>
 
	</form>