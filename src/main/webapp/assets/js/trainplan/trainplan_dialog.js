/**
 * Created by speeder on 2014/6/20.
 */
_default = {
    autoOpen: false,
    height: $(window).height()/2,
    width: 800,
    title: "",
    position: [(window.screen.availWidth-10-this.width)/2,  (window.screen.availHeight-30-this.height)/2],
    maxWidth: $(window).width(),
    maxHeight: $(window).height(),
    model: true
};

_getDialog = function(page, options) {
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
    var $dialog = $('<div class="dialog" style="overflow: hidden; z-index: 999"></div>')
        .html('<iframe src="' + page + '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder="0px"></iframe>')
        .dialog(_default);
    return $dialog;
};