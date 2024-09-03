<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">

<div class="card-body text-center mt-3">
         <h1 class="h1 mb-3 pacifico-regular">To do list</h1>
</div>
<nav mt-5>
<ul class="nav nav-underline">
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.dueDateSortingActive}"/>" active="true" aria-current="page" href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=list&project=<c:out value="${requestScope.project}"/>"><i class="bi bi-sort-down-alt"></i>Sort by due date</a>
  </li>
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.prioritySortingActive}"/>" href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=listTasksSortedByPriority&project=<c:out value="${requestScope.project}"/>"><i class="bi bi-sort-up"></i>Sort by priority</a>
  </li>
  <li class="nav-item">
    <a class="nav-link <c:out value="${requestScope.statusSortingActive}"/>" href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=listTasksSortedByStatus&project=<c:out value="${requestScope.project}"/>"><i class="bi bi-sort-down-alt"></i>Sort by status</a>
  </li>
  <li class="nav-item dropdown">
      <a class="nav-link dropdown-toggle <c:out value="${requestScope.filterActive}"/>" data-bs-toggle="dropdown" role="button" aria-expanded="false"><i class="bi bi-funnel-fill"></i></i>Filter by project</a>
      <ul class="dropdown-menu">
       <li>
            <a class="dropdown-item" href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=list&project=all" name="project" value="all"/>
            All projects
            </a>
       </li>
      <c:forEach items="${requestScope.projects}" var="project">
        <li>
            <a class="dropdown-item" href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=list&project=<c:out value = "${project}"/>" name="project" value="<c:out value = "${project}"/>">
            <c:out value = "${project}"/>
            </a>
        </li>
      </c:forEach>
      </ul>
  </li>
</ul>
</nav>

<div class="d-grid gap-2 d-md-flex justify-content-md-end">
  <a href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=add">
    <button type="button" class="btn btn-secondary btn-sm">+ Add new task</button>
  </a>
</div>

    <table class= "table table-striped">
        <tr>
            <th>Project</th>
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
        <tr <c:if test="${task.isOverdue}"> class="table-danger"</c:if>>
            <td><c:out value = "${task.project}"/></td>
            <td>
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=editTask&taskId=<c:out value = "${task.id}"/>">
                             <c:out value = "${task.description}"/></i>
                </a>
            </td>
            <td><c:out value = "${task.formattedInitialDate}"/></td>
            <td><c:out value = "${task.formattedDueDate}"/></td>
            <td><c:out value = "${task.priority}"/></td>
            <td><c:out value = "${task.status}"/></td>
            <td>
             <c:forEach items="${task.files}" var="file">
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=download&fileName=<c:out value = "${file.name}"/>">
                    <p><i class="bi bi-file-earmark"></i><c:out value = "${file.name}"/></p>
                </a>
             </c:forEach>
            </td>
            <td>
                <a href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=editTask&taskId=<c:out value = "${task.id}"/>">
                  Edit Task <i class="bi bi-pencil"></i>
                 </a>

                <a href="<c:out value="${pageContext.request.contextPath}"/>/tasks?action=delete&taskId=<c:out value = "${task.id}"/>">
                 Delete <i class="bi bi-trash"></i>
                </a>
            </td>
        </tr>
        </c:forEach>
    </table>
</div>

<%@include file="../shared/footer.jsp"%>