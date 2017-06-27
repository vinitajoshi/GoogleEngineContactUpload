<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
<link rel="icon" href="${pageContext.request.contextPath}/images/etouch-logo.png"  height="10%"/>
</head>
<body>
<div class="main-container">
<jsp:include page="/JSP/header.jsp"/>
<div class="content-container">
<section class="contact-content">
<div class="container-fluid">
<div class="row">
<div class="container">
<!-- <h1 class="heading">Welcome to eTouch data bridge</h1> -->
<h1 class="heading"></h1>
<div class="divClass"><a href="JSP/contact.jsp">Upload Contacts To Eloqua</a></div>
<div class="divClass"><a href="JSP/sharedList.jsp">Upload Contacts and Link To Shared List</a></div>
 <!-- <div class="divClass">  <a href="googleenginesegmentextract">Delete Contacts</a></div> -->
</div>
</div>
</div>
</section>
</div>
</div>
</body>
</html>