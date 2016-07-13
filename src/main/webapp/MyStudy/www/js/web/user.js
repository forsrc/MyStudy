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


$(document).ready(function () {
    app.initialize();
});

var myScroll;

function main() {


    init();
    getList();
}

var TOKEN = null;
function init() {
    //alert(window.screen.height)
    try{
        device;
    }catch (e){
       var device = null;
    }


    $("#wrapper").css("height",
        (window.screen.height
            - 175
            //- 50
            + 5
            - (device ? (device.platform === "Android" ? 12 : 0) :  0)
        ) + "px");

    var username = document.getElementById("username");
    username.innerText = sessionStorage.username;
    TOKEN = getToken();
}

var url = MY_WEB_URL.user_list;
function list(callback, start, size) {

    $.ajax({
        type: 'GET',
        async: true,
        url: url,
        dataType: 'json',
        data: {
            start: start,
            size: size,
            token: TOKEN.token
        },
        beforeSend: function () {
            $("#loader").fadeIn("fast");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR + " --> " + textStatus, errorThrown);
            $("#fail").snackbar("show");
        },
        success: function (response) {
            $("#loader").fadeOut("slow", function () {
                console.log(response);
                if (typeof(callback) === "function") {
                    callback(response);
                }

            });
        }
    });

}

function scroll() {

}

var START = 0;
var SIZE = 10;

function getList(start, size) {
    start = start || 0;
    size = size || 10;
    var trs = $("#tab1-table").find("tbody").find("tr");
    list(function (response) {
        var list = response.return;
        var length = list.length;

        var tr = null;
        for (var i = 0; i < length; i++) {
            //li = document.createElement('tr');
            //li.innerText = 'Generated row ' + (++generatedCount);
            //el.insertBefore(li, el.childNodes[0]);
            if (start + i >= length) {
                $("tbody").append("<tr data-id-'{0}'><td></td><td>{1}</td><td>{2}</td><td>{3}</td></tr>"
                    .formatStr([list[i].id, list[i].username, (list[i].isAdmin ? "Y" : "N"), list[i].createOn]));
                continue;
            }
            tr = trs[start + i];

            if (!$(tr).attr("data-id")) {
                $(tr).html("<td></td><td>{0}</td><td>{1}</td><td>{2}</td>"
                    .formatStr([list[i].username, (list[i].isAdmin ? "Y" : "N"), list[i].createOn]));
                $(tr).attr("data-id", list[i].id);
            } else {
                $(trs[0]).before("<tr data-id-'{0}'><td></td><td>{1}</td><td>{2}</td><td>{3}</td></tr>"
                    .formatStr([list[i].id, list[i].username, (list[i].isAdmin ? "Y" : "N"), list[i].createOn]));
            }

        }
        START = $("#tab1-table").find("tbody").find("tr").length;

    }, start, size);
}


// Functions to simulate "refresh" and "load" on pull-down/pull-up
var generatedCount = 0;
function pullDownAction(theScroller) {
    setTimeout(function () {	// <-- Simulate network congestion, remove setTimeout from production!
        /*var el, li, i;
         el = document.getElementById('thelist');

        for (i = 0; i < 3; i++) {
         li = document.createElement('li');
         li.innerText = 'Generated row ' + (++generatedCount);
         el.insertBefore(li, el.childNodes[0]);
         }*/
        getList(START, SIZE)
        theScroller.refresh();		// Remember to refresh when contents are loaded (ie: on ajax completion)
    }, 1000);	// <-- Simulate network congestion, remove setTimeout from production!
}
function pullUpAction(theScroller) {
    setTimeout(function () {	// <-- Simulate network congestion, remove setTimeout from production!
        /*var el, li, i;
         el = document.getElementById('thelist');

         for (i = 0; i < 3; i++) {
         li = document.createElement('li');
         li.innerText = 'Generated row ' + (++generatedCount);
         el.appendChild(li, el.childNodes[0]);
         }*/
        //getList(START, SIZE);
        theScroller.refresh();		// Remember to refresh when contents are loaded (ie: on ajax completion)
    }, 1000);	// <-- Simulate network congestion, remove setTimeout from production!
}


