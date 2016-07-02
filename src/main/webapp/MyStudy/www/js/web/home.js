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
    initialize: function() {
        if(this.initialized){
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
    bindEvents: function() {
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
    onDeviceReady: function() {
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
    receivedEvent: function(id) {

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

function main(){

    if (!sessionStorage.sessionId) {
        window.location.href = MY_WEB_URL.login;
        return;
    }

    init();

    $("#loader").fadeOut("slow", function () {

    });
}

function init(){

    var username = document.getElementById("username");
    username.innerText = sessionStorage.username;

}
