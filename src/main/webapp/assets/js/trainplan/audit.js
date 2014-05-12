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

    var model = new AuditModel();

    $("#kyjh_time").click(function() {
        if($(this).is(':checked')){
            model.open_kyjh_time("", {title: "客运开行计划时刻表"});
        } else {
            model.close_kyjh_time();
        }
    });
    $("#kyjh_routing").click(function() {
        if($(this).is(':checked')){
            model.open_kyjh_routing("", {title: "客运开行计划经由"});
        } else {
            model.close_kyjh_routing();
        }
    });
    $("#yxx_time").click(function() {
        if($(this).is(':checked')){
            model.open_yxx_time("", {title: "运行线时刻表"})
        } else {
            model.close_yxx_time();
        }
    });
    $("#yxx_routing").click(function() {
        if($(this).is(':checked')){
            model.open_yxx_routing("", {title: "运行线经由"});
        } else {
            model.close_yxx_routing();
        }
    });
    $("#compare").click(function() {
        model.open_compare("", {title: "图形对比"});
    })
});

function AuditModel() {
    var self = this;

    self._default = {
        autoOpen: false,
        height: 400,
        width: 500,
        title: "",
        position: [1100,0],
        maxWidth: 600,
        minHeight: 600
    };

    //客运计划时刻表框
    self.kyjh_time = null;
    //客运计划经由框
    self.kyjh_routing = null;
    //运行线时刻表框
    self.yxx_time = null;
    //运行线经由框
    self.yxx_routing = null;
    //图形对比框
    self.compare = null;

    self._getDialog = function(page, options) {
        self._default = {
            autoOpen: options.autoOpen || self._default.autoOpen,
            height: options.height || self._default.height,
            width: options.width || self._default.width,
            title: options.title || self._default.title,
            position: options.position || self._default.position,
            maxWidth: options.maxWidth || self._default.maxWidth,
            minHeight: options.minHeight || self._default.minHeight,
            close: options.close
        };
        var $dialog = $('<div class="dialog" style="overflow: auto"></div>')
            .html('<iframe src="' + page + '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=no frameborder="0px"></iframe>')
            .dialog(self._default);
        return $dialog;
    };

    //客运计划时刻表
    self.open_kyjh_time = function(params, options) {
        if(self.kyjh_time_opened()) {
            self.update_kyjh_time(params)
        } else {
            options.close = self.close_kyjh_time;
            self.kyjh_time = self._getDialog("timetable.html", options);
            self.kyjh_time.dialog("open");
        }
    }

    self.close_kyjh_time = function() {
        self.kyjh_time.dialog("close");
        self.kyjh_time.remove();
        self.kyjh_time = null;
        $("#kyjh_time").attr('checked', false);
    };

    self.kyjh_time_opened = function() {
        return self.kyjh_time != null;
    }

    self.update_kyjh_time = function(params) {

    }

    //客运计划时经由
    self.open_kyjh_routing = function(params, options) {
        if(self.kyjh_routing_opened()) {
            self.update_kyjh_routing(params);
        } else {
            options.close = self.close_kyjh_routing;
            self.kyjh_routing = self._getDialog("routing.html", options);
            self.kyjh_routing.dialog("open");
        }
    }

    self.close_kyjh_routing = function() {
        self.kyjh_routing.dialog("close");
        self.kyjh_routing = null;
        $("#kyjh_routing").attr('checked', false);
    }

    self.update_kyjh_routing = function(params) {

    }

    self.kyjh_routing_opened = function() {
        return self.kyjh_routing != null;
    }
    //运行线时刻表
    self.open_yxx_time = function(params, options) {
        if(self.yxx_time_opened()) {
            self.update_yxx_time(params);
        } else {
            options.close = self.close_yxx_time;
            self.yxx_time = self._getDialog("timetable.html", options);
            self.yxx_time.dialog("open");
        }

    }

    self.close_yxx_time = function() {
        self.yxx_time.dialog("close");
        self.yxx_time = null;
        $("#yxx_time").attr('checked', false);
    }

    self.update_yxx_time = function(params) {

    }

    self.yxx_time_opened = function() {
        return self.yxx_time != null;
    }
    //运行线经由
    self.open_yxx_routing = function(params, options) {
        if(self.yxx_routing_opened()) {
            self.update_yxx_routing(params);
        } else {
            options.close = self.close_yxx_routing;
            self.yxx_routing = self._getDialog("routing.html", options);
            self.yxx_routing.dialog("open");
        }
    }

    self.close_yxx_routing = function() {
        self.yxx_routing.dialog("close");
        self.yxx_routing = null;
        $("#yxx_routing").attr('checked', false);
    }

    self.update_yxx_routing = function(params) {

    }

    self.yxx_routing_opened = function() {
        return self.yxx_routing != null;
    }
    //图形对比框
    self.open_compare = function(params, options) {
        if(self.compare_opened()) {
            self.update_compare(params);
        } else {
            options.close = self.close_compare;
            self.compare = self._getDialog("mycanvas.html", options);
            self.compare.dialog("open");
        }
    }

    self.close_compare = function() {
        self.compare.dialog("close");
        self.compare = null;
    }

    self.update_compare = function(params) {

    }

    self.compare_opened = function() {
        return self.compare != null;
    }
}