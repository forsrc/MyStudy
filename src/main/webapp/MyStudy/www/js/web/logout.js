$(document).ready(function () {

    app.initialize();
    $.material.init();
    $.material.ripples();
    sessionStorage.removeItem('activeTab');
    sessionStorage.removeItem('leadId');

    sessionStorage.clear();
    window.location.href = MY_WEB_URL.login;
});
