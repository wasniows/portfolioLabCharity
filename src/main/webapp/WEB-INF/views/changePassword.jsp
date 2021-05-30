<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>Document</title>
    <link rel="stylesheet" href="../../resources/css/style.css" />
</head>
<body>
<header class="help">
    <nav class="container container--70">
        <br>
        <ul>
            <li><a href="${pageContext.request.contextPath}/" class="btn btn--without-border">Start</a></li>
            <li><a href="${pageContext.request.contextPath}/#steps" class="btn btn--without-border">O co chodzi?</a></li>
            <li><a href="${pageContext.request.contextPath}/#about-us" class="btn btn--without-border">O nas</a></li>
            <li><a href="${pageContext.request.contextPath}/#help" class="btn btn--without-border">Fundacje i organizacje</a></li>
            <li><a href="${pageContext.request.contextPath}form" class="btn btn--without-border">Przekaż dary</a></li>
            <li><a href="${pageContext.request.contextPath}/#contact" class="btn btn--without-border">Kontakt</a></li>
        </ul>
    </nav>
</header>

<section class="login-page">
    <h2>Zmiana hasła</h2>

    <%--@elvariable id="password" type="password"--%>
    <form:form modelAttribute="password" method="get" action="/user/changePassword">
        <form:errors path="*" cssClass="errorblock" element="div" /><br>
        <div class="form-group">
            <form:hidden  path="email" />
        </div>
        <div class="form-group">
            <form:password path="password" placeholder="Obecne hasło" />
        </div>
        <div class="form-group">
            <form:password path="newPassword" placeholder="Nowe hasło" />
        </div>
        <div class="form-group">
            <form:password path="matchingNewPassword" placeholder="Potwórz nowe hasło" />
        </div>

        <div class="form-group form-group--buttons">
            <button class="btn" type="submit">Zmień hasło</button>
            <a href="/user/profil" class="btn btn--without-border">Anuluj</a>
        </div>
    </form:form>
</section>

<%@ include file="footer.jsp" %>

</body>
</html>