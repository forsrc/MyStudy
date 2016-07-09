/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
"use strict";
var app = {
    initialized: false,
    // Application Constructor
    initialize: function () {
        if (this.initialized) {
            console.log(new Date() + " -> Initialized.");
            return;
        }
        this.bindEvents();
        this.initialized = true;
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        document.addEventListener("pause", this.onPause, false);
        document.addEventListener("resume", this.onResume, false);
        document.addEventListener("backbutton", this.onBackKeyDown, false);
        document.addEventListener("menubutton", this.onMenuKeyDown, false);
        document.addEventListener("searchbutton", this.onSearchKeyDown, false);
        document.addEventListener("startcallbutton", this.onStartCallKeyDown, false);
        document.addEventListener("endcallbutton", this.onEndCallKeyDown, false);
        document.addEventListener("volumedownbutton", this.onVolumeDownKeyDown, false);
        document.addEventListener("volumeupbutton", this.onVolumeUpKeyDown, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function () {
        app.receivedEvent('deviceready');
    },
    onPause: function () {
        console.log(new Date() + " -> onPause()");
        // Handle the pause event
    },
    onResume: function () {
        console.log(new Date() + " -> onResume()");
        setTimeout(function () {
            // TODO: do your thing!
        }, 0);
    },
    onBackKeyDown: function () {
        console.log(new Date() + " -> onBackKeyDown()");
        // Handle the back button
    },
    onMenuKeyDown: function () {
        console.log(new Date() + " -> onMenuKeyDown()");
        // Handle the back button
    },
    onSearchKeyDown: function () {
        console.log(new Date() + " -> onSearchKeyDown()");
        // Handle the search button
    },
    onStartCallKeyDown: function () {
        console.log(new Date() + " -> onStartCallKeyDown()");
        // Handle the start call button
    },
    onEndCallKeyDown: function () {
        console.log(new Date() + " -> onEndCallKeyDown()");
        // Handle the end call button
    },
    onVolumeDownKeyDown: function () {
        console.log(new Date() + " -> onVolumeDownKeyDown()");
        // Handle the volume down button
    },
    onVolumeUpKeyDown: function () {
        console.log(new Date() + " -> onVolumeUpKeyDown()");
        // Handle the volume up button
    },
    // Update DOM on a Received Event
    receivedEvent: function (id) {

        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        //receivedElement.setAttribute('style', 'display:block;');
        receivedElement.setAttribute('style', 'display:none;');

        console.log(new Date() + ' -> Received Event: ' + id);

        main();
    }
};

function main() {
    if (sessionStorage.sessionId) {
        toNextPage();
        return;
    }

    init();

    sessionStorage.clear();

    binding();

    scroll();

    getLoginToken();
}

var MY_AES = null;
var MY_RSA_4_CLIENT = null;
var My_RSA_4_SERVER = null;
var TOKEN = null;

function getLoginToken() {

    var rsaKey = new RSAKey();
    rsaKey.generate(1024, "10001");
    MY_RSA_4_CLIENT = new MyRsa(rsaKey.n, "65537", rsaKey.d, true);

    var rsa4Client = Base64.encode(rsaKey.n.toString());

    $.ajax({
        type: 'POST',
        url: MY_WEB_URL.getLoginToken,
        ContentType: 'multipart/form-data',
        data: {
            rsa4Client: rsa4Client
        },
        beforeSend: function () {

        },
        success: function (response) {
            console.log(response);

            if (response.status != 200) {
                showFail("Get login token failed, please try later.");
                return;
            }


            console.log("rn --> " + Base64.decode(response.return.rsa4Server));
            var rsa4Server = Base64.decode(response.return.rsa4Server);
            console.log("ak --> " + Base64.decode(response.return.ak));
            var ak = MY_RSA_4_CLIENT.decrypt(response.return.ak);
            var ai = MY_RSA_4_CLIENT.decrypt(response.return.ai);
            console.log("ak --> " + ak);
            console.log("ai --> " + ai);
            MY_AES = new MyAes(ak, ai, true);
            My_RSA_4_SERVER = new MyRsa(rsa4Server, "65537", null, true);
            TOKEN = {
                ak: ak,
                ai: ai,
                rsa4Server: rsa4Server,
                loginToken: MY_AES.decrypt(response.return.loginToken),
                loginTokenTime: response.return.loginTokenTime
            };
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown);
            showFail("Can not connect the service.");
        }
    });
}

function showFail(msg) {
    $("#failMessage").attr("data-content", "<i class='material-icons times'>error_outline</i>&nbsp;&nbsp;" + msg);
    $("#failMessage").snackbar("show");
}

function binding() {

    $('input').keypress(function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if ((code == 13) || (code == 10)) {
            $("#login").click();
        }
    });

    $("#login").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();

        toLogin(username, password);
    });
}

function toLogin(username, password) {
    if (username == '' || password == '') {
        $("#loginError").fadeIn();
        return;
    }

    if (TOKEN == null) {
        showFail("Get login token failed, please try later.");

        setTimeout(function () {
            getLoginToken();
        }, 200);
    }

    var formData = {
        //"username": MY_AES.encrypt(username),
        //"password": MY_AES.encrypt(password),
        "username": My_RSA_4_SERVER.encrypt(username),
        "password": My_RSA_4_SERVER.encrypt(password),
        "loginToken": TOKEN.loginToken
    };
    $.ajax({
        type: 'POST',
        url: MY_WEB_URL.toLogin,
        ContentType: 'multipart/form-data',
        data: formData,
        beforeSend: function () {

        },
        success: function (response) {
            console.log(response);

            if (response.status != 200) {
                showLoginException();
                return;
            }


            var rsa4Server = Base64.decode(response.return.rsa4Server);
            console.log("ak --> " + Base64.decode(response.return.ak));
            var ak = MY_RSA_4_CLIENT.decrypt(response.return.ak);
            var ai = MY_RSA_4_CLIENT.decrypt(response.return.ai);
            console.log("ak --> " + ak);
            console.log("ai --> " + ai);

            MY_AES = new MyAes(ak, ai, true);


            TOKEN = {
                ak: ak,
                ai: ai,
                rsa4Server: rsa4Server,
                rsa4ClinetN: MY_RSA_4_CLIENT.bin.toString(),
                rsa4ClinetD: MY_RSA_4_CLIENT.bid.toString(),
                /*
                loginTokenTime: response.return.loginTokenTime,
                loginToken: MY_AES.decrypt(response.return.loginToken),
                id: MY_AES.decrypt(response.return.id),
                isAdmin: MY_AES.decrypt(response.return.isAdmin)
                */
                loginToken: MY_RSA_4_CLIENT.decrypt(response.return.loginToken),
                id: MY_RSA_4_CLIENT.decrypt(response.return.id),
                isAdmin: MY_RSA_4_CLIENT.decrypt(response.return.isAdmin)
            };

            sessionStorage.sessionId = TOKEN.id;
            sessionStorage.username = username;
            sessionStorage.isAdmin = TOKEN.isAdmin;
            console.log(username + " --> " + sessionStorage.sessionId);
            sessionStorage.token = JSON.stringify(TOKEN);


            toNextPage();

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown);
            showAjaxFail();
        }
    });

}

function toNextPage() {
    window.location.href = MY_WEB_URL.home;
}

function showLoginException() {
    $("#loginError").fadeIn();
}

function showAjaxFail() {
    $("#fail").snackbar("show");
}

function init() {
    $.material.init();
    $.material.ripples();

    var passPhrase = "forsrc@163.com";

// The length of the RSA key, in bits.
    var bits = 1024;

    var privateKey = cryptico.generateRSAKey(passPhrase, bits);
    console.log("privateKey --> " + privateKey);
    var publicKey = cryptico.publicKeyString(privateKey);
    console.log("publicKey --> " + publicKey);

    var plainText = "hello world";

    var encryptionResult = cryptico.encrypt(plainText, publicKey);
    console.log(encryptionResult);
    var decryptionResult = cryptico.decrypt(encryptionResult.cipher, privateKey);
    console.log(decryptionResult);

    var rsa = new RSAKey();
    rsa.generate(1024, "10001");
    console.log(rsa);
    //console.log(rsa.n);
    //console.log(rsa.e);
    //console.log(rsa.d);

}

function scroll() {
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
}
