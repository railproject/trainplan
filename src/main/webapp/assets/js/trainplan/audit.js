/**
 * Created by star on 5/10/14.
 */
$(function(){
    $("#date_selector").datepicker({format: "yyyy-mm-dd"});
    var date = $.url().param("date");
    if (date) {
        $("#date_selector").val(date);
    } else {
        $("#date_selector").datepicker('setValue', new Date());
    }
});