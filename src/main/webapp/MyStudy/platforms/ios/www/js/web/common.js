"use strict";

$(document).ready(function () {

    

});


function saveToken(token) {
    sessionStorage.token = JSON.stringify(TOKEN);
}

function getToken() {
    var token = sessionStorage.token;
    if(!token){
        return {};
    }
    
    return JSON.parse(token);
}


