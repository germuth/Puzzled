jQuery(document).ready(function($) {
	var disable_impressions = ois.disable_impressions_stats;
	var disable_submissions = ois.disable_submissions_stats;
	if (disable_impressions != 'yes' && disable_submissions != 'yes') {
		var impressed = new Array();
		var submitted = new Array();
		if (disable_impressions != 'yes') {
			var ois_interval = setInterval(function() {
				$('.ois_wrapper:onScreen').each(function() {
					if ($.inArray($(this).attr('data'), impressed) > -1) {} else {
						impressed.push($(this).attr('data'));
						var ois_post_id = $('.ois_wrapper').attr('rel');
						var ois_skin_id = $(this).attr('data');
						var ois_service = $(this).attr('service');
						var submission_data = "action=ois_ajax" + "&ois_submission_nonce=" + ois.ois_submission_nonce + "&post_id=" + ois_post_id + "&skin_id=" + ois_skin_id + "&submit=no";
						$.ajax({
							type: "POST",
							url: ois.ajaxurl,
							data: submission_data,
							success: function(msg) {}
						});
					}
				});
			}, 3000);
		}
		if (disable_submissions != 'yes') {
			$('.ois_wrapper form').submit(function(e) {
				var ois_service = $(this).attr('service');
				if (ois_service != 'feedburner') {
					e.preventDefault();
				}
				var selfId = $(this).attr('id');
				var self = this;
				var maybeWrapper = this;
				var unfoundWrapper = true;
				var id = $(this).attr('id');
				while (unfoundWrapper) {
					maybeWrapper = $(maybeWrapper).parent();
					if (maybeWrapper.hasClass('ois_wrapper')) {
						unfoundWrapper = false;
						var id = $(maybeWrapper).attr('data');
					}
				}
				if ($.inArray(id, submitted) == -1) {
					submitted.push(id);
					var ois_post_id = $('.ois_wrapper').attr('rel');
					var ois_skin_id = id;
					var ois_name = $('#' + ois_skin_id + '_name').val();
					var ois_email = $('#' + ois_skin_id + '_email').val();
					var submission_data = "action=ois_ajax" + "&ois_submission_nonce=" + ois.ois_submission_nonce + "&post_id=" + ois_post_id + "&skin_id=" + ois_skin_id + "&submit=yes" + "&name=" + ois_name + "&email=" + ois_email + "&service=" + ois_service;
					
					$.ajax({
						type: "POST",
						url: ois.ajaxurl,
						data: submission_data,
						success: function(data) {
							if (ois_service == 'feedburner') {
								if (data && data != 'no_redirect') {
									window.location.href = data;
								}
							} else {
								disable_submissions = 'yes'; // so no inf. loop
								$(self).unbind('submit');
								$('#' + selfId + ' :input[type="submit"]').click();
							}
						},
					});
				}
			});
		}
	}
});
(function($) {
	$.expr[":"].onScreen = function(elem) {
		var $window = $(window);
		var viewport_top = $window.scrollTop();
		var viewport_height = $window.height();
		var viewport_bottom = viewport_top + viewport_height;
		var $elem = $(elem);
		var top = $elem.offset().top
		var height = $elem.height();
		var bottom = top + height
		return (top >= viewport_top && top < viewport_bottom) || (bottom > viewport_top && bottom <= viewport_bottom) || (height > viewport_height && top <= viewport_top && bottom >= viewport_bottom)
}
})(jQuery);