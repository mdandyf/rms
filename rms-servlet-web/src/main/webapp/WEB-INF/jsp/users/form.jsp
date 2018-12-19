<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix = "rms" uri = "/WEB-INF/tags/link.tld"%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">

    <title>RMS</title>
    <meta name="description" content="Index">
    <meta name="author" content="Mitrais">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
    <rms:link type="stylesheet" href="css/styles.css?v=1.0"/>

    <!--[if lt IE 9]>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.js"></script>
    <![endif]-->
</head>

<body>
<div class="mdl-layout mdl-js-layout mdl-color--grey-100">
    <main class="mdl-layout__content">
        <div class="mdl-card mdl-shadow--6dp">
            <div class="mdl-card__title mdl-color--primary mdl-color-text--white">
                <h2 class="mdl-card__title-text">Acme Co.</h2>
            </div>
            <form action="/users/data" method="post">
            <div class="mdl-card__supporting-text">
                <div class="mdl-textfield mdl-js-textfield">
                    <input class="mdl-textfield__input" type="text" id="username" name="username" value="${user.userName}" />
                    <label class="mdl-textfield__label" for="username">Username</label>
                </div>
                <div class="mdl-textfield mdl-js-textfield">
                    <input class="mdl-textfield__input" type="password" id="userpass" name="userpass" value="${user.password}" />
                    <label class="mdl-textfield__label" for="userpass">Password</label>
                </div>
                <div class="mdl-card__actions mdl-card--border">
                    <input class="mdl-textfield__input" type="hidden" id="userid" name="userid" value="${user.id}" />
                    <input type="submit" id="buttonSave" name="buttonSave" value="Save" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"/>
                </div>
            </div>
            </form>
        </div>
    </main>
</div>
<script src="js/scripts.js"></script>
</body>
</html>
