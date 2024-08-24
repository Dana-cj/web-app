<%@include file="task/shared/header.jsp"%>
<%@ include file="task/shared/nav.jsp" %>
<div class="container">
   <h1>TO DO LIST<h1>

    <table class= "table table-striped">
        <tr>
            <th>Task description</th>
            <th>Initial Date</th>
            <th>Due Date</th>
             <th>Priority</th>
              <th>Status</th>
            <th></th>
            <th></th>
        </tr>

        <c:forEach items="${requestScope.tasks}" var="task">
        <tr>
            <td><c:out value = "${task.description}"/></td>
            <td><c:out value = "${task.formattedInitialDate}"/></td>
            <td><c:out value = "${task.formattedDueDate}"/></td>
            <td><c:out value = "${task.priority}"/></td>
            <td><c:out value = "${task.status}"/></td>
            <td>
                <a href="/final-project/tasks?action=edit&taskId=<c:out value = "${task.id}"/>">
                 Edit <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                </a>
                <a href="/final-project/tasks?action=delete&taskId=<c:out value = "${task.id}"/>">
                 Delete <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                </a>
            </td>
        </tr>
        </c:forEach>

    </table>
</div>

<%@include file="task/shared/footer.jsp"%>