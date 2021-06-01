<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="help">
    <nav class="container container--70">
        <ul class="nav--actions">
            <li class="logged-user">
                <h3>Witaj ${username}</h3>
                <ul class="dropdown">
                    <li><a href="/">Start</a></li>
                    <li><a href="/user/profil">Profil</a></li>
                    <li><a href="/donations">Moje zbiórki</a></li>
                    <li><a href="<c:out value='perform_logout'/>">Wyloguj</a></li>
                </ul>
            </li>
        </ul>
    </nav>
</header>