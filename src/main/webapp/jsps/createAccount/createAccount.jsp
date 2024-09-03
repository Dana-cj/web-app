<%@include file="../shared/header.jsp"%>
<div class="card-body text-center mt-3">
         <h1 class="h1 mb-3 pacifico-regular">To do list</h1>
</div>

   <div class="container">
           <div class="row">
               <div class="col-md-6 mx-auto">
                   <div class="card mt-3">
                       <div class="card-body text-center">

                           <c:if test="${requestScope.errorNewAccount != null}">
                               <div class="alert alert-warning"><c:out value="${requestScope.errorNewAccount}"/></div>
                           </c:if>

                           <form method="post">
                               <div class="mb-3">
                                   <h5 class="h5 mb-3 font-weight-normal">Create account</h5>
                               </div>
                               <div class="mb-3">
                                   <input type="text" class="form-control" id="newUsername" name="newUsername" placeholder="Username" required autofocus>
                               </div>
                               <div class="mb-3">
                                   <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="Password" required>
                               </div>
                               <button type="submit" class="btn btn-primary">Create account</button>
                           </form>
                            <c:if test="${requestScope.accountCreated != null}">
                                <div class="alert alert-success"><c:out value="${requestScope.accountCreated}"/></div>
                                <div class="card-body text-center mt-5">
                                    <a href="<c:out value="${pageContext.request.contextPath}"/>/login?action=login">
                                        <h2>Log in</h2>
                                    </a>
                                </div>
                            </c:if>
                       </div>
                   </div>
               </div>
           </div>
   </div>
<%@ include file="../shared/footer.jsp" %>
