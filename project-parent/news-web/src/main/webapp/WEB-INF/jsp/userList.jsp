<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale scope="session" value="${param.lang}"/>
    <fmt:setBundle basename="messages"/>
    <title>All users</title>
</head>
<body>
<%@include file="/WEB-INF/include/menu.jsp"%>
<%@include file="/WEB-INF/include/header.jsp"%>

<table style="width: 100%">
    <tr>
        <td>User ID</td>
        <td>Username</td>
        <td>Role</td>
        <td>Actions</td>
    </tr>
    <c:forEach items="${userList}" var="user">
        <tr>
            <td><c:out value="${user.id}"/></td>
            <td><c:out value="${user.username}"/></td>
            <td><c:out value="${user.role}"/></td>
            <td></td>
        </tr>
    </c:forEach>
</table>

<%@include file="/WEB-INF/include/footer.jsp"%>
</body>
</html>
