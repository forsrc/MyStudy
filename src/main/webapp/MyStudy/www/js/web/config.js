"use strict";

//var SERVER = "http://localhost:8077";
var SERVER = "http://localhost:8077/MyStudy";
//var SERVER      = "http://192.168.2.107:8077";
var WEB_NAME    = "";
var SERVER_TEST = SERVER + WEB_NAME + "/MyStudy/www";
var SERVER_REST = SERVER + WEB_NAME + "/springmvc";

var PATH = "../";

var MY_WEB_URL = {

    "loginJson"        : SERVER_TEST + "/json/login.json",
    "listViewJson"     : SERVER_TEST + "/json/appListView.json",
    "detailJson"       : SERVER_TEST + "/json/detail.json",

    "toLogin"          : SERVER_REST + "/v1.0/login",
    "user_list"        : SERVER_REST + "/v1.0/user",
    "getLoginToken"    : SERVER_REST + "/v1.0/login/getLoginToken",

    "login"            : PATH + "web/login.html",
    "logout"           : PATH + "web/logout.html",
    "home"             : PATH + "web/home.html",
    "user"             : PATH + "web/user.html",
    "demo"             : PATH + "web/demo.html",

    "activity"         : PATH + "web/activity.html",
    "search"           : PATH + "web/search.html",
    "detail"           : PATH + "/web/detail.html",

    "index"            : "/web/login.html"
};

var logEnable = true;
