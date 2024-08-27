<%@include file="../shared/header.jsp"%>
<div class="card-body text-center mt-3">
         <h1 class="h1 mb-3 pacifico-regular">To do list</h1>
</div>
<div class="container">
        <div class="row">
            <div class="col-md-6 mx-auto">
                <div class="card mt-3">
                    <div class="card-body text-center">
                        <c:if test="${requestScope.error != null}">
                            <div class="alert alert-danger"><c:out value="${requestScope.error}"/></div>
                        </c:if>

                        <form method="post">
                            <div class="mb-3">
                                <h5 class="h5 mb-3 font-weight-normal">Please log in</h5>
                            </div>
                            <div class="mb-3">
                                <input type="text" class="form-control" id="username" name="username" placeholder="Username" required autofocus>
                            </div>
                            <div class="mb-3">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                            </div>
                            <div class="mb-3 form-check">
                                <input type="checkbox"  id="rememberMe" value="remember-me">
                                <label class="form-check-label" for="rememberMe">rememberMe</label>
                            </div>
                            <button type="submit" class="btn btn-primary">Log in</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
</div>

<div class="card-body text-center mt-3">
         <h4 class="h4 mb-3 font-weight-normal">OR</h4>
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
                                   <input type="text" class="form-control" id="newusername" name="newusername" placeholder="Username" required autofocus>
                               </div>
                               <div class="mb-3">
                                   <input type="password" class="form-control" id="newpassword" name="newpassword" placeholder="Password" required>
                               </div>
                               <button type="submit" class="btn btn-primary">Create account</button>
                           </form>
                            <c:if test="${requestScope.accountCreated != null}">
                                <div class="alert alert-success"><c:out value="${requestScope.accountCreated}"/></div>
                            </c:if>
                       </div>
                   </div>
               </div>
           </div>
   </div>
<%@ include file="../shared/footer.jsp" %>
