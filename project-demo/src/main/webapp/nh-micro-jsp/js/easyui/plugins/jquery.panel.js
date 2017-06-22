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
	function removeNode(target) {
		target.each(function() {
			$(this).remove();
			if ($.browser.msie) {
				this.outerHTML = "";
			}
		});
	};
	function resizePanel(target, options) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelHeader = panel.children("div.panel-header");
		var panelBody = panel.children("div.panel-body");
		if (options) {
			if (options.width) {
				opts.width = options.width;
			}
			if (options.height) {
				opts.height = options.height;
			}
			if (options.left != null) {
				opts.left = options.left;
			}
			if (options.top != null) {
				opts.top = options.top;
			}
		}
		if (opts.fit == true) {
			var p = panel.parent();
			p.addClass("panel-noscroll");
			opts.width = p.width();
			opts.height = p.height();
		}
		panel.css({
			left: opts.left,
			top: opts.top
		});
		if (!isNaN(opts.width)) {
			panel._outerWidth(opts.width);
		} else {
			panel.width("auto");
		}
		panelHeader.add(panelBody)._outerWidth(panel.width());
		if (!isNaN(opts.height)) {
			panel._outerHeight(opts.height);
			panelBody._outerHeight(panel.height() - panelHeader.outerHeight());
		} else {
			panelBody.height("auto");
		}
		panel.css("height", "");
		opts.onResize.apply(target, [opts.width, opts.height]);
		panel.find(">div.panel-body>div").triggerHandler("_resize");
	};
	function movePanel(target, options) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (options) {
			if (options.left != null) {
				opts.left = options.left;
			}
			if (options.top != null) {
				opts.top = options.top;
			}
		}
		panel.css({
			left: opts.left,
			top: opts.top
		});
		opts.onMove.apply(target, [opts.left, opts.top]);
	};
	function wrapPanel(target) {
		var panel = $(target).addClass("panel-body").wrap("<div class=\"panel\"></div>").parent();
		panel.bind("_resize", function() {
			var _12 = $.data(target, "panel").options;
			if (_12.fit == true) {
				resizePanel(target);
			}
			return false;
		});
		return panel;
	};
	function setHeader(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (opts.tools && typeof opts.tools == "string") {
			panel.find(">div.panel-header>div.panel-tool .panel-tool-a").appendTo(opts.tools);
		}
		removeNode(panel.children("div.panel-header"));
		if (opts.title && !opts.noheader) {
			var header = $("<div class=\"panel-header\"><div class=\"panel-title\">" + opts.title + "</div></div>").prependTo(panel);
			if (opts.iconCls) {
				header.find(".panel-title").addClass("panel-with-icon");
				$("<div class=\"panel-icon\"></div>").addClass(opts.iconCls).appendTo(header);
			}
			var tool = $("<div class=\"panel-tool\"></div>").appendTo(header);
			if (opts.tools) {
				if (typeof opts.tools == "string") {
					$(opts.tools).children().each(function() {
						$(this).addClass($(this).attr("iconCls")).addClass("panel-tool-a").appendTo(tool);
					});
				} else {
					for (var i = 0; i < opts.tools.length; i++) {
						var t = $("<a href=\"javascript:void(0)\"></a>").addClass(opts.tools[i].iconCls).appendTo(tool);
						if (opts.tools[i].handler) {
							t.bind("click", eval(opts.tools[i].handler));
						}
					}
				}
			}
			if (opts.collapsible) {
				$("<a class=\"panel-tool-collapse\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",
				function() {
					if (opts.collapsed == true) {
						expandPanel(target, true);
					} else {
						collapsePanel(target, true);
					}
					return false;
				});
			}
			if (opts.minimizable) {
				$("<a class=\"panel-tool-min\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",
				function() {
					minimizePanel(target);
					return false;
				});
			}
			if (opts.maximizable) {
				$("<a class=\"panel-tool-max\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",
				function() {
					if (opts.maximized == true) {
						restorePanel(target);
					} else {
						maximizePanel(target);
					}
					return false;
				});
			}
			if (opts.closable) {
				$("<a class=\"panel-tool-close\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click",
				function() {
					closePanel(target);
					return false;
				});
			}
			panel.children("div.panel-body").removeClass("panel-body-noheader");
		} else {
			panel.children("div.panel-body").addClass("panel-body-noheader");
		}
	};
	function refreshPanel(target) {
		var panel = $.data(target, "panel");
		if (panel.options.href && (!panel.isLoaded || !panel.options.cache)) {
			panel.isLoaded = false;
			destroyRelative(target);
			var panelBody = panel.panel.find(">div.panel-body");
			if (panel.options.loadingMessage) {
				panelBody.html($("<div class=\"panel-loading\"></div>").html(panel.options.loadingMessage));
			}
			$.ajax({
				url: panel.options.href,
				cache: false,
				success: function(_1f) {
					panelBody.html(panel.options.extractor.call(target, _1f));
					if ($.parser) {
						$.parser.parse(panelBody);
					}
					panel.options.onLoad.apply(target, arguments);
					panel.isLoaded = true;
				}
			});
		}
	};
	function destroyRelative(target) {
		var t = $(target);
		t.find(".combo-f").each(function() {
			$(this).combo("destroy");
		});
		t.find(".m-btn").each(function() {
			$(this).menubutton("destroy");
		});
		t.find(".s-btn").each(function() {
			$(this).splitbutton("destroy");
		});
	};
	function setResizable(target) {
		$(target).find("div.panel:visible,div.accordion:visible,div.tabs-container:visible,div.layout:visible").each(function() {
			$(this).triggerHandler("_resize", [true]);
		});
	};
	function openPanel(target, forceOpen) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceOpen != true) {
			if (opts.onBeforeOpen.call(target) == false) {
				return;
			}
		}
		panel.show();
		opts.closed = false;
		opts.minimized = false;
		opts.onOpen.call(target);
		if (opts.maximized == true) {
			opts.maximized = false;
			maximizePanel(target);
		}
		if (opts.collapsed == true) {
			opts.collapsed = false;
			collapsePanel(target);
		}
		if (!opts.collapsed) {
			refreshPanel(target);
			setResizable(target);
		}
	};
	function closePanel(target, forceClose) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceClose != true) {
			if (opts.onBeforeClose.call(target) == false) {
				return;
			}
		}
		panel.hide();
		opts.closed = true;
		opts.onClose.call(target);
	};
	function destroyPanel(target, forceDestroy) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceDestroy != true) {
			if (opts.onBeforeDestroy.call(target) == false) {
				return;
			}
		}
		destroyRelative(target);
		removeNode(panel);
		opts.onDestroy.call(target);
	};
	function collapsePanel(target, animate) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelBody = panel.children("div.panel-body");
		var panelHeader = panel.children("div.panel-header").find("a.panel-tool-collapse");
		if (opts.collapsed == true) {
			return;
		}
		panelBody.stop(true, true);
		if (opts.onBeforeCollapse.call(target) == false) {
			return;
		}
		panelHeader.addClass("panel-tool-expand");
		if (animate == true) {
			panelBody.slideUp("normal",
			function() {
				opts.collapsed = true;
				opts.onCollapse.call(target);
			});
		} else {
			panelBody.hide();
			opts.collapsed = true;
			opts.onCollapse.call(target);
		}
	};
	function expandPanel(target, animate) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelBody = panel.children("div.panel-body");
		var panelHeader = panel.children("div.panel-header").find("a.panel-tool-collapse");
		if (opts.collapsed == false) {
			return;
		}
		panelBody.stop(true, true);
		if (opts.onBeforeExpand.call(target) == false) {
			return;
		}
		panelHeader.removeClass("panel-tool-expand");
		if (animate == true) {
			panelBody.slideDown("normal",
			function() {
				opts.collapsed = false;
				opts.onExpand.call(target);
				refreshPanel(target);
				setResizable(target);
			});
		} else {
			panelBody.show();
			opts.collapsed = false;
			opts.onExpand.call(target);
			refreshPanel(target);
			setResizable(target);
		}
	};
	function maximizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelHeader = panel.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == true) {
			return;
		}
		panelHeader.addClass("panel-tool-restore");
		if (!$.data(target, "panel").original) {
			$.data(target, "panel").original = {
				width: opts.width,
				height: opts.height,
				left: opts.left,
				top: opts.top,
				fit: opts.fit
			};
		}
		opts.left = 0;
		opts.top = 0;
		opts.fit = true;
		resizePanel(target);
		opts.minimized = false;
		opts.maximized = true;
		opts.onMaximize.call(target);
	};
	function minimizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		panel.hide();
		opts.minimized = true;
		opts.maximized = false;
		opts.onMinimize.call(target);
	};
	function restorePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelHeader = panel.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == false) {
			return;
		}
		panel.show();
		panelHeader.removeClass("panel-tool-restore");
		var original = $.data(target, "panel").original;
		opts.width = original.width;
		opts.height = original.height;
		opts.left = original.left;
		opts.top = original.top;
		opts.fit = original.fit;
		resizePanel(target);
		opts.minimized = false;
		opts.maximized = false;
		$.data(target, "panel").original = null;
		opts.onRestore.call(target);
	};
	function setBorder(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var panelHeader = $(target).panel("header");
		var panelBody = $(target).panel("body");
		panel.css(opts.style);
		panel.addClass(opts.cls);
		if (opts.border) {
			panelHeader.removeClass("panel-header-noborder");
			panelBody.removeClass("panel-body-noborder");
		} else {
			panelHeader.addClass("panel-header-noborder");
			panelBody.addClass("panel-body-noborder");
		}
		panelHeader.addClass(opts.headerCls);
		panelBody.addClass(opts.bodyCls);
		if (opts.id) {
			$(target).attr("id", opts.id);
		} else {
			$(target).removeAttr("id");
		}
	};
	function setTitle(target, title) {
		$.data(target, "panel").options.title = title;
		$(target).panel("header").find("div.panel-title").html(title);
	};
	var TO = false;
	var resized = true;
	$(window).unbind(".panel").bind("resize.panel", function() {
		if (!resized) {
			return;
		}
		if (TO !== false) {
			clearTimeout(TO);
		}
		TO = setTimeout(function() {
			resized = false;
			var layout = $("body.layout");
			if (layout.length) {
				layout.layout("resize");
			} else {
				$("body").children("div.panel,div.accordion,div.tabs-container,div.layout").triggerHandler("_resize");
			}
			resized = true;
			TO = false;
		}, 200);
	});
	$.fn.panel = function(method, options) {
		if (typeof method == "string") {
			return $.fn.panel.methods[method](this, options);
		}
		method = method || {};
		return this.each(function() {
			var panel = $.data(this, "panel");
			var opts;
			if (panel) {
				opts = $.extend(panel.options, method);
			} else {
				opts = $.extend({},
				$.fn.panel.defaults, $.fn.panel.parseOptions(this), method);
				$(this).attr("title", "");
				panel = $.data(this, "panel", {
					options: opts,
					panel: wrapPanel(this),
					isLoaded: false
				});
			}
			if (opts.content) {
				$(this).html(opts.content);
				if ($.parser) {
					$.parser.parse(this);
				}
			}
			setHeader(this);
			setBorder(this);
			if (opts.doSize == true) {
				panel.panel.css("display", "block");
				resizePanel(this);
			}
			if (opts.closed == true || opts.minimized == true) {
				panel.panel.hide();
			} else {
				openPanel(this);
			}
		});
	};
	$.fn._outerWidth = function(width) {
		return this.each(function() {
			if (!$.boxModel && $.browser.msie) {
				$(this).width(width);
			} else {
				$(this).width(width - ($(this).outerWidth() - $(this).width()));
			}
		});
	};
	$.fn._outerHeight = function(height) {
		return this.each(function() {
			if (!$.boxModel && $.browser.msie) {
				$(this).height(height);
			} else {
				$(this).height(height - ($(this).outerHeight() - $(this).height()));
			}
		});
	};
	$.fn.panel.methods = {
		options: function(jq) {
			return $.data(jq[0], "panel").options;
		},
		panel: function(jq) {
			return $.data(jq[0], "panel").panel;
		},
		header: function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-header");
		},
		body: function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-body");
		},
		setTitle: function(jq, title) {
			return jq.each(function() {
				setTitle(this, title);
			});
		},
		open: function(jq, forceOpen) {
			return jq.each(function() {
				openPanel(this, forceOpen);
			});
		},
		close: function(jq, forceClose) {
			return jq.each(function() {
				closePanel(this, forceClose);
			});
		},
		destroy: function(jq, forceDestroy) {
			return jq.each(function() {
				destroyPanel(this, forceDestroy);
			});
		},
		refresh: function(jq, href) {
			return jq.each(function() {
				$.data(this, "panel").isLoaded = false;
				if (href) {
					$.data(this, "panel").options.href = href;
				}
				refreshPanel(this);
			});
		},
		resize: function(jq, options) {
			return jq.each(function() {
				resizePanel(this, options);
			});
		},
		move: function(jq, options) {
			return jq.each(function() {
				movePanel(this, options);
			});
		},
		maximize: function(jq) {
			return jq.each(function() {
				maximizePanel(this);
			});
		},
		minimize: function(jq) {
			return jq.each(function() {
				minimizePanel(this);
			});
		},
		restore: function(jq) {
			return jq.each(function() {
				restorePanel(this);
			});
		},
		collapse: function(jq, animate) {
			return jq.each(function() {
				collapsePanel(this, animate);
			});
		},
		expand: function(jq, animate) {
			return jq.each(function() {
				expandPanel(this, animate);
			});
		}
	};
	$.fn.panel.parseOptions = function(target) {
		var $target = $(target);
		return {
			id: $target.attr("id"),
			width: (parseInt(target.style.width) || undefined),
			height: (parseInt(target.style.height) || undefined),
			left: (parseInt(target.style.left) || undefined),
			top: (parseInt(target.style.top) || undefined),
			title: ($target.attr("title") || undefined),
			iconCls: ($target.attr("iconCls") || $target.attr("icon")),
			cls: $target.attr("cls"),
			headerCls: $target.attr("headerCls"),
			bodyCls: $target.attr("bodyCls"),
			tools: $target.attr("tools"),
			href: $target.attr("href"),
			loadingMessage: ($target.attr("loadingMessage") != undefined ? $target.attr("loadingMessage") : undefined),
			cache: ($target.attr("cache") ? $target.attr("cache") == "true": undefined),
			fit: ($target.attr("fit") ? $target.attr("fit") == "true": undefined),
			border: ($target.attr("border") ? $target.attr("border") == "true": undefined),
			noheader: ($target.attr("noheader") ? $target.attr("noheader") == "true": undefined),
			collapsible: ($target.attr("collapsible") ? $target.attr("collapsible") == "true": undefined),
			minimizable: ($target.attr("minimizable") ? $target.attr("minimizable") == "true": undefined),
			maximizable: ($target.attr("maximizable") ? $target.attr("maximizable") == "true": undefined),
			closable: ($target.attr("closable") ? $target.attr("closable") == "true": undefined),
			collapsed: ($target.attr("collapsed") ? $target.attr("collapsed") == "true": undefined),
			minimized: ($target.attr("minimized") ? $target.attr("minimized") == "true": undefined),
			maximized: ($target.attr("maximized") ? $target.attr("maximized") == "true": undefined),
			closed: ($target.attr("closed") ? $target.attr("closed") == "true": undefined)
		};
	};
	$.fn.panel.defaults = {
		id: null,
		title: null,
		iconCls: null,
		width: "auto",
		height: "auto",
		left: null,
		top: null,
		cls: null,
		headerCls: null,
		bodyCls: null,
		style: {},
		href: null,
		cache: true,
		fit: false,
		border: true,
		doSize: true,
		noheader: false,
		content: null,
		collapsible: false,
		minimizable: false,
		maximizable: false,
		closable: false,
		collapsed: false,
		minimized: false,
		maximized: false,
		closed: false,
		tools: null,
		href: null,
		loadingMessage: "Loading...",
		extractor: function(data) {
			var pattern = /<body[^>]*>((.|[\n\r])*)<\/body>/im;
			var matches = pattern.exec(data);
			if (matches) {
				return matches[1];
			} else {
				return data;
			}
		},
		onLoad: function() {},
		onBeforeOpen: function() {},
		onOpen: function() {},
		onBeforeClose: function() {},
		onClose: function() {},
		onBeforeDestroy: function() {},
		onDestroy: function() {},
		onResize: function(_6c, _6d) {},
		onMove: function(_6e, top) {},
		onMaximize: function() {},
		onRestore: function() {},
		onMinimize: function() {},
		onBeforeCollapse: function() {},
		onBeforeExpand: function() {},
		onCollapse: function() {},
		onExpand: function() {}
	};
})(jQuery);