$(document).ready(function () {
    if (!sessionStorage.sessionId) {
        window.location.href = MY_WEB_URL.login;
        return;
    }
    app.initialize();
    $.material.init();
    $.material.ripples();
    sessionStorage.removeItem('activeTab');
    sessionStorage.removeItem('leadId');
    $(".logout").click(function () {
        sessionStorage.clear();
        window.location.href = MY_WEB_URL.login;
    });
    $("select").dropdown({
        "optionClass": "withripple"
    });
    $(document).on('click', 'td.leadonClick', function () {
        var phone = $(this).children().attr("href");
        if (phone.indexOf("tel") >= 0) {
            window.open(phone, '_system');
        } else {
            return false;
        }
    });

    $().dropdown({
        autoinit: "select"
    });

    $('input').keypress(function (e) {
        var code = (e.keyCode ? e.keyCode : e.which);
        if ((code == 13) || (code == 10)) {
            $(".search").click();
        }
    });

    $(".search").click(function () {

        var lname = $("#lname").val();
        var email = $("#email").val();
        var tel = $("#tel").val();
        var pname = $("#pname").val();

        $.ajax({
            type: 'GET',
            url: MY_WEB_URL.listViewJson
            + '&username=' + sessionStorage.username
            + '&session_id=' + sessionStorage.sessionId
            + '&name=' + lname + '&email=' + email
            + '&phone=' + tel + '&project_name=' + pname,
            dataType: 'json',
            beforeSend: function () {
                $(".search-form").fadeOut(
                    "fast");
                $(".loader").fadeIn("fast");


            },
            success: function (result) {
                var result = $.parseJSON(JSON.stringify(
                    result));
                //alert(result.length);
                console.log(JSON.stringify(result));
                $(".loader").fadeOut("slow",
                    function () {
                        $(".search-results").fadeIn();
                        if (result.length) {
                            $(".result-line").html(
                                "<span class='badge result-badge'>"
                                + result.length
                                + "</span> Lead(s) Found !!"
                            );
                        } else {
                            $(".result-line").html(
                                "<span class='badge result-badge'>"
                                + result.length
                                + "</span> Leads Found !! Try with filters."
                            );
                        }
                        $(".search-results table tbody").html("");
                        $.each(result,
                            function (key, value) {
                                if (value.phone === null) {
                                    var newPhone = '<a href="javascript:void(0)"></a>';
                                } else {
                                    var newPhone = '<a href="tel:'
                                        + value.phone
                                        + '"><i class="material-icons">call</i></a>';
                                }
                                node = '<tr id="' + value.id
                                    + '"><td class="leadonClick">'
                                    + newPhone + '</td><td class="leadnoClick">'
                                    + value.name
                                    + '</td><td class="leadnoClick">'
                                    + value.status + '</td></tr>';

                                $(".search-results table tbody").append(node);

                            });
                        $(".one-badge,.two-badge,.no-badge").html("");
                        var filters = 0;
                        $(".search-form input").each(function (index, element) {
                            if ($(this).val()) {
                                filters++;

                                if (filters == 1) {
                                    $(".one-badge").html($(this).val());
                                }
                                if (filters == 2) {
                                    $(".two-badge").html($(this).val());
                                }
                                if (filters > 2) {
                                    var num = filters - 2;
                                    $(".no-badge").html("+" + num);
                                }
                            }
                        });
                        if (filters == 0) {
                            $(".no-badge").html("No Filters");
                        }
                    });

                $(".modify-filter").removeClass("hidden");

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
                $("#fail").snackbar("show");
            }

        });
    });

    $(".modify-filter").click(function () {

        $(".search-results").fadeOut("fast");
        $(".search-form").fadeIn("slow");
    });


    $(document).on('click',
        '.search-results table tr td.leadnoClick',
        function () {
            var id = $(this).parent().attr("id");
            //alert("aa");
            if (id != null) {
                window.location.href = "leadDetail.html?id=" + id;
            }
        });

    $(window).scroll(function () {
        var height = $(".toolbar").outerHeight();

        if ($(this).scrollTop() > height) {
            //$('.toolbar').slideUp("fast");
            $('.toolbar').css({
                "position": "fixed",
                "top": "0",
                "z-index": "999"
            });
        } else {
            $('.toolbar').css({
                "position": "static"
            });
        }
    });
});
