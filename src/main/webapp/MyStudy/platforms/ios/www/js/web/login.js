"use strict";

$(document).ready(function () {
    if (sessionStorage.sessionId) {
        window.location.href = MY_WEB_URL.activity;
        return;
    }

    app.initialize();
    $.material.init();
    $.material.ripples();

    sessionStorage.clear();

    $('input').keypress(function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if ((code == 13) || (code == 10)) {
            $("#login").click();
        }
    });

    $("#login").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        if (username == '' || password == '') {
            $("#loginError").fadeIn();
            return;
        }
        var formData = {
            "username": username,
            "password": password
        };

        $.ajax({
            //type: 'POST',
            type: 'GET',
            url: MY_WEB_URL.loginJson,
            ContentType: 'multipart/form-data',
            data: formData,
            beforeSend: function () {

            },
            success: function (response) {
                console.log(response);
                if (response.username != username) { //TODO
                    $("#loginError").fadeIn();
                    return;
                }
                //var response = JSON.parse(response);
                if (response.id != 0 && response.code == 200) {
                    sessionStorage.sessionId = response.id;
                    sessionStorage.username = username;
                    console.log(sessionStorage.sessionId);
                    window.location.href = MY_WEB_URL.activity;
                    return;
                }
                $("#loginError").fadeIn();
                //window.location.href = "../web/activity.html";
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
                $("#fail").snackbar("show");
            }
        });


    });

    var lastScrollTop = 0;
    $(window).scroll(function (event) {
        var st = $(this).scrollTop();
        if (st > lastScrollTop) {
            //alert('downscroll code');
        } else {
            //alert('upscroll code');
        }
        lastScrollTop = st;
    });


});
