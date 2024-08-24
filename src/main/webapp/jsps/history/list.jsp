<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">

    <table class= "table table-striped">
        <tr>
            <th>Description</th>
              <th>Status</th>
        </tr>

        <c:forEach items="${requestScope.doableList}" var="doable">
        <tr>
            <td><c:out value = "${doable.description}"/></td>
            <td><c:out value = "${doable.status}"/></td>
        </tr>
        </c:forEach>
    </table>
</div>

<%@include file="../shared/footer.jsp"%>