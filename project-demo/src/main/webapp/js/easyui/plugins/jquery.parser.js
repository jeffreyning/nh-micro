/**
 * jQuery EasyUI 1.2.6
 *
 * Licensed under the GPL terms
 * To use it on other terms please contact us
 *
 * Copyright(c) 2009-2012 stworthy [ stworthy@gmail.com ]
 *
 */
(function($) {
	$.parser = {
		auto: true,
		onComplete: function(context) {},
		plugins: ["linkbutton", "menu", "menubutton", "splitbutton", "progressbar", "tree", "combobox", "combotree", "numberbox", "validatebox", "searchbox", "numberspinner", "timespinner", "calendar", "datebox", "datetimebox", "slider", "layout", "panel", "datagrid", "propertygrid", "treegrid", "tabs", "accordion", "window", "dialog"],
		parse: function(context) {
			var $targets = [];
			for (var i = 0; i < $.parser.plugins.length; i++) {
				var name = $.parser.plugins[i];
				var $target = $(".easyui-" + name, context);
				if ($target.length) {
					if ($target[name]) {
						$target[name]();
					} else {
						$targets.push({
							name: name,
							jq: $target
						});
					}
				}
			}
			if ($targets.length && window.easyloader) {
				var names = [];
				for (var i = 0; i < $targets.length; i++) {
					names.push($targets[i].name);
				}
				easyloader.load(names,
				function() {
					for (var i = 0; i < $targets.length; i++) {
						var name = $targets[i].name;
						var jq = $targets[i].jq;
						jq[name]();
					}
					$.parser.onComplete.call($.parser, context);
				});
			} else {
				$.parser.onComplete.call($.parser, context);
			}
		}
	};
	$(function() {
		if (!window.easyloader && $.parser.auto) {
			$.parser.parse();
		}
	});
})(jQuery);