<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">

  <form method="post" action="tasks?action=editFiles&taskId=<c:out value = "${requestScope.task.id}"/>" enctype="multipart/form-data">
      <div class="form-group mt-5">
        <label for="description">Description: </label>
        <input type="text" class="form-control" id="description" name="description" value="<c:out value= "${requestScope.task.description}"/>" disabled>
      </div>

      <div class="form-group mt-5">
        <label for="dueDate">Due date: </label>
        <input type="date" id="dueDate" name="dueDate" value= "<c:out value= "${requestScope.task.localDueDate}"/>" disabled>
      </div>


    <label class="my-1 mr-2 form-group mt-5" for="inlineFormCustomSelectPref">Priority: </label>
      <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref" name="priority" disabled>
        <option selected><c:out value= "${requestScope.task.priority}"/></option>
        <option value="HIGH">HIGH</option>
        <option value="MEDIUM">MEDIUM</option>
        <option value="LOW">LOW</option>
      </select>

    <label class="my-1 mr-2 form-group mt-5" for="inlineFormCustomSelectPref2">Status: </label>
      <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref2" name="status" disabled>
        <option selected><c:out value= "${requestScope.task.status}"/></option>
        <option value="TO_DO">TO DO</option>
        <option value="IN_PROGRESS">IN PROGRESS</option>
        <option value="DONE">DONE</option>
      </select>

        <h6 class="mt-5">Files attached:</h6>
            <c:forEach items="${requestScope.task.files}" var="file">
                    <p><i class="bi bi-file-earmark"></i><c:out value = "${file.name}"/></p>
        </c:forEach>

        <div class="form-group mt-5">
            <label for="files">File input</label>
            <input type="file" id="files" name="files" />
            <input type="file" name="files" />
            <input type="file" name="files" />
            <p class="help-block">*******You can save here new files that help you solve the task******</p>
        </div>
      <button type="submit" class="btn btn-primary mt-5">Add files</button>
    </form>
</div>

 <%@include file="../shared/footer.jsp"%>