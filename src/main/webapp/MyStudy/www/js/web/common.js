$(document).ready(function () {

    myStudyInit();

});


function myStudyInit(){
    app.initialize();
    $.material.init();
    $.material.ripples();
}

function myStudyToolbar(){
    $(window).scroll(function () {
        var $toolbar = $("#toolbar");
        if(!$toolbar){
            return;
        }
        var height = $toolbar.outerHeight();

        if ($(this).scrollTop() > height) {
            //$('.toolbar').slideUp("fast");
            $toolbar.css({
                "position": "fixed",
                "top": "0",
                "z-index": "999"
            });
        } else {
            $toolbar.css({
                "position": "static"
            });
        }
    });
}