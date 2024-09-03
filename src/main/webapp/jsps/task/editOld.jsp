<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">
    <form method="post">
       <div class="form-group mt-5">
        <label for="project">Project: </label>
        <input type="text" class="form-control" id="project" name="project" value="<c:out value= "${requestScope.task.project}"/>">
      </div>

      <div class="form-group mt-5">
        <label for="description">Description: </label>
        <input type="text" class="form-control" id="description" name="description" value="<c:out value= "${requestScope.task.description}"/>">
      </div>

      <div class="form-group mt-5">
        <label for="dueDate">Due date: </label>
        <input type="date" id="dueDate" name="dueDate" value= "<c:out value= "${requestScope.task.localDueDate}"/>">
      </div>


    <label class="my-1 mr-2 form-group mt-5" for="inlineFormCustomSelectPref">Priority: </label>
      <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref" name="priority">
        <option selected><c:out value= "${requestScope.task.priority}"/></option>
        <option value="HIGH">HIGH</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="LOW">LOW</option>
      </select>

    <label class="my-1 mr-2 form-group mt-5" for="inlineFormCustomSelectPref2">Status: </label>
      <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref2" name="status">
        <option selected><c:out value= "${requestScope.task.status}"/></option>
        <option value="TO_DO">TO DO</option>
        <option value="IN_PROGRESS">IN PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>

    <h6 class="mt-5">Files attached:</h6>
    <c:forEach items="${requestScope.task.files}" var="file">
                    <p><i class="bi bi-file-earmark"></i><c:out value = "${file.name}"/></p>
    </c:forEach>

      <button type="submit" class="btn btn-primary">Update task</button>
    </form>
</div>

 <%@include file="../shared/footer.jsp"%>