var IScrollPullUpDown = function (wrapperName, iScrollConfig, pullDownActionHandler, pullUpActionHandler) {
    var iScrollConfig, pullDownActionHandler, pullUpActionHandler, pullDownEl, pullDownOffset, pullUpEl, scrollStartPos;
    var pullThreshold = 5;
    var me = this;

    function showPullDownElNow(className) {
        // Shows pullDownEl with a given className
        pullDownEl.style.transitionDuration = '';
        pullDownEl.style.marginTop = '';
        pullDownEl.className = 'pullDown ' + className;
    }

    var hidePullDownEl = function (time, refresh) {
        // Hides pullDownEl
        pullDownEl.style.transitionDuration = (time > 0 ? time + 'ms' : '');
        pullDownEl.style.marginTop = '';
        pullDownEl.className = 'pullDown scrolledUp';

        // If refresh==true, refresh again after time+10 ms to update iScroll's "scroller.offsetHeight" after the pull-down-bar is really hidden...
        // Don't refresh when the user is still dragging, as this will cause the content to jump (i.e. don't refresh while dragging)
        if (refresh) setTimeout(function () {
            me.myScroll.refresh();
        }, time + 10);
    };

    function initIScroll() {
        var wrapperObj = document.querySelector('#' + wrapperName);
        var scrollerObj = wrapperObj.children[0];

        if (pullDownActionHandler) {
            // If a pullDownActionHandler-function is supplied, add a pull-down bar at the top and enable pull-down-to-refresh.
            // (if pullDownActionHandler==null this iScroll will have no pull-down-functionality)
            pullDownEl = document.createElement('div');
            pullDownEl.className = 'pullDown scrolledUp';
            pullDownEl.innerHTML = '<span class="pullDownIcon"></span><span class="pullDownLabel">Pull down to refresh...</span>';
            scrollerObj.insertBefore(pullDownEl, scrollerObj.firstChild);
            pullDownOffset = pullDownEl.offsetHeight;
        }
        if (pullUpActionHandler) {
            // If a pullUpActionHandler-function is supplied, add a pull-up bar in the bottom and enable pull-up-to-load.
            // (if pullUpActionHandler==null this iScroll will have no pull-up-functionality)
            pullUpEl = document.createElement('div');
            pullUpEl.className = 'pullUp';
            pullUpEl.innerHTML = '<span class="pullUpIcon"></span><span class="pullUpLabel">Pull up to load more...</span>';
            scrollerObj.appendChild(pullUpEl);
        }

        me.myScroll = new IScroll(wrapperObj, iScrollConfig);

        me.myScroll.on('refresh', function () {
            if ((pullDownEl) && (pullDownEl.className.match('loading'))) {
                pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Pull down to refresh...';
                if (this.y >= 0) {
                    // The pull-down-bar is fully visible:
                    // Hide it with a simple 250ms animation
                    hidePullDownEl(250, true);

                } else if (this.y > -pullDownOffset) {
                    // The pull-down-bar is PARTLY visible:
                    // Set up a shorter animation to hide it

                    // Firt calculate a new margin-top for pullDownEl that matches the current scroll position
                    pullDownEl.style.marginTop = this.y + 'px';

                    // CSS-trick to force webkit to render/update any CSS-changes immediately: Access the offsetHeight property...
                    pullDownEl.offsetHeight;

                    // Calculate the animation time (shorter, dependant on the new distance to animate) from here to completely 'scrolledUp' (hidden)
                    // Needs to be done before adjusting the scroll-positon (if we want to read this.y)
                    var animTime = (250 * (pullDownOffset + this.y) / pullDownOffset);

                    // Set scroll positon to top
                    // (this is the same as adjusting the scroll postition to match the exact movement pullDownEl made due to the change of margin-top above, so the content will not "jump")
                    this.scrollTo(0, 0, 0);

                    // Hide pullDownEl with the new (shorter) animation (and reset the inline style again).
                    setTimeout(function () {	// Do this in a new thread to avoid glitches in iOS webkit (will make sure the immediate margin-top change above is rendered)...
                        hidePullDownEl(animTime, true);
                    }, 0);

                } else {
                    // The pull-down-bar is completely off screen:
                    // Hide it immediately
                    hidePullDownEl(0, true);
                    // And adjust the scroll postition to match the exact movement pullDownEl made due to change of margin-top above, so the content will not "jump"
                    this.scrollBy(0, pullDownOffset, 0);
                }
            }
            if ((pullUpEl) && (pullUpEl.className.match('loading'))) {
                pullUpEl.className = 'pullUp';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull up to load more...';
            }
        });

        me.myScroll.on('scrollStart', function () {
            scrollStartPos = this.y; // Store the scroll starting point to be able to track movement in 'scroll' below
        });

        me.myScroll.on('scroll', function () {
            if (pullDownEl || pullUpEl) {
                if ((scrollStartPos == 0) && (this.y == 0)) {
                    // 'scroll' called, but scroller is not moving!
                    // Probably because the content inside wrapper is small and fits the screen, so drag/scroll is disabled by iScroll

                    // Fix this by a hack: Setting "myScroll.hasVerticalScroll=true" tricks iScroll to believe
                    // that there is a vertical scrollbar, and iScroll will enable dragging/scrolling again...
                    this.hasVerticalScroll = true;

                    // Set scrollStartPos to -1000 to be able to detect this state later...
                    scrollStartPos = -1000;
                } else if ((scrollStartPos == -1000) &&
                    (((!pullUpEl) && (!pullDownEl.className.match('flip')) && (this.y < 0)) ||
                    ((!pullDownEl) && (!pullUpEl.className.match('flip')) && (this.y > 0)))) {
                    // Scroller was not moving at first (and the trick above was applied), but now it's moving in the wrong direction.
                    // I.e. the user is either scrolling up while having no "pull-up-bar",
                    // or scrolling down while having no "pull-down-bar" => Disable the trick again and reset values...
                    this.hasVerticalScroll = false;
                    scrollStartPos = 0;
                    this.scrollBy(0, -this.y, 0);	// Adjust scrolling position to undo this "invalid" movement
                }
            }

            if (pullDownEl) {
                if (this.y > pullDownOffset + pullThreshold && !pullDownEl.className.match('flip')) {
                    showPullDownElNow('flip');
                    this.scrollBy(0, -pullDownOffset, 0);	// Adjust scrolling position to match the change in pullDownEl's margin-top
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Release to refresh...';
                } else if (this.y < 0 && pullDownEl.className.match('flip')) { // User changes his mind...
                    hidePullDownEl(0, false);
                    this.scrollBy(0, pullDownOffset, 0);	// Adjust scrolling position to match the change in pullDownEl's margin-top
                    pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Pull down to refresh...';
                }
            }
            if (pullUpEl) {
                if (this.y < (this.maxScrollY - pullThreshold) && !pullUpEl.className.match('flip')) {
                    pullUpEl.className = 'pullUp flip';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Release to load more...';
                } else if (this.y > (this.maxScrollY + pullThreshold) && pullUpEl.className.match('flip')) {
                    pullUpEl.className = 'pullUp';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull up to load more...';
                }
            }
        });

        me.myScroll.on('scrollEnd', function () {
            if ((pullDownEl) && (pullDownEl.className.match('flip'))) {
                showPullDownElNow('loading');
                pullDownEl.querySelector('.pullDownLabel').innerHTML = 'Loading...';
                pullDownActionHandler(this);	// Execute custom function (ajax call?)
            }
            if ((pullUpEl) && (pullUpEl.className.match('flip'))) {
                pullUpEl.className = 'pullUp loading';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Loading...';
                pullUpActionHandler(this);	// Execute custom function (ajax call?)
            }
            if (scrollStartPos = -1000) {
                // If scrollStartPos=-1000: Recalculate the true value of "hasVerticalScroll" as it may have been
                // altered in 'scroll' to enable pull-to-refresh/load when the content fits the screen...
                this.hasVerticalScroll = this.options.scrollY && this.maxScrollY < 0;
            }
        });

    }

    window.addEventListener('load', function () {
        initIScroll()
    }, false);
};


var scroller = new IScrollPullUpDown('wrapper', {
    probeType: 2,
    bounceTime: 250,
    bounceEasing: 'quadratic',
    mouseWheel: false,
    scrollbars: true,
    fadeScrollbars: true,
    interactiveScrollbars: false
}, pullDownAction, pullUpAction);


document.addEventListener('touchmove', function (e) {
    e.preventDefault();
}, false);

