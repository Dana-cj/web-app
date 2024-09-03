<%@include file="../shared/header.jsp"%>
<div class="card-body text-center mt-3">
         <h1 class="h1 mb-3 pacifico-regular">To do list</h1>



    <div class="alert alert-success"><c:out value="${requestScope.accountCreated}"/></div>
    <div class="card-body text-center mt-5">
        <a href="<c:out value="${pageContext.request.contextPath}"/>/login?action=login">
            <h2>Log in</h2>
        </a>
    </div>
</div>
<%@ include file="../shared/footer.jsp" %>
