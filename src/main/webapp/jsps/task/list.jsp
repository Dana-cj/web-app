<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">

<nav mt-5>
<ul class="nav nav-underline">
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.dueDateSortingActive}"/>" active="true" aria-current="page" href="/new-web-app/tasks?action=list"><i class="bi bi-sort-down-alt"></i>Sort by due date</a>
  </li>
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.prioritySortingActive}"/>" href="/new-web-app/tasks?action=listTasksSortedByPriority"><i class="bi bi-sort-up"></i>Sort by priority</a>
  </li>
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.statusSortingActive}"/>" href="/new-web-app/tasks?action=listTasksSortedByStatus"><i class="bi bi-sort-down-alt"></i>Sort by status</a>
  </li>
</ul>
</nav>

<div class="d-grid gap-2 d-md-flex justify-content-md-end">
  <a href="/new-web-app/tasks?action=add">
    <button type="button" class="btn btn-secondary btn-sm">+ Add new task</button>
  </a>
</div>

    <table class= "table table-striped">
        <tr>
            <th>Task description</th>
            <th>Initial Date</th>
            <th>Due Date</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Files</th>
            <th></th>
            <th></th>
        </tr>

        <c:forEach items="${requestScope.tasks}" var="task">
        <tr>
            <td><c:out value = "${task.description}"/></td>
            <td "table-warning"><c:out value = "${task.formattedInitialDate}"/></td>
            <td class="table-danger"><c:out value = "${task.formattedDueDate}"/></td>
            <td><c:out value = "${task.priority}"/></td>
            <td><c:out value = "${task.status}"/></td>
            <td>
             <c:forEach items="${task.files}" var="file">
                <a href="/new-web-app/tasks?action=download&fileName=<c:out value = "${file.name}"/>">
                    <p><i class="bi bi-file-earmark"></i><c:out value = "${file.name}"/></p>
                </a>
             </c:forEach>
            </td>

            <td>
                <a href="/new-web-app/tasks?action=edit&taskId=<c:out value = "${task.id}"/>">
                 Edit <i class="bi bi-pencil"></i>
                </a>
                <a href="/new-web-app/tasks?action=delete&taskId=<c:out value = "${task.id}"/>">
                 Delete <i class="bi bi-trash"></i>
                </a>

            </td>
        </tr>
        </c:forEach>

    </table>
</div>

<%@include file="../shared/footer.jsp"%>