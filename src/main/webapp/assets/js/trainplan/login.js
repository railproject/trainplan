/**
 * Created by star on 5/15/14.
 */
$(function() {
    $("#inputUsername").blur(function() {
        if($("#inputUsername").val().trim().length == 0) {
            return false;
        }
        $.ajax({
            url: "user/" + $("#inputUsername").val().trim() + "/account",
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            $("#inputAccount option").remove();
            for( var i = 0; i < list.length; i++) {
                $("#inputAccount").append("<option value='" + list[i].ACC_ID + "'>" + list[i].ACC_NAME + "</option>");
            }
        }).fail(function() {

        }).always(function() {
        })
    });

    $("#loginForm").submit(function() {
        if($("#inputUsername").val().trim().length == 0) {
            return false;
        }
        if(!$("#inputAccount").val()) {
            return false;
        }
        $("input[name='username']").val($("#inputUsername").val() + "@" + $("#inputAccount").val());
        $("input[name='password']").val($("#inputPassword").val());
    });
});