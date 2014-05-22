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
        $("#checkBtn").prop( "disabled", true )
        ko.utils.arrayForEach(self.planList(), function(plan) {
            if(plan.dailyLineFlag() != "已上图" || !plan.dailyLineId()) {
                plan.isTrainInfoMatch(-1);
                plan.isTimeTableMatch(-1);
                plan.isRoutingMatch(-1);
            } else {
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
            }
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
    self.isTrainInfoMatchClass = ko.computed(function() {
        var className = "btn-default";
        switch(self.isTrainInfoMatch()) {
            case 0:
                className = "btn-warning";
                break;
            case 1:
                className = "btn-success";
                break;
            case -1:
                className = "btn-danger";
                break;
            default:
        }
        return className;
    });

    self.isTrainInfoMatchText = ko.computed(function() {
        var txt = "未知";
        switch(self.isTrainInfoMatch()) {
            case 0:
                txt = "未校验";
                break;
            case 1:
                txt = "匹配";
                break;
            case -1:
                txt = "不匹配";
                break;
            default:
        }
        return txt;
    });

    self.isTimeTableMatchClass = ko.computed(function() {
        var className = "btn-default";
        switch(self.isTimeTableMatch()) {
            case 0:
                className = "btn-warning";
                break;
            case 1:
                className = "btn-success";
                break;
            case -1:
                className = "btn-danger";
                break;
            default:
        }
        return className;
    });

    self.isTimeTableMatchText = ko.computed(function() {
        var txt = "未知";
        switch(self.isTimeTableMatch()) {
            case 0:
                txt = "未校验";
                break;
            case 1:
                txt = "匹配";
                break;
            case -1:
                txt = "不匹配";
                break;
            default:
        }
        return txt;
    });

    self.isRoutingMatchClass = ko.computed(function() {
        var className = "btn-default";
        switch(self.isRoutingMatch()) {
            case 0:
                className = "btn-warning";
                break;
            case 1:
                className = "btn-success";
                break;
            case -1:
                className = "btn-danger";
                break;
            default:
        }
        return className;
    });

    self.isRoutingMatchText = ko.computed(function() {
        var txt = "未知";
        switch(self.isRoutingMatch()) {
            case 0:
                txt = "未校验";
                break;
            case 1:
                txt = "匹配";
                break;
            case -1:
                txt = "不匹配";
                break;
            default:
        }
        return txt;
    });

    self._default = {
        autoOpen: false,
        height: $(window).height()/2,
        width: 800,
        title: "",
        position: [($(window).width() - 800), 0],
        maxWidth: 870,
        maxHeight: $(window).height(),
        model: true
    };

    self._getDialog = function(page, options) {
        var _default = {
            autoOpen: options.autoOpen || self._default.autoOpen,
            height: options.height || self._default.height,
            width: options.width || self._default.width,
            title: options.title || self._default.title,
            position: options.position || self._default.position,
            maxWidth: self._default.maxWidth,
            maxHeight: self._default.maxHeight,
            closeText: "关闭",
            model: true,
            close: options.close || null
        };
        var $dialog = $('<div class="dialog" style="overflow: hidden"></div>')
            .html('<iframe src="' + page + '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder="0px"></iframe>')
            .dialog(_default);
        return $dialog;
    };

    // actions
    self.showInfoComparePanel = function() {
        var url = "audit/compare/traininfo/plan/" + self.id() + "/line/" + self.dailyLineId();
        self._getDialog(url, {title: "客运计划列车信息 vs 日计划列车信息", width: 850}).dialog("open");
    }

    self.showTimeTableComparePanel = function() {
        var url = "audit/compare/timetable/" + $("#bureau option:selected").val() + "/plan/" + self.id() + "/line/" + self.dailyLineId();
        self._getDialog(url, {title: "客运计划列车时刻表 vs 日计划列车时刻表", height: $(window).height(), width: 850}).dialog("open");
    }
}
