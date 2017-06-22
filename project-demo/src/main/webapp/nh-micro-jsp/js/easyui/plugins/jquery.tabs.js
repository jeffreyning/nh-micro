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
	function getMaxScrollWidth(target) {
		var header = $(target).children("div.tabs-header");
		var tabsWidth = 0;
		$("ul.tabs li", header).each(function() {
			tabsWidth += $(this).outerWidth(true);
		});
		var wrapWidth = header.children("div.tabs-wrap").width();
		var padding = parseInt(header.find("ul.tabs").css("padding-left"));
		return tabsWidth - wrapWidth + padding;
	};
	function setScrollers(target) {
		var opts = $.data(target, "tabs").options;
		var header = $(target).children("div.tabs-header");
		var tool = header.children("div.tabs-tool");
		var leftScroller = header.children("div.tabs-scroller-left");
		var rightScroller = header.children("div.tabs-scroller-right");
		var wrap = header.children("div.tabs-wrap");
		tool._outerHeight(header.outerHeight() - (opts.plain ? 2 : 0));
		var fullWidth = 0;
		$("ul.tabs li", header).each(function() {
			fullWidth += $(this).outerWidth(true);
		});
		var realWidth = header.width() - tool.outerWidth();
		if (fullWidth > realWidth) {
			leftScroller.show();
			rightScroller.show();
			tool.css("right", rightScroller.outerWidth());
			wrap.css({
				marginLeft: leftScroller.outerWidth(),
				marginRight: rightScroller.outerWidth() + tool.outerWidth(),
				left: 0,
				width: realWidth - leftScroller.outerWidth() - rightScroller.outerWidth()
			});
		} else {
			leftScroller.hide();
			rightScroller.hide();
			tool.css("right", 0);
			wrap.css({
				marginLeft: 0,
				marginRight: tool.outerWidth(),
				left: 0,
				width: realWidth
			});
			wrap.scrollLeft(0);
		}
	};
	function initHeader(target) {
		var opts = $.data(target, "tabs").options;
		var header = $(target).children("div.tabs-header");
		if (opts.tools) {
			if (typeof opts.tools == "string") {
				$(opts.tools).addClass("tabs-tool").appendTo(header);
				$(opts.tools).show();
			} else {
				header.children("div.tabs-tool").remove();
				var tool = $("<div class=\"tabs-tool\"></div>").appendTo(header);
				for (var i = 0; i < opts.tools.length; i++) {
					var button = $("<a href=\"javascript:void(0);\"></a>").appendTo(tool);
					button[0].onclick = eval(opts.tools[i].handler ||
					function() {});
					button.linkbutton($.extend({},
					opts.tools[i], {
						plain: true
					}));
				}
			}
		} else {
			header.children("div.tabs-tool").remove();
		}
	};
	function setSize(target) {
		var opts = $.data(target, "tabs").options;
		var cc = $(target);
		if (opts.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			opts.width = p.width();
			opts.height = p.height();
		}
		cc.width(opts.width).height(opts.height);
		var header = $(target).children("div.tabs-header");
		header._outerWidth(opts.width);
		setScrollers(target);
		var panels = $(target).children("div.tabs-panels");
		var height = opts.height;
		if (!isNaN(height)) {
			panels._outerHeight(height - header.outerHeight());
		} else {
			panels.height("auto");
		}
		var width = opts.width;
		if (!isNaN(width)) {
			panels._outerWidth(width);
		} else {
			panels.width("auto");
		}
	};
	function setPanelSize(target) {
		var opts = $.data(target, "tabs").options;
		var tab = getSelected(target);
		if (tab) {
			var panels = $(target).children("div.tabs-panels");
			var width = opts.width == "auto" ? "auto": panels.width();
			var height = opts.height == "auto" ? "auto": panels.height();
			tab.panel("resize", {
				width: width,
				height: height
			});
		}
	};
	function wrapTabs(target) {
		var cc = $(target);
		cc.addClass("tabs-container");
		cc.wrapInner("<div class=\"tabs-panels\"/>");
		$("<div class=\"tabs-header\">" + "<div class=\"tabs-scroller-left\"></div>" + "<div class=\"tabs-scroller-right\"></div>" + "<div class=\"tabs-wrap\">" + "<ul class=\"tabs\"></ul>" + "</div>" + "</div>").prependTo(target);
		var tabs = [];
		var tp = cc.children("div.tabs-panels");
		tp.children("div[selected]").attr("toselect", "true");
		tp.children("div").each(function() {
			var pp = $(this);
			tabs.push(pp);
			createTab(target, pp);
		});
		cc.children("div.tabs-header").find(".tabs-scroller-left, .tabs-scroller-right").hover(function() {
			$(this).addClass("tabs-scroller-over");
		},
		function() {
			$(this).removeClass("tabs-scroller-over");
		});
		cc.bind("_resize", function(e, fit) {
			var opts = $.data(target, "tabs").options;
			if (opts.fit == true || fit) {
				setSize(target);
				setPanelSize(target);
			}
			return false;
		});
		return tabs;
	};
	function setProperties(target) {
		var opts = $.data(target, "tabs").options;
		var header = $(target).children("div.tabs-header");
		var panels = $(target).children("div.tabs-panels");
		if (opts.plain == true) {
			header.addClass("tabs-header-plain");
		} else {
			header.removeClass("tabs-header-plain");
		}
		if (opts.border == true) {
			header.removeClass("tabs-header-noborder");
			panels.removeClass("tabs-panels-noborder");
		} else {
			header.addClass("tabs-header-noborder");
			panels.addClass("tabs-panels-noborder");
		}
		$(".tabs-scroller-left", header).unbind(".tabs").bind("click.tabs", function() {
			var wrap = $(".tabs-wrap", header);
			var pos = wrap.scrollLeft() - opts.scrollIncrement;
			wrap.animate({
				scrollLeft: pos
			},
			opts.scrollDuration);
		});
		$(".tabs-scroller-right", header).unbind(".tabs").bind("click.tabs", function() {
			var wrap = $(".tabs-wrap", header);
			var pos = Math.min(wrap.scrollLeft() + opts.scrollIncrement, getMaxScrollWidth(target));
			wrap.animate({
				scrollLeft: pos
			},
			opts.scrollDuration);
		});
		var tabs = $.data(target, "tabs").tabs;
		for (var i = 0, len = tabs.length; i < len; i++) {
			var eahTab = tabs[i];
			var tab = eahTab.panel("options").tab;
			tab.unbind(".tabs").bind("click.tabs", {p: eahTab}, function(e) {
				selectTab(target, getTabIndex(target, e.data.p));
			}).bind("contextmenu.tabs", {p: eahTab}, function(e) {
				opts.onContextMenu.call(target, e, e.data.p.panel("options").title);
			});
			tab.find("a.tabs-close").unbind(".tabs").bind("click.tabs", {p: eahTab}, function(e) {
				closeTab(target, getTabIndex(target, e.data.p));
				return false;
			});
		}
	};
	function createTab(target, pp, options) {
		options = options || {};
		pp.panel($.extend({},
		options, {
			border: false,
			noheader: true,
			closed: true,
			doSize: false,
			iconCls: (options.icon ? options.icon: undefined),
			onLoad: function() {
				if (options.onLoad) {
					options.onLoad.call(this, arguments);
				}
				$.data(target, "tabs").options.onLoad.call(target, pp);
			}
		}));
		var opts = pp.panel("options");
		var header = $(target).children("div.tabs-header");
		var tabs = $("ul.tabs", header);
		var tab = $("<li></li>").appendTo(tabs);
		var tabInner = $("<a href=\"javascript:void(0)\" class=\"tabs-inner\"></a>").appendTo(tab);
		var tabTitle = $("<span class=\"tabs-title\"></span>").html(opts.title).appendTo(tabInner);
		var tabIcon = $("<span class=\"tabs-icon\"></span>").appendTo(tabInner);
		if (opts.closable) {
			tabTitle.addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
		}
		if (opts.iconCls) {
			tabTitle.addClass("tabs-with-icon");
			tabIcon.addClass(opts.iconCls);
		}
		if (opts.tools) {
			var tpTool = $("<span class=\"tabs-p-tool\"></span>").insertAfter(tabInner);
			if (typeof opts.tools == "string") {
				$(opts.tools).children().appendTo(tpTool);
			} else {
				for (var i = 0; i < opts.tools.length; i++) {
					var t = $("<a href=\"javascript:void(0)\"></a>").appendTo(tpTool);
					t.addClass(opts.tools[i].iconCls);
					if (opts.tools[i].handler) {
						t.bind("click", eval(opts.tools[i].handler));
					}
				}
			}
			var pr = tpTool.children().length * 12;
			if (opts.closable) {
				pr += 8;
			} else {
				pr -= 3;
				tpTool.css("right", "5px");
			}
			tabTitle.css("padding-right", pr + "px");
		}
		opts.tab = tab;
	};
	function addTab(target, options) {
		var opts = $.data(target, "tabs").options;
		var tabs = $.data(target, "tabs").tabs;
		if (options.selected == undefined) {
			options.selected = true;
		}
		var pp = $("<div></div>").appendTo($(target).children("div.tabs-panels"));
		tabs.push(pp);
		createTab(target, pp, options);
		opts.onAdd.call(target, options.title);
		setScrollers(target);
		setProperties(target);
		if (options.selected) {
			selectTab(target, tabs.length - 1);
		}
	};
	function update(target, param) {
		var selectHis = $.data(target, "tabs").selectHis;
		var pp = param.tab;
		var title = pp.panel("options").title;
		pp.panel($.extend({},
		param.options, {
			iconCls: (param.options.icon ? param.options.icon: undefined)
		}));
		var opts = pp.panel("options");
		var tab = opts.tab;
		tab.find("span.tabs-icon").attr("class", "tabs-icon");
		tab.find("a.tabs-close").remove();
		tab.find("span.tabs-title").html(opts.title);
		if (opts.closable) {
			tab.find("span.tabs-title").addClass("tabs-closable");
			$("<a href=\"javascript:void(0)\" class=\"tabs-close\"></a>").appendTo(tab);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-closable");
		}
		if (opts.iconCls) {
			tab.find("span.tabs-title").addClass("tabs-with-icon");
			tab.find("span.tabs-icon").addClass(opts.iconCls);
		} else {
			tab.find("span.tabs-title").removeClass("tabs-with-icon");
		}
		if (title != opts.title) {
			for (var i = 0; i < selectHis.length; i++) {
				if (selectHis[i] == title) {
					selectHis[i] = opts.title;
				}
			}
		}
		setProperties(target);
		$.data(target, "tabs").options.onUpdate.call(target, opts.title);
	};
	function closeTab(target, which) {
		var opts = $.data(target, "tabs").options;
		var tabs = $.data(target, "tabs").tabs;
		var param = $.data(target, "tabs").selectHis;
		if (!exists(target, which)) {
			return;
		}
		var tab = getTab(target, which);
		var title = tab.panel("options").title;
		if (opts.onBeforeClose.call(target, title) == false) {
			return;
		}
		var tab = getTab(target, which, true);
		tab.panel("options").tab.remove();
		tab.panel("destroy");
		opts.onClose.call(target, title);
		setScrollers(target);
		for (var i = 0; i < param.length; i++) {
			if (param[i] == title) {
				param.splice(i, 1);
				i--;
			}
		}
		var lastTab = param.pop();
		if (lastTab) {
			selectTab(target, lastTab);
		} else {
			if (tabs.length) {
				selectTab(target, 0);
			}
		}
	};
	function getTab(target, which, isClose) {
		var tabs = $.data(target, "tabs").tabs;
		if (typeof which == "number") {
			if (which < 0 || which >= tabs.length) {
				return null;
			} else {
				var tab = tabs[which];
				if (isClose) {
					tabs.splice(which, 1);
				}
				return tab;
			}
		}
		for (var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").title == which) {
				if (isClose) {
					tabs.splice(i, 1);
				}
				return tab;
			}
		}
		return null;
	};
	function getTabIndex(target, tab) {
		var tabs = $.data(target, "tabs").tabs;
		for (var i = 0; i < tabs.length; i++) {
			if (tabs[i][0] == $(tab)[0]) {
				return i;
			}
		}
		return - 1;
	};
	function getSelected(target) {
		var tabs = $.data(target, "tabs").tabs;
		for (var i = 0; i < tabs.length; i++) {
			var tab = tabs[i];
			if (tab.panel("options").closed == false) {
				return tab;
			}
		}
		return null;
	};
	function initSelectTab(target) {
		var tabs = $.data(target, "tabs").tabs;
		for (var i = 0; i < tabs.length; i++) {
			if (tabs[i].attr("toselect") == "true") {
				selectTab(target, i);
				return;
			}
		}
		if (tabs.length) {
			selectTab(target, 0);
		}
	};
	function selectTab(target, which) {
		var opts = $.data(target, "tabs").options;
		var tabs = $.data(target, "tabs").tabs;
		var selectHis = $.data(target, "tabs").selectHis;
		if (tabs.length == 0) {
			return;
		}
		var selectedTab = getTab(target, which);
		if (!selectedTab) {
			return;
		}
		var oldSelectedTab = getSelected(target);
		if (oldSelectedTab) {
			oldSelectedTab.panel("close");
			oldSelectedTab.panel("options").tab.removeClass("tabs-selected");
		}
		selectedTab.panel("open");
		var title = selectedTab.panel("options").title;
		selectHis.push(title);
		var tab = selectedTab.panel("options").tab;
		tab.addClass("tabs-selected");
		var wrap = $(target).find(">div.tabs-header div.tabs-wrap");
		var leftPos = tab.position().left + wrap.scrollLeft();
		var left = leftPos - wrap.scrollLeft();
		var width = left + tab.outerWidth();
		if (left < 0 || width > wrap.innerWidth()) {
			var pos = Math.min(leftPos - (wrap.width() - tab.width()) / 2, getMaxScrollWidth(target));
			wrap.animate({
				scrollLeft: pos
			},
			opts.scrollDuration);
		} else {
			var pos = Math.min(wrap.scrollLeft(), getMaxScrollWidth(target));
			wrap.animate({
				scrollLeft: pos
			},
			opts.scrollDuration);
		}
		setPanelSize(target);
		opts.onSelect.call(target, title);
	};
	function exists(target, which) {
		return getTab(target, which) != null;
	};
	$.fn.tabs = function(options, param) {
		if (typeof options == "string") {
			return $.fn.tabs.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var tabs = $.data(this, "tabs");
			var opts;
			if (tabs) {
				opts = $.extend(tabs.options, options);
				tabs.options = opts;
			} else {
				$.data(this, "tabs", {
					options: $.extend({},
					$.fn.tabs.defaults, $.fn.tabs.parseOptions(this), options),
					tabs: wrapTabs(this),
					selectHis: []
				});
			}
			initHeader(this);
			setProperties(this);
			setSize(this);
			initSelectTab(this);
		});
	};
	$.fn.tabs.methods = {
		options: function(jq) {
			return $.data(jq[0], "tabs").options;
		},
		tabs: function(jq) {
			return $.data(jq[0], "tabs").tabs;
		},
		resize: function(jq) {
			return jq.each(function() {
				setSize(this);
				setPanelSize(this);
			});
		},
		add: function(jq, options) {
			return jq.each(function() {
				addTab(this, options);
			});
		},
		close: function(jq, title) {
			return jq.each(function() {
				closeTab(this, title);
			});
		},
		getTab: function(jq, which) {
			return getTab(jq[0], which);
		},
		getTabIndex: function(jq, tab) {
			return getTabIndex(jq[0], tab);
		},
		getSelected: function(jq) {
			return getSelected(jq[0]);
		},
		select: function(jq, which) {
			return jq.each(function() {
				selectTab(this, which);
			});
		},
		exists: function(jq, which) {
			return exists(jq[0], which);
		},
		update: function(jq, param) {
			return jq.each(function() {
				update(this, param);
			});
		}
	};
	$.fn.tabs.parseOptions = function(target) {
		var t = $(target);
		return {
			width: (parseInt(target.style.width) || undefined),
			height: (parseInt(target.style.height) || undefined),
			fit: (t.attr("fit") ? t.attr("fit") == "true": undefined),
			border: (t.attr("border") ? t.attr("border") == "true": undefined),
			plain: (t.attr("plain") ? t.attr("plain") == "true": undefined),
			tools: t.attr("tools")
		};
	};
	$.fn.tabs.defaults = {
		width: "auto",
		height: "auto",
		plain: false,
		fit: false,
		border: true,
		tools: null,
		scrollIncrement: 100,
		scrollDuration: 400,
		onLoad: function(panel) {},
		onSelect: function(title) {},
		onBeforeClose: function(title) {},
		onClose: function(title) {},
		onAdd: function(title) {},
		onUpdate: function(title) {},
		onContextMenu: function(e, title) {}
	};
})(jQuery);