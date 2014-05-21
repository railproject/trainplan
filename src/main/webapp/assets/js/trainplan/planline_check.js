/**
 * Created by star on 5/21/14.
 */

$(function() {
    var app = new ApplicationModel();

    ko.applyBindings(app);
});

// ----------- 应用模型 -------------------
function ApplicationModel() {
    var self = this;

    var tableModel = new TableModel();

    $("#checkBtn").bind('click', {}, tableModel.autoCheck);

    self.tableModel = ko.observable(tableModel);

    self.paramModel = ko.observable(new ParamModel(tableModel));


}

// ########### 页面参数模型 ###############
function ParamModel(tableModel) {
    var self = this;

    self.bureauList = ko.observableArray();

    self.bureau = ko.observable();

    $.ajax({
        url: "base/bureau/all",
        method: "GET",
        contentType: "application/json; charset=UTF-8"
    }).done(function(list) {
        self.bureauList.removeAll();
        for( var i = 0; i < list.length; i++) {
            self.bureauList.push(list[i]);
        }
    }).fail(function() {

    }).always(function() {
    })

    $("#date_selector").datepicker({format: "yyyy-mm-dd"}).on('changeDate', function (ev) {
        tableModel.loadTable(moment(ev.date).format("YYYYMMDD"));
    });;
    var date = $.url().param("date");
    if (date) {
        $("#date_selector").val(date);
    } else {
        $("#date_selector").datepicker('setValue', new Date());
    }
}

// ################# 列表模型 #############
function TableModel() {
    var self = this;

    self.planList = ko.observableArray();

    self.loadTable = function() {
        var date = moment($("#date_selector").val()).format("YYYYMMDD");
        $.ajax({
            url: "audit/plan/runplan/" + date + "/" + $("#bureau option:selected").val() + "/1",
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            self.planList.removeAll();
            for( var i = 0; i < list.length; i++) {
                self.planList.push(new Plan(list[i]));
            }
            // 表头固定
            $("#plan_table").freezeHeader();
        }).fail(function() {

        }).always(function() {

        })
    };


    self.autoCheck = function() {
        ko.utils.arrayForEach(self.planList(), function(plan) {
//            if(plan.dailyLineFlag() != "已上图" || !plan.dailyLineId()) {
//                plan.isTrainInfoMatch(-1);
//                plan.isTimeTableMatch(-1);
//                plan.isRoutingMatch(-1);
//            } else {
                $.ajax({
                    url: "audit/plan/" + plan.id() + "/line/" + plan.dailyLineId() + "/check",
                    method: "GET",
                    contentType: "application/json; charset=UTF-8"
                }).done(function(data) {
                    plan.isTrainInfoMatch(data.isTrainInfoMatch);
                    plan.isTimeTableMatch(data.isTimeTableMatch);
                    plan.isRoutingMatch(data.isRoutingMatch);
                }).fail(function() {

                }).always(function() {

                })
//            }
        });
    }
}

function Plan(dto) {
    var self = this;

    // properties
    self.id = ko.observable(dto.id);
    self.name = ko.observable(dto.serial);
    self.startStn = ko.observable(dto.startSTN);
    self.startTime = ko.observable(dto.startTime);
    self.endStn = ko.observable(dto.endSTN);
    self.endTime = ko.observable(dto.endTime);
    self.sourceType = ko.observable(dto.sourceType);
    self.dailyLineFlag = ko.observable(dto.dailyLineFlag);
    self.dailyLineTime = ko.observable(dto.dailyLineTime);//data-toggle="tooltip" data-placement="top" title="" data-original-title="Tooltip on top"
    self.checkLev1 = ko.observable(dto.checkLev1);
    self.checkLev2 = ko.observable(dto.checkLev2);
    self.highLineFlag = ko.observable(dto.highLineFlag);
    self.dailyLineId = ko.observable(dto.dailyLineId);

    // 校验项 0：未校验，1：匹配，-1：不匹配
    self.isTrainInfoMatch = ko.observable(0);
    self.isTimeTableMatch = ko.observable(0);
    self.isRoutingMatch = ko.observable(0);

    // computed
    self.trainInfo = ko.computed(function() {
        var html = "<button type=\"button\" class=\"btn btn-default\" style=\"padding: 0px 5px 0px 5px\">未知</button>";
        switch(self.isTrainInfoMatch()) {
            case 0:
                html = "<button type=\"button\" class=\"btn btn-warning\" style=\"padding: 0px 5px 0px 5px\">未校验</button>";
                break;
            case 1:
                html = "<button type=\"button\" class=\"btn btn-success\" style=\"padding: 0px 5px 0px 5px\">匹配</button>";
                break;
            case -1:
                html = "<button type=\"button\" class=\"btn btn-danger\" style=\"padding: 0px 5px 0px 5px\">不匹配</button>";
                break;
            default:
        }
        return html;
    });

    self.timeTable = ko.computed(function() {
        var html = "<button type=\"button\" class=\"btn btn-default\" style=\"padding: 0px 5px 0px 5px\">未知</button>";
        switch(self.isTimeTableMatch()) {
            case 0:
                html = "<button type=\"button\" class=\"btn btn-warning\" style=\"padding: 0px 5px 0px 5px\">未校验</button>";
                break;
            case 1:
                html = "<button type=\"button\" class=\"btn btn-success\" style=\"padding: 0px 5px 0px 5px\">匹配</button>";
                break;
            case -1:
                html = "<button type=\"button\" class=\"btn btn-danger\" style=\"padding: 0px 5px 0px 5px\">不匹配</button>";
                break;
            default:
        }
        return html;
    });

    self.routing = ko.computed(function() {
        var html = "<button type=\"button\" class=\"btn btn-default\" style=\"padding: 0px 5px 0px 5px\">未知</button>";
        switch(self.isRoutingMatch()) {
            case 0:
                html = "<button type=\"button\" class=\"btn btn-warning\" style=\"padding: 0px 5px 0px 5px\">未校验</button>";
                break;
            case 1:
                html = "<button type=\"button\" class=\"btn btn-success\" style=\"padding: 0px 5px 0px 5px\">匹配</button>";
                break;
            case -1:
                html = "<button type=\"button\" class=\"btn btn-danger\" style=\"padding: 0px 5px 0px 5px\">不匹配</button>";
                break;
            default:
        }
        return html;
    });
}
