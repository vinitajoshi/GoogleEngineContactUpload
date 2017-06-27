<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="icon" href="${pageContext.request.contextPath}/images/etouch-logo.png"  height="10%"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Shared List</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/form_validate.js"></script>
<script type="text/javascript">
function ValidateForm(frm) {
if (frm.shredName.value == "") {alert('Name is required.');frm.shredName.focus();return false;}
return true; 
}
</script>
</head>
<body>
<jsp:include page="/JSP/header.jsp"/>
<div class="container">
<h1 class="heading">Shared List Information</h1>
<div id="informationForm">
<div class="form-outer">
<form action="googleenginesharedlistupload" method="post">
<div class="row">
<div class="col-md-6 col-sm-6 textfield-outer-large margin-right50">
                    <label class="hide" for="shredName">Shared name</label>
                    <input class="text-field text-field-large" placeholder="Shared List Name (required)" id="shredName" name="shredName" data-popover-offset="5,5" required data-req="true" tabindex="1" type="text">
                    <span class="error-msg" id="error_msg_shredName" style="display: none;">Please enter Shared List name</span> </div>
<div class="col-md-6 col-sm-6 textfield-outer-large margin-right50">
                    <label class="hide" for="shredName">Bucket name</label>
                    <input class="text-field text-field-large" placeholder="Bucket Name (required)" id="bucketName" name="bucketName" data-popover-offset="5,5" required data-req="true" tabindex="1" type="text">
                    <span class="error-msg" id="error_msg_bucketName" style="display: none;">Please enter bucket name</span> </div>
                    </div>
                    <div class="row">
<div class="col-md-6 col-sm-6 textfield-outer-large margin-right50">
                    <label class="hide" for="fileName">File name</label>
                    <input class="text-field text-field-large" placeholder="File Name (required)" id="fileName" name="fileName" data-popover-offset="5,5" required data-req="true" tabindex="1" type="text">
                    <span class="error-msg" id="error_msg_fileName" style="display: none;">Please enter File name</span> </div> 
<div class="col-md-6 col-sm-6 textfield-outer-large margin-right50">
                    <label class="hide" for="exclusivFileName">Exclusive file name</label>
                    <input class="text-field text-field-large" placeholder="Exclusive File Name (optional)" id="exclusivFileName" name="exclusivFileName" data-popover-offset="5,5" data-req="false" tabindex="1" type="text">
                    <span class="error-msg" id="error_msg_exclusivFileName" style="display: none;">Please enter File name</span> </div>
                    </div>
<div class="row">
                  <div class="col-md-6 col-sm-6 textfield-outer-large margin-right50 submit-cnt">
                    <input id="submit" onclick="submitContactForm()" class="btn btn-primary tertiary-btn xlarge" type="submit" tabindex="9" value="Submit">
                  </div></div>
</form>
</div>
</div>
<div class="divClass">
<c:forEach items="${shardData}" var="user">
<c:if test="${user.shardListPresent!=0}">
<c:if test="${user.totalRecords==0}">
<div style="color:red; ">No records to display. Please provide proper bucket name/file name.</div>
</c:if>
<c:if test="${user.totalRecords!=0}">
     <table border="1">
     <tr><td>Shared List Name</td><td> <c:out value="${user.sharedListName}" /></td></tr>
     <tr><td>Bucket Name</td><td> <c:out value="${user.bucketName}" /></td></tr>
 <tr><td>File Name</td><td> <c:out value="${user.fileName}" /></td></tr>
 <c:if test="${not empty user.exclsvfileName}">
   <tr><td>Exclusive File Name</td><td> <c:out value="${user.exclsvfileName}" /></td></tr>
</c:if>
 <tr><td>Total Records</td><td> <c:out value="${user.totalRecords}" /></td></tr>
 <tr><td>Success Count</td> <td> <c:out value="${user.sucessCount}" /></td></tr>
 <tr><td>Failed Count</td> <td> <c:out value="${user.failedCount}" /></td></tr>
 <tr><td>Total Time in Seconds</td> <td> <c:out value="${user.schedExecTime}" /></td></tr>
</table>
</c:if>
</c:if>
<c:if test="${user.shardListPresent==0}">
<div style="color:red; ">There is no such shared list. Please provide proper shared list name.</div>
</c:if>
</c:forEach>
</div>
</div>

</body>
</html>