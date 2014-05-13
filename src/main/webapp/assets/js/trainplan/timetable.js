/**
 * Created by star on 5/12/14.
 */

var _default = {
    id: "",
    name: "",
    arrDateTime: "",
    dptDateTime: "",
    trackName: "",
    psg: ""
}
$(function() {
    var train_id = $.url().param("train_id");

    var timeTableModel = new TimeTableModel();
    timeTableModel.loadTable(train_id);
    ko.applyBindings(timeTableModel);

})

function TimeTableModel() {
    var self = this;

    self.timetable = ko.observableArray();

    self.loadTable = function(train_id) {
        $.ajax({
            url: "audit/runplan/stn/" + train_id,
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            if(list.length > 0) {
                for(var i = 0; i < list.length; i++) {
                    self.timetable.push(list[i])
                }
            } else {
                for(var i = 0; i < 20; i++) {
                    self.timetable.push(_default);
                }
            }
        }).faild(function() {

        }).always(function() {
            $("timetable").freezeHeader();
        })
    }
}