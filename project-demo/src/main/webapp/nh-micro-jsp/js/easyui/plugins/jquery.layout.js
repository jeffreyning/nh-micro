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
	var resizing = false;
	function setSize(target) {
		var opts = $.data(target, "layout").options;
		var panels = $.data(target, "layout").panels;
		var $target = $(target);
		if (opts.fit == true) {
			var p = $target.parent();
			p.addClass("panel-noscroll");
			$target.width(p.width());
			$target.height(p.height());
		}
		var position = {
			top: 0,
			left: 0,
			width: $target.width(),
			height: $target.height()
		};
		function setNorthSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width: $target.width(),
				height: pp.panel("options").height,
				left: 0,
				top: 0
			});
			position.top += pp.panel("options").height;
			position.height -= pp.panel("options").height;
		};
		if (isVisible(panels.expandNorth)) {
			setNorthSize(panels.expandNorth);
		} else {
			setNorthSize(panels.north);
		}
		function setSouthSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width: $target.width(),
				height: pp.panel("options").height,
				left: 0,
				top: $target.height() - pp.panel("options").height
			});
			position.height -= pp.panel("options").height;
		};
		if (isVisible(panels.expandSouth)) {
			setSouthSize(panels.expandSouth);
		} else {
			setSouthSize(panels.south);
		}
		function setEastSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width: pp.panel("options").width,
				height: position.height,
				left: $target.width() - pp.panel("options").width,
				top: position.top
			});
			position.width -= pp.panel("options").width;
		};
		if (isVisible(panels.expandEast)) {
			setEastSize(panels.expandEast);
		} else {
			setEastSize(panels.east);
		}
		function setWestSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width: pp.panel("options").width,
				height: position.height,
				left: 0,
				top: position.top
			});
			position.left += pp.panel("options").width;
			position.width -= pp.panel("options").width;
		};
		if (isVisible(panels.expandWest)) {
			setWestSize(panels.expandWest);
		} else {
			setWestSize(panels.west);
		}
		panels.center.panel("resize", position);
	};
	function init(target) {
		var $target = $(target);
		if ($target[0].tagName == "BODY") {
			$("html").css({
				height: "100%",
				overflow: "hidden"
			});
			$("body").css({
				height: "100%",
				overflow: "hidden",
				border: "none"
			});
		}
		$target.addClass("layout");
		$target.css({
			margin: 0,
			padding: 0
		});
		$("<div class=\"layout-split-proxy-h\"></div>").appendTo($target);
		$("<div class=\"layout-split-proxy-v\"></div>").appendTo($target);
		$target.children("div[region]").each(function() {
			var region = $(this).attr("region");
			createPanel(target, {
				region: region
			});
		});
		$target.bind("_resize", function(e, param) {
			var opts = $.data(target, "layout").options;
			if (opts.fit == true || param) {
				setSize(target);
			}
			return false;
		});
	};
	function createPanel(target, region) {
		region.region = region.region || "center";
		var panels = $.data(target, "layout").panels;
		var $target = $(target);
		var dir = region.region;
		if (panels[dir].length) {
			return;
		}
		var $panelBody = $target.children("div[region=" + dir + "]");
		if (!$panelBody.length) {
			$panelBody = $("<div></div>").appendTo($target);
		}
		$panelBody.panel($.extend({}, {
			width: ($panelBody.length ? parseInt($panelBody[0].style.width) || $panelBody.outerWidth() : "auto"),
			height: ($panelBody.length ? parseInt($panelBody[0].style.height) || $panelBody.outerHeight() : "auto"),
			split: ($panelBody.attr("split") ? $panelBody.attr("split") == "true": undefined),
			doSize: false,
			cls: ("layout-panel layout-panel-" + dir),
			bodyCls: "layout-body",
			onOpen: function() {
				var constant = {
					north: "up",
					south: "down",
					east: "right",
					west: "left"
				};
				if (!constant[dir]) {
					return;
				}
				var btnClass = "layout-button-" + constant[dir];
				var $panelTool = $(this).panel("header").children("div.panel-tool");
				if (!$panelTool.children("a." + btnClass).length) {
					var t = $("<a href=\"javascript:void(0)\"></a>").addClass(btnClass).appendTo($panelTool);
					t.bind("click", {
						dir: dir
					},
					function(e) {
						collapsePanel(target, e.data.dir);
						return false;
					});
				}
			}
		}, region));
		panels[dir] = $panelBody;
		if ($panelBody.panel("options").split) {
			var $panel = $panelBody.panel("panel");
			$panel.addClass("layout-split-" + dir);
			var handles = "";
			if (dir == "north") {
				handles = "s";
			}
			if (dir == "south") {
				handles = "n";
			}
			if (dir == "east") {
				handles = "w";
			}
			if (dir == "west") {
				handles = "e";
			}
			$panel.resizable({
				handles: handles,
				onStartResize: function(e) {
					resizing = true;
					if (dir == "north" || dir == "south") {
						var proxy = $(">div.layout-split-proxy-v", target);
					} else {
						var proxy = $(">div.layout-split-proxy-h", target);
					}
					var top = 0, left = 0, width = 0, height = 0;
					var pos = {
						display: "block"
					};
					if (dir == "north") {
						pos.top = parseInt($panel.css("top")) + $panel.outerHeight() - proxy.height();
						pos.left = parseInt($panel.css("left"));
						pos.width = $panel.outerWidth();
						pos.height = proxy.height();
					} else {
						if (dir == "south") {
							pos.top = parseInt($panel.css("top"));
							pos.left = parseInt($panel.css("left"));
							pos.width = $panel.outerWidth();
							pos.height = proxy.height();
						} else {
							if (dir == "east") {
								pos.top = parseInt($panel.css("top")) || 0;
								pos.left = parseInt($panel.css("left")) || 0;
								pos.width = proxy.width();
								pos.height = $panel.outerHeight();
							} else {
								if (dir == "west") {
									pos.top = parseInt($panel.css("top")) || 0;
									pos.left = $panel.outerWidth() - proxy.width();
									pos.width = proxy.width();
									pos.height = $panel.outerHeight();
								}
							}
						}
					}
					proxy.css(pos);
					$("<div class=\"layout-mask\"></div>").css({
						left: 0,
						top: 0,
						width: $target.width(),
						height: $target.height()
					}).appendTo($target);
				},
				onResize: function(e) {
					if (dir == "north" || dir == "south") {
						var proxy = $(">div.layout-split-proxy-v", target);
						proxy.css("top", e.pageY - $(target).offset().top - proxy.height() / 2);
					} else {
						var proxy = $(">div.layout-split-proxy-h", target);
						proxy.css("left", e.pageX - $(target).offset().left - proxy.width() / 2);
					}
					return false;
				},
				onStopResize: function() {
					$(">div.layout-split-proxy-v", target).css("display", "none");
					$(">div.layout-split-proxy-h", target).css("display", "none");
					var opts = $panelBody.panel("options");
					opts.width = $panel.outerWidth();
					opts.height = $panel.outerHeight();
					opts.left = $panel.css("left");
					opts.top = $panel.css("top");
					$panelBody.panel("resize");
					setSize(target);
					resizing = false;
					$target.find(">div.layout-mask").remove();
				}
			});
		}
	};
	function removePanel(target, region) {
		var panels = $.data(target, "layout").panels;
		if (panels[region].length) {
			panels[region].panel("destroy");
			panels[region] = $();
			var expandRegion = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
			if (panels[expandRegion]) {
				panels[expandRegion].panel("destroy");
				panels[expandRegion] = undefined;
			}
		}
	};
	function collapsePanel(target, region, animate) {
		if (animate == undefined) {
			animate = "normal";
		}
		var panels = $.data(target, "layout").panels;
		var p = panels[region];
		if (p.panel("options").onBeforeCollapse.call(p) == false) {
			return;
		}
		var expandRegion = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
		if (!panels[expandRegion]) {
			panels[expandRegion] = createExpandPanel(region);
			panels[expandRegion].panel("panel").click(function() {
				var regionHandles = setRegionHandles();
				p.panel("expand", false).panel("open").panel("resize", regionHandles.collapse);
				p.panel("panel").animate(regionHandles.expand);
				return false;
			});
		}
		var regionHandles = setRegionHandles();
		if (!isVisible(panels[expandRegion])) {
			panels.center.panel("resize", regionHandles.resizeC);
		}
		p.panel("panel").animate(regionHandles.collapse, animate,
		function() {
			p.panel("collapse", false).panel("close");
			panels[expandRegion].panel("open").panel("resize", regionHandles.expandP);
		});
		function createExpandPanel(dir) {
			var iconCls;
			if (dir == "east") {
				iconCls = "layout-button-left";
			} else {
				if (dir == "west") {
					iconCls = "layout-button-right";
				} else {
					if (dir == "north") {
						iconCls = "layout-button-down";
					} else {
						if (dir == "south") {
							iconCls = "layout-button-up";
						}
					}
				}
			}
			var p = $("<div></div>").appendTo(target).panel({
				cls: "layout-expand",
				title: "&nbsp;",
				closed: true,
				doSize: false,
				tools: [{
					iconCls: iconCls,
					handler: function() {
						expandPanel(target, region);
						return false;
					}
				}]
			});
			p.panel("panel").hover(function() {
				$(this).addClass("layout-expand-over");
			},
			function() {
				$(this).removeClass("layout-expand-over");
			});
			return p;
		};
		function setRegionHandles() {
			var $target = $(target);
			if (region == "east") {
				return {
					resizeC: {
						width: panels.center.panel("options").width + panels["east"].panel("options").width - 28
					},
					expand: {
						left: $target.width() - panels["east"].panel("options").width
					},
					expandP: {
						top: panels["east"].panel("options").top,
						left: $target.width() - 28,
						width: 28,
						height: panels["center"].panel("options").height
					},
					collapse: {
						left: $target.width()
					}
				};
			} else {
				if (region == "west") {
					return {
						resizeC: {
							width: panels.center.panel("options").width + panels["west"].panel("options").width - 28,
							left: 28
						},
						expand: {
							left: 0
						},
						expandP: {
							left: 0,
							top: panels["west"].panel("options").top,
							width: 28,
							height: panels["center"].panel("options").height
						},
						collapse: {
							left: -panels["west"].panel("options").width
						}
					};
				} else {
					if (region == "north") {
						var hh = $target.height() - 28;
						if (isVisible(panels.expandSouth)) {
							hh -= panels.expandSouth.panel("options").height;
						} else {
							if (isVisible(panels.south)) {
								hh -= panels.south.panel("options").height;
							}
						}
						panels.east.panel("resize", {
							top: 28,
							height: hh
						});
						panels.west.panel("resize", {
							top: 28,
							height: hh
						});
						if (isVisible(panels.expandEast)) {
							panels.expandEast.panel("resize", {
								top: 28,
								height: hh
							});
						}
						if (isVisible(panels.expandWest)) {
							panels.expandWest.panel("resize", {
								top: 28,
								height: hh
							});
						}
						return {
							resizeC: {
								top: 28,
								height: hh
							},
							expand: {
								top: 0
							},
							expandP: {
								top: 0,
								left: 0,
								width: $target.width(),
								height: 28
							},
							collapse: {
								top: -panels["north"].panel("options").height
							}
						};
					} else {
						if (region == "south") {
							var hh = $target.height() - 28;
							if (isVisible(panels.expandNorth)) {
								hh -= panels.expandNorth.panel("options").height;
							} else {
								if (isVisible(panels.north)) {
									hh -= panels.north.panel("options").height;
								}
							}
							panels.east.panel("resize", {
								height: hh
							});
							panels.west.panel("resize", {
								height: hh
							});
							if (isVisible(panels.expandEast)) {
								panels.expandEast.panel("resize", {
									height: hh
								});
							}
							if (isVisible(panels.expandWest)) {
								panels.expandWest.panel("resize", {
									height: hh
								});
							}
							return {
								resizeC: {
									height: hh
								},
								expand: {
									top: $target.height() - panels["south"].panel("options").height
								},
								expandP: {
									top: $target.height() - 28,
									left: 0,
									width: $target.width(),
									height: 28
								},
								collapse: {
									top: $target.height()
								}
							};
						}
					}
				}
			}
		};
	};
	function expandPanel(target, region) {
		var panels = $.data(target, "layout").panels;
		var regionHandles = setRegionHandles();
		var p = panels[region];
		if (p.panel("options").onBeforeExpand.call(p) == false) {
			return;
		}
		var expandRegion = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
		panels[expandRegion].panel("close");
		p.panel("panel").stop(true, true);
		p.panel("expand", false).panel("open").panel("resize", regionHandles.collapse);
		p.panel("panel").animate(regionHandles.expand,
		function() {
			setSize(target);
		});
		function setRegionHandles() {
			var $target = $(target);
			if (region == "east" && panels.expandEast) {
				return {
					collapse: {
						left: $target.width()
					},
					expand: {
						left: $target.width() - panels["east"].panel("options").width
					}
				};
			} else {
				if (region == "west" && panels.expandWest) {
					return {
						collapse: {
							left: -panels["west"].panel("options").width
						},
						expand: {
							left: 0
						}
					};
				} else {
					if (region == "north" && panels.expandNorth) {
						return {
							collapse: {
								top: -panels["north"].panel("options").height
							},
							expand: {
								top: 0
							}
						};
					} else {
						if (region == "south" && panels.expandSouth) {
							return {
								collapse: {
									top: $target.height()
								},
								expand: {
									top: $target.height() - panels["south"].panel("options").height
								}
							};
						}
					}
				}
			}
		};
	};
	function bindEvents(target) {
		var panels = $.data(target, "layout").panels;
		var $target = $(target);
		if (panels.east.length) {
			panels.east.panel("panel").bind("mouseover", "east", autoCollapsePanel);
		}
		if (panels.west.length) {
			panels.west.panel("panel").bind("mouseover", "west", autoCollapsePanel);
		}
		if (panels.north.length) {
			panels.north.panel("panel").bind("mouseover", "north", autoCollapsePanel);
		}
		if (panels.south.length) {
			panels.south.panel("panel").bind("mouseover", "south", autoCollapsePanel);
		}
		panels.center.panel("panel").bind("mouseover", "center", autoCollapsePanel);
		function autoCollapsePanel(e) {
			if (resizing == true) {
				return;
			}
			if (e.data != "east" && isVisible(panels.east) && isVisible(panels.expandEast)) {
				collapsePanel(target, "east");
			}
			if (e.data != "west" && isVisible(panels.west) && isVisible(panels.expandWest)) {
				collapsePanel(target, "west");
			}
			if (e.data != "north" && isVisible(panels.north) && isVisible(panels.expandNorth)) {
				collapsePanel(target, "north");
			}
			if (e.data != "south" && isVisible(panels.south) && isVisible(panels.expandSouth)) {
				collapsePanel(target, "south");
			}
			return false;
		};
	};
	function isVisible(pp) {
		if (!pp) {
			return false;
		}
		if (pp.length) {
			return pp.panel("panel").is(":visible");
		} else {
			return false;
		}
	};
	function collapseAll(target) {
		var panels = $.data(target, "layout").panels;
		if (panels.east.length && panels.east.panel("options").collapsed) {
			collapsePanel(target, "east", 0);
		}
		if (panels.west.length && panels.west.panel("options").collapsed) {
			collapsePanel(target, "west", 0);
		}
		if (panels.north.length && panels.north.panel("options").collapsed) {
			collapsePanel(target, "north", 0);
		}
		if (panels.south.length && panels.south.panel("options").collapsed) {
			collapsePanel(target, "south", 0);
		}
	};
	$.fn.layout = function(method, param) {
		if (typeof method == "string") {
			return $.fn.layout.methods[method](this, param);
		}
		return this.each(function() {
			var state = $.data(this, "layout");
			if (!state) {
				var opts = $.extend({}, {
					fit: $(this).attr("fit") == "true"
				});
				$.data(this, "layout", {
					options: opts,
					panels: {
						center: $(),
						north: $(),
						south: $(),
						east: $(),
						west: $()
					}
				});
				init(this);
				bindEvents(this);
			}
			setSize(this);
			collapseAll(this);
		});
	};
	$.fn.layout.methods = {
		resize: function(jq) {
			return jq.each(function() {
				setSize(this);
			});
		},
		panel: function(jq, param) {
			return $.data(jq[0], "layout").panels[param];
		},
		collapse: function(jq, param) {
			return jq.each(function() {
				collapsePanel(this, param);
			});
		},
		expand: function(jq, region) {
			return jq.each(function() {
				expandPanel(this, region);
			});
		},
		add: function(jq, param) {
			return jq.each(function() {
				createPanel(this, param);
				setSize(this);
				if ($(this).layout("panel", param.region).panel("options").collapsed) {
					collapsePanel(this, param.region, 0);
				}
			});
		},
		remove: function(jq, region) {
			return jq.each(function() {
				removePanel(this, region);
				setSize(this);
			});
		}
	};
})(jQuery);