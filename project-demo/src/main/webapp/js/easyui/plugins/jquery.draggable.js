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
	var dragging = false;
	function drag(e) {
		var opts = $.data(e.data.target, "draggable").options;
		var data = e.data;
		var left = data.startLeft + e.pageX - data.startX;
		var top = data.startTop + e.pageY - data.startY;
		if (opts.deltaX != null && opts.deltaX != undefined) {
			left = e.pageX + opts.deltaX;
		}
		if (opts.deltaY != null && opts.deltaY != undefined) {
			top = e.pageY + opts.deltaY;
		}
		if (e.data.parent != document.body) {
			left += $(e.data.parent).scrollLeft();
			top += $(e.data.parent).scrollTop();
		}
		if (opts.axis == "h") {
			data.left = left;
		} else {
			if (opts.axis == "v") {
				data.top = top;
			} else {
				data.left = left;
				data.top = top;
			}
		}
	};
	function applyDrag(e) {
		var opts = $.data(e.data.target, "draggable").options;
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (proxy) {
			proxy.css("cursor", opts.cursor);
		} else {
			proxy = $(e.data.target);
			$.data(e.data.target, "draggable").handle.css("cursor", opts.cursor);
		}
		proxy.css({
			left: e.data.left,
			top: e.data.top
		});
	};
	function doDown(e) {
		dragging = true;
		var opts = $.data(e.data.target, "draggable").options;
		var droppables = $(".droppable").filter(function() {
			return e.data.target != this;
		}).filter(function() {
			var accept = $.data(this, "droppable").options.accept;
			if (accept) {
				return $(accept).filter(function() {
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		$.data(e.data.target, "draggable").droppables = droppables;
		var proxy = $.data(e.data.target, "draggable").proxy;
		if (!proxy) {
			if (opts.proxy) {
				if (opts.proxy == "clone") {
					proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				$.data(e.data.target, "draggable").proxy = proxy;
			} else {
				proxy = $(e.data.target);
			}
		}
		proxy.css("position", "absolute");
		drag(e);
		applyDrag(e);
		opts.onStartDrag.call(e.data.target, e);
		return false;
	};
	function doMove(e) {
		drag(e);
		if ($.data(e.data.target, "draggable").options.onDrag.call(e.data.target, e) != false) {
			applyDrag(e);
		}
		var target = e.data.target;
		$.data(e.data.target, "draggable").droppables.each(function() {
			var $target = $(this);
			var p2 = $target.offset();
			if (e.pageX > p2.left && e.pageX < p2.left + $target.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + $target.outerHeight()) {
				if (!this.entered) {
					$target.trigger("_dragenter", [target]);
					this.entered = true;
				}
				$target.trigger("_dragover", [target]);
			} else {
				if (this.entered) {
					$target.trigger("_dragleave", [target]);
					this.entered = false;
				}
			}
		});
		return false;
	};
	function doUp(e) {
		dragging = false;
		drag(e);
		var proxy = $.data(e.data.target, "draggable").proxy;
		var opts = $.data(e.data.target, "draggable").options;
		if (opts.revert) {
			if (checkDrop() == true) {
				removeProxy();
				$(e.data.target).css({
					position: e.data.startPosition,
					left: e.data.startLeft,
					top: e.data.startTop
				});
			} else {
				if (proxy) {
					proxy.animate({
						left: e.data.startLeft,
						top: e.data.startTop
					},
					function() {
						removeProxy();
					});
				} else {
					$(e.data.target).animate({
						left: e.data.startLeft,
						top: e.data.startTop
					},
					function() {
						$(e.data.target).css("position", e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position: "absolute",
				left: e.data.left,
				top: e.data.top
			});
			removeProxy();
			checkDrop();
		}
		opts.onStopDrag.call(e.data.target, e);
		$(document).unbind(".draggable");
		setTimeout(function() {
			$("body").css("cursor", "auto");
		}, 100);
		function removeProxy() {
			if (proxy) {
				proxy.remove();
			}
			$.data(e.data.target, "draggable").proxy = null;
		};
		function checkDrop() {
			var dropped = false;
			$.data(e.data.target, "draggable").droppables.each(function() {
				var $target = $(this);
				var p2 = $target.offset();
				if (e.pageX > p2.left && e.pageX < p2.left + $target.outerWidth() && e.pageY > p2.top && e.pageY < p2.top + $target.outerHeight()) {
					if (opts.revert) {
						$(e.data.target).css({
							position: e.data.startPosition,
							left: e.data.startLeft,
							top: e.data.startTop
						});
					}
					$target.trigger("_drop", [e.data.target]);
					dropped = true;
					this.entered = false;
				}
			});
			return dropped;
		};
		return false;
	};
	$.fn.draggable = function(options, param) {
		if (typeof options == "string") {
			return $.fn.draggable.methods[options](this, param);
		}
		return this.each(function() {
			var opts;
			var data = $.data(this, "draggable");
			if (data) {
				data.handle.unbind(".draggable");
				opts = $.extend(data.options, options);
			} else {
				opts = $.extend({},
				$.fn.draggable.defaults, options || {});
			}
			if (opts.disabled == true) {
				$(this).css("cursor", "default");
				return;
			}
			var handle = null;
			if (typeof opts.handle == "undefined" || opts.handle == null) {
				handle = $(this);
			} else {
				handle = (typeof opts.handle == "string" ? $(opts.handle, this) : opts.handle);
			}
			$.data(this, "draggable", {
				options: opts,
				handle: handle
			});
			handle.unbind(".draggable").bind("mousemove.draggable", {
				target: this
			},
			function(e) {
				if (dragging) {
					return;
				}
				var opts = $.data(e.data.target, "draggable").options;
				if (isCanDrag(e)) {
					$(this).css("cursor", opts.cursor);
				} else {
					$(this).css("cursor", "");
				}
			}).bind("mouseleave.draggable", {
				target: this
			},
			function(e) {
				$(this).css("cursor", "");
			}).bind("mousedown.draggable", {
				target: this
			},
			function(e) {
				if (isCanDrag(e) == false) {
					return;
				}
				var position = $(e.data.target).position();
				var data = {
					startPosition: $(e.data.target).css("position"),
					startLeft: position.left,
					startTop: position.top,
					left: position.left,
					top: position.top,
					startX: e.pageX,
					startY: e.pageY,
					target: e.data.target,
					parent: $(e.data.target).parent()[0]
				};
				$.extend(e.data, data);
				var opts = $.data(e.data.target, "draggable").options;
				if (opts.onBeforeDrag.call(e.data.target, e) == false) {
					return;
				}
				$(document).bind("mousedown.draggable", e.data, doDown);
				$(document).bind("mousemove.draggable", e.data, doMove);
				$(document).bind("mouseup.draggable", e.data, doUp);
				$("body").css("cursor", opts.cursor);
			});
			function isCanDrag(e) {
				var data = $.data(e.data.target, "draggable");
				var handle = data.handle;
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				return Math.min(t, r, b, l) > data.options.edge;
			};
		});
	};
	$.fn.draggable.methods = {
		options: function(jq) {
			return $.data(jq[0], "draggable").options;
		},
		proxy: function(jq) {
			return $.data(jq[0], "draggable").proxy;
		},
		enable: function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled: false
				});
			});
		},
		disable: function(jq) {
			return jq.each(function() {
				$(this).draggable({
					disabled: true
				});
			});
		}
	};
	$.fn.draggable.defaults = {
		proxy: null,
		revert: false,
		cursor: "move",
		deltaX: null,
		deltaY: null,
		handle: null,
		disabled: false,
		edge: 0,
		axis: null,
		onBeforeDrag: function(e) {},
		onStartDrag: function(e) {},
		onDrag: function(e) {},
		onStopDrag: function(e) {}
	};
})(jQuery);