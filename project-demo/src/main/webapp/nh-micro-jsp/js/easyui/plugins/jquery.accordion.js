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
	function reSize(target) {
		var opts = $.data(target, "accordion").options;
		var panels = $.data(target, "accordion").panels;
		var cc = $(target);
		if (opts.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			opts.width = p.width();
			opts.height = p.height();
		}
		if (opts.width > 0) {
			cc._outerWidth(opts.width);
		}
		var height = "auto";
		if (opts.height > 0) {
			cc._outerHeight(opts.height);
			var outerHeight = panels.length ? panels[0].panel("header").css("height", "").outerHeight() : "auto";
			var height = cc.height() - (panels.length - 1) * outerHeight;
		}
		for (var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			var header = panel.panel("header");
			header._outerHeight(outerHeight);
			panel.panel("resize", {
				width: cc.width(),
				height: height
			});
		}
	};
	function getSelectedPanel(target) {
		var panels = $.data(target, "accordion").panels;
		for (var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").collapsed == false) {
				return panel;
			}
		}
		return null;
	};
	function getPanelByTitle(target, title, isDelete) {
		var panels = $.data(target, "accordion").panels;
		for (var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").title == title) {
				if (isDelete) {
					panels.splice(i, 1);
				}
				return panel;
			}
		}
		return null;
	};
	//渲染折叠面板
	function renderAccordion(target) {
		var cc = $(target);
		cc.addClass("accordion");
		if (cc.attr("border") == "false") {
			cc.addClass("accordion-noborder");
		} else {
			cc.removeClass("accordion-noborder");
		}
		var selectedPanels = cc.children("div[selected]");
		cc.children("div").not(selectedPanels).attr("collapsed", "true");
		if (selectedPanels.length == 0) {
			cc.children("div:first").attr("collapsed", "false");
		}
		var panels = [];
		cc.children("div").each(function() {
			var pp = $(this);
			panels.push(pp);
			createPanel(target, pp, {});
		});
		cc.bind("_resize", function(e, fit) {
			var opts = $.data(target, "accordion").options;
			if (opts.fit == true || fit) {
				reSize(target);
			}
			return false;
		});
		return {
			accordion: cc,
			panels: panels
		};
	};
	function createPanel(target, pp, options) {
		pp.panel($.extend({},
		options, {
			collapsible: false,
			minimizable: false,
			maximizable: false,
			closable: false,
			doSize: false,
			tools: [{
				iconCls: "accordion-collapse",
				handler: function() {
					var animate = $.data(target, "accordion").options.animate;
					if (pp.panel("options").collapsed) {
						stopAnimate(target);
						pp.panel("expand", animate);
					} else {
						stopAnimate(target);
						pp.panel("collapse", animate);
					}
					return false;
				}
			}],
			onBeforeExpand: function() {
				var selectedPanel = getSelectedPanel(target);
				if (selectedPanel) {
					var header = $(selectedPanel).panel("header");
					header.removeClass("accordion-header-selected");
					header.find(".accordion-collapse").triggerHandler("click");
				}
				var header = pp.panel("header");
				header.addClass("accordion-header-selected");
				header.find(".accordion-collapse").removeClass("accordion-expand");
			},
			onExpand: function() {
				var opts = $.data(target, "accordion").options;
				opts.onSelect.call(target, pp.panel("options").title);
			},
			onBeforeCollapse: function() {
				var header = pp.panel("header");
				header.removeClass("accordion-header-selected");
				header.find(".accordion-collapse").addClass("accordion-expand");
			}
		}));
		pp.panel("body").addClass("accordion-body");
		pp.panel("header").addClass("accordion-header").click(function() {
			$(this).find(".accordion-collapse").triggerHandler("click");
			return false;
		});
	};
	function selectByTitle(target, title) {
		var opts = $.data(target, "accordion").options;
		var panels = $.data(target, "accordion").panels;
		var selectedPanel = getSelectedPanel(target);
		if (selectedPanel && selectedPanel.panel("options").title == title) {
			return;
		}
		var panel = getPanelByTitle(target, title);
		if (panel) {
			panel.panel("header").triggerHandler("click");
		} else {
			if (selectedPanel) {
				selectedPanel.panel("header").addClass("accordion-header-selected");
				opts.onSelect.call(target, selectedPanel.panel("options").title);
			}
		}
	};
	function stopAnimate(target) {
		var panels = $.data(target, "accordion").panels;
		for (var i = 0; i < panels.length; i++) {
			panels[i].stop(true, true);
		}
	};
	function add(target, options) {
		var opts = $.data(target, "accordion").options;
		var panels = $.data(target, "accordion").panels;
		stopAnimate(target);
		options.collapsed = options.selected == undefined ? true: options.selected;
		var pp = $("<div></div>").appendTo(target);
		panels.push(pp);
		createPanel(target, pp, options);
		reSize(target);
		opts.onAdd.call(target, options.title);
		selectByTitle(target, options.title);
	};
	function removeByTile(target, title) {
		var opts = $.data(target, "accordion").options;
		var panels = $.data(target, "accordion").panels;
		stopAnimate(target);
		if (opts.onBeforeRemove.call(target, title) == false) {
			return;
		}
		var panel = getPanelByTitle(target, title, true);
		if (panel) {
			panel.panel("destroy");
			if (panels.length) {
				reSize(target);
				var selectedPanel = getSelectedPanel(target);
				if (!selectedPanel) {
					selectByTitle(target, panels[0].panel("options").title);
				}
			}
		}
		opts.onRemove.call(target, title);
	};
	$.fn.accordion = function(options, param) {
		if (typeof options == "string") {
			return $.fn.accordion.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "accordion");
			var opts;
			if (data) {
				opts = $.extend(data.options, options);
				data.opts = opts;
			} else {
				opts = $.extend({},
				$.fn.accordion.defaults, $.fn.accordion.parseOptions(this), options);
				var r = renderAccordion(this);
				$.data(this, "accordion", {
					options: opts,
					accordion: r.accordion,
					panels: r.panels
				});
			}
			reSize(this);
			selectByTitle(this);
		});
	};
	$.fn.accordion.methods = {
		options: function(jq) {
			return $.data(jq[0], "accordion").options;
		},
		panels: function(jq) {
			return $.data(jq[0], "accordion").panels;
		},
		resize: function(jq) {
			return jq.each(function() {
				reSize(this);
			});
		},
		getSelected: function(jq) {
			return getSelectedPanel(jq[0]);
		},
		getPanel: function(jq, title) {
			return getPanelByTitle(jq[0], title);
		},
		select: function(jq, title) {
			return jq.each(function() {
				selectByTitle(this, title);
			});
		},
		add: function(jq, options) {
			return jq.each(function() {
				add(this, options);
			});
		},
		remove: function(jq, title) {
			return jq.each(function() {
				removeByTile(this, title);
			});
		}
	};
	$.fn.accordion.parseOptions = function(target) {
		var t = $(target);
		return {
			width: (parseInt(target.style.width) || undefined),
			height: (parseInt(target.style.height) || undefined),
			fit: (t.attr("fit") ? t.attr("fit") == "true": undefined),
			border: (t.attr("border") ? t.attr("border") == "true": undefined),
			animate: (t.attr("animate") ? t.attr("animate") == "true": undefined)
		};
	};
	$.fn.accordion.defaults = {
		width: "auto",
		height: "auto",
		fit: false,
		border: true,
		animate: true,
		onSelect: function(title) {},
		onAdd: function(title) {},
		onBeforeRemove: function(title) {},
		onRemove: function(title) {}
	};
})(jQuery);