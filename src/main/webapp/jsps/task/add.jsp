<%@include file="../shared/header.jsp"%>
<%@ include file="../shared/nav.jsp" %>
<div class="container fluid mt-5">
  <form method="post" action="tasks?action=add" enctype="multipart/form-data">

      <div class="form-group mt-5">
         <label for="project">Project: </label>
         <input type="text" class="form-control" id="project" name="project" placeholder="Project name" >
      </div>

      <div class="form-group mt-5">
        <label for="description">Description: </label>
        <input type="text" class="form-control" id="description" name="description" placeholder="Do ...." required>
      </div>

      <div class="form-group mt-5">
        <label for="dueDate">Due date: </label>
        <input type="date" id="dueDate" name="dueDate" required><br><br>
      </div>

    <div class="form-group mt-5">
        <label class="my-1 mr-2 form-group" for="inlineFormCustomSelectPref">Priority: </label>
        <select class="custom-select my-1 mr-sm-2" id="inlineFormCustomSelectPref" name="priority">
            <option value="HIGH">HIGH</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="LOW">LOW</option>
        </select>
     </div>

      <div class="form-group mt-5">
        <label for="files">File input: </label>
        <input type="file" id="files" name="files" />
        <input type="file" name="files" />
        <input type="file" name="files" />
        <p class="help-block">*******You can save here the files that help you solve the task******</p>
      </div>

      <button type="submit" class="btn btn-primary mt-5">Add new task</button>
    </form>
</div>

 <%@include file="../shared/footer.jsp"%>