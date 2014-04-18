;(function($) {
    console.log("including migrate? "+(typeof $.browser == "undefined")+" jQuery version - " +$().jquery + " (" + parseFloat($().jquery) + ")");
    
    if (typeof $.browser == "undefined"){ 
        //if this site is using jQuery 1.9 or above we need to use the migrate plugin to keep functionality...
        /*! jQuery Migrate v1.2.1 | (c) 2005, 2013 jQuery Foundation, Inc. and other contributors | jquery.org/license */
        jQuery.migrateMute===void 0&&(jQuery.migrateMute=!0),function(e,t,n){function r(n){var r=t.console;i[n]||(i[n]=!0,e.migrateWarnings.push(n),r&&r.warn&&!e.migrateMute&&(r.warn("JQMIGRATE: "+n),e.migrateTrace&&r.trace&&r.trace()))}function a(t,a,i,o){if(Object.defineProperty)try{return Object.defineProperty(t,a,{configurable:!0,enumerable:!0,get:function(){return r(o),i},set:function(e){r(o),i=e}}),n}catch(s){}e._definePropertyBroken=!0,t[a]=i}var i={};e.migrateWarnings=[],!e.migrateMute&&t.console&&t.console.log&&t.console.log("JQMIGRATE: Logging is active"),e.migrateTrace===n&&(e.migrateTrace=!0),e.migrateReset=function(){i={},e.migrateWarnings.length=0},"BackCompat"===document.compatMode&&r("jQuery is not compatible with Quirks Mode");var o=e("<input/>",{size:1}).attr("size")&&e.attrFn,s=e.attr,u=e.attrHooks.value&&e.attrHooks.value.get||function(){return null},c=e.attrHooks.value&&e.attrHooks.value.set||function(){return n},l=/^(?:input|button)$/i,d=/^[238]$/,p=/^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,f=/^(?:checked|selected)$/i;a(e,"attrFn",o||{},"jQuery.attrFn is deprecated"),e.attr=function(t,a,i,u){var c=a.toLowerCase(),g=t&&t.nodeType;return u&&(4>s.length&&r("jQuery.fn.attr( props, pass ) is deprecated"),t&&!d.test(g)&&(o?a in o:e.isFunction(e.fn[a])))?e(t)[a](i):("type"===a&&i!==n&&l.test(t.nodeName)&&t.parentNode&&r("Can't change the 'type' of an input or button in IE 6/7/8"),!e.attrHooks[c]&&p.test(c)&&(e.attrHooks[c]={get:function(t,r){var a,i=e.prop(t,r);return i===!0||"boolean"!=typeof i&&(a=t.getAttributeNode(r))&&a.nodeValue!==!1?r.toLowerCase():n},set:function(t,n,r){var a;return n===!1?e.removeAttr(t,r):(a=e.propFix[r]||r,a in t&&(t[a]=!0),t.setAttribute(r,r.toLowerCase())),r}},f.test(c)&&r("jQuery.fn.attr('"+c+"') may use property instead of attribute")),s.call(e,t,a,i))},e.attrHooks.value={get:function(e,t){var n=(e.nodeName||"").toLowerCase();return"button"===n?u.apply(this,arguments):("input"!==n&&"option"!==n&&r("jQuery.fn.attr('value') no longer gets properties"),t in e?e.value:null)},set:function(e,t){var a=(e.nodeName||"").toLowerCase();return"button"===a?c.apply(this,arguments):("input"!==a&&"option"!==a&&r("jQuery.fn.attr('value', val) no longer sets properties"),e.value=t,n)}};var g,h,v=e.fn.init,m=e.parseJSON,y=/^([^<]*)(<[\w\W]+>)([^>]*)$/;e.fn.init=function(t,n,a){var i;return t&&"string"==typeof t&&!e.isPlainObject(n)&&(i=y.exec(e.trim(t)))&&i[0]&&("<"!==t.charAt(0)&&r("$(html) HTML strings must start with '<' character"),i[3]&&r("$(html) HTML text after last tag is ignored"),"#"===i[0].charAt(0)&&(r("HTML string cannot start with a '#' character"),e.error("JQMIGRATE: Invalid selector string (XSS)")),n&&n.context&&(n=n.context),e.parseHTML)?v.call(this,e.parseHTML(i[2],n,!0),n,a):v.apply(this,arguments)},e.fn.init.prototype=e.fn,e.parseJSON=function(e){return e||null===e?m.apply(this,arguments):(r("jQuery.parseJSON requires a valid JSON string"),null)},e.uaMatch=function(e){e=e.toLowerCase();var t=/(chrome)[ \/]([\w.]+)/.exec(e)||/(webkit)[ \/]([\w.]+)/.exec(e)||/(opera)(?:.*version|)[ \/]([\w.]+)/.exec(e)||/(msie) ([\w.]+)/.exec(e)||0>e.indexOf("compatible")&&/(mozilla)(?:.*? rv:([\w.]+)|)/.exec(e)||[];return{browser:t[1]||"",version:t[2]||"0"}},e.browser||(g=e.uaMatch(navigator.userAgent),h={},g.browser&&(h[g.browser]=!0,h.version=g.version),h.chrome?h.webkit=!0:h.webkit&&(h.safari=!0),e.browser=h),a(e,"browser",e.browser,"jQuery.browser is deprecated"),e.sub=function(){function t(e,n){return new t.fn.init(e,n)}e.extend(!0,t,this),t.superclass=this,t.fn=t.prototype=this(),t.fn.constructor=t,t.sub=this.sub,t.fn.init=function(r,a){return a&&a instanceof e&&!(a instanceof t)&&(a=t(a)),e.fn.init.call(this,r,a,n)},t.fn.init.prototype=t.fn;var n=t(document);return r("jQuery.sub() is deprecated"),t},e.ajaxSetup({converters:{"text json":e.parseJSON}});var b=e.fn.data;e.fn.data=function(t){var a,i,o=this[0];return!o||"events"!==t||1!==arguments.length||(a=e.data(o,t),i=e._data(o,t),a!==n&&a!==i||i===n)?b.apply(this,arguments):(r("Use of jQuery.fn.data('events') is deprecated"),i)};var j=/\/(java|ecma)script/i,w=e.fn.andSelf||e.fn.addBack;e.fn.andSelf=function(){return r("jQuery.fn.andSelf() replaced by jQuery.fn.addBack()"),w.apply(this,arguments)},e.clean||(e.clean=function(t,a,i,o){a=a||document,a=!a.nodeType&&a[0]||a,a=a.ownerDocument||a,r("jQuery.clean() is deprecated");var s,u,c,l,d=[];if(e.merge(d,e.buildFragment(t,a).childNodes),i)for(c=function(e){return!e.type||j.test(e.type)?o?o.push(e.parentNode?e.parentNode.removeChild(e):e):i.appendChild(e):n},s=0;null!=(u=d[s]);s++)e.nodeName(u,"script")&&c(u)||(i.appendChild(u),u.getElementsByTagName!==n&&(l=e.grep(e.merge([],u.getElementsByTagName("script")),c),d.splice.apply(d,[s+1,0].concat(l)),s+=l.length));return d});var Q=e.event.add,x=e.event.remove,k=e.event.trigger,N=e.fn.toggle,T=e.fn.live,M=e.fn.die,S="ajaxStart|ajaxStop|ajaxSend|ajaxComplete|ajaxError|ajaxSuccess",C=RegExp("\\b(?:"+S+")\\b"),H=/(?:^|\s)hover(\.\S+|)\b/,A=function(t){return"string"!=typeof t||e.event.special.hover?t:(H.test(t)&&r("'hover' pseudo-event is deprecated, use 'mouseenter mouseleave'"),t&&t.replace(H,"mouseenter$1 mouseleave$1"))};e.event.props&&"attrChange"!==e.event.props[0]&&e.event.props.unshift("attrChange","attrName","relatedNode","srcElement"),e.event.dispatch&&a(e.event,"handle",e.event.dispatch,"jQuery.event.handle is undocumented and deprecated"),e.event.add=function(e,t,n,a,i){e!==document&&C.test(t)&&r("AJAX events should be attached to document: "+t),Q.call(this,e,A(t||""),n,a,i)},e.event.remove=function(e,t,n,r,a){x.call(this,e,A(t)||"",n,r,a)},e.fn.error=function(){var e=Array.prototype.slice.call(arguments,0);return r("jQuery.fn.error() is deprecated"),e.splice(0,0,"error"),arguments.length?this.bind.apply(this,e):(this.triggerHandler.apply(this,e),this)},e.fn.toggle=function(t,n){if(!e.isFunction(t)||!e.isFunction(n))return N.apply(this,arguments);r("jQuery.fn.toggle(handler, handler...) is deprecated");var a=arguments,i=t.guid||e.guid++,o=0,s=function(n){var r=(e._data(this,"lastToggle"+t.guid)||0)%o;return e._data(this,"lastToggle"+t.guid,r+1),n.preventDefault(),a[r].apply(this,arguments)||!1};for(s.guid=i;a.length>o;)a[o++].guid=i;return this.click(s)},e.fn.live=function(t,n,a){return r("jQuery.fn.live() is deprecated"),T?T.apply(this,arguments):(e(this.context).on(t,this.selector,n,a),this)},e.fn.die=function(t,n){return r("jQuery.fn.die() is deprecated"),M?M.apply(this,arguments):(e(this.context).off(t,this.selector||"**",n),this)},e.event.trigger=function(e,t,n,a){return n||C.test(e)||r("Global events are undocumented and deprecated"),k.call(this,e,t,n||document,a)},e.each(S.split("|"),function(t,n){e.event.special[n]={setup:function(){var t=this;return t!==document&&(e.event.add(document,n+"."+e.guid,function(){e.event.trigger(n,null,t,!0)}),e._data(this,n,e.guid++)),!1},teardown:function(){return this!==document&&e.event.remove(document,n+"."+e._data(this,n)),!1}}})}(jQuery,window);
    }

    /* Stop execution if cookies are not enabled */
    var cookieEnabled=(navigator.cookieEnabled)? true : false;
    //if not IE4+ nor NS6+
    if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled) {
        document.cookie="testcookie";
        cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false;
    }
    if (!cookieEnabled) {
        console.log('Cookies are disabled. Exiting operation.');
        return false;
    }
    enable_link_select('popup-domination-link');
    
    
    // the following variables detect whether or not to show the popup //
    //
    //  isMobile    => true when the useragent is in the mobiles list
    //  isHidden    => true when the popup is part of an A/B campaign that's been seen already
    //  isRefBlock  => true when the URL of the page has teh query string pdref=1 in it.

    var isMobile = false;
    // Smartphones
    if (window.screen.availWidth <=767) {
        isMobile = true;
    }
    // Tablets
    if (window.screen.availWidth > 767 && window.screen.availWidth <=1024) {
        isMobile = true;
    }
    
    if (typeof popup_domination.show_mobiles != 'undefined' && popup_domination.show_mobiles == "true") { isMobile = false; }
    if (typeof popup_domination_new_window == 'undefined') { popup_domination_new_window = 'no'; }
    
    var isHidden = (get_cookie('popup_domination_hide_ab'+popup_domination.campaign) == 'Y');
    var isRefBlock = (location.search.indexOf('pdref=1') > -1);

    if (typeof popup_domination == 'undefined') {
        popup_domination = '';
        return false;
    }
    var timer, exit_shown = false;
    
    $(document).ready(function() {
        vidcheck = setTimeout(function() {check_cookie(popup_domination.popupid,popup_domination.mailingid)},1);
        var cururl = window.location;
        if (decodeURIComponent(popup_domination.conversionpage) == cururl) {
            var abcookie = get_cookie("popup_dom_split_show");
            var camp = popup_domination.campaign;
            if (abcookie == 'Y') {
                var popupid = get_cookie("popup_domination_lightbox");
                var data = {
                    action: 'popup_domination_ab_split',
                    stage: 'opt-in',
                    camp : camp,
                    popupid : popupid,
                    optin: '1'
                };
                jQuery.post(popup_domination_admin_ajax, data, function(response) {
                    document.cookie = 'popup_dom_split_show' + '=; expires=Thu, 01-Jan-70 00:00:01 GMT;';
                });
            }
        }
        
        if (popup_domination.show_anim != "inpost") { $(document).find('body').prepend(popup_domination.output); }
        if (popup_domination.impression_count > 1) {
            if (check_impressions()) {
                return false;
            }
        }
        max_zindex();
        if (popup_domination.center && popup_domination.center == 'Y') { 
            init_center();
        }

        if (!isHidden && !isRefBlock) {
            switch(popup_domination.show_opt) {
                case 'mouseleave':
                    $('html,body').mouseout(window_mouseout);
                    break;
                case 'unload':
                    enable_unload();
                    break;
                case 'linkclick':
                    if (!popup_domination.linkclick) {
                        popup_domination.linkclick = 'popup-domination-link';
                    }
                    enable_link_select(popup_domination.linkclick);
                    break;
                case 'tab':
                    if (!check_cookie('none',popup_domination.mailingid) && !($.browser.msie && $.browser.version < 10))
                    {    
                        create_tab();
                        break;
                    }
                default:
                    if (popup_domination.delay && popup_domination.delay > 0) {
                        timer = setTimeout(show_lightbox,(popup_domination.delay*1000));
                    } else {
                        show_lightbox();
                    }
                break;
            }
        }
        
        
        $('#popup_domination_lightbox_wrapper #close-button').css('cursor','pointer').click(function() {
            if (popup_domination.close_option != 'false') {
                $('#popup_domination_lightbox_wrapper').hide(); //possible IE fix?
                close_box(popup_domination.popupid, false);
                return false;
            }
        });
        $(document).keydown(function(e) {
            if (e.which == 27 && popup_domination.close_option != 'false') {
                close_box(popup_domination.popupid, false);
                return false;
            }
        });        
        
        if (popup_domination.close_option == 'false') {
            $('#popup_domination_lightbox_close').remove();
        } else {
            $('#popup_domination_lightbox_wrapper #popup_domination_lightbox_close').click(function() {
                close_box(popup_domination.popupid, false);
                return false;
            });
        }
        
        var provider = $('#popup_domination_lightbox_wrapper .provider').val();
        
        
        // TODO REVIEW
        if (provider == 'aw') {
            $('#popup_domination_lightbox_wrapper .form div').append('</form>');
        };
        
        //code to allow 'open in new window' functionality - prevents browsers blocking it.
        if ($('#popup_domination_lightbox_wrapper form').attr("target") == 'popdom' || popup_domination_new_window == "yes")
        {
            $('#popup_domination_lightbox_wrapper input[type="submit"],.popdom_form input[type="submit"]').attr("onclick","window.open('about:blank','popdom')");
        }
        
        if ( $.isFunction($.fn.on) ) {
            $('#popup_domination_lightbox_wrapper , #popdom-inline-container , .popdom_form ').on('click', 'input[type="submit"]', function(e) {submit_the_thing(this,e)});
        } else {
            $('#popup_domination_lightbox_wrapper input[type="submit"], #popdom-inline-container input[type="submit"], .popdom_form input[type="submit"]').live('click', function(e) {submit_the_thing(this,e)});
        }
        
        function submit_the_thing(target,e) {
            e.preventDefault();
            var checked = false;
            var checkArray = new Array();
            var thisform = '';//$(this).parents('form');
            if (thisform.length == 0)
            {
                thisform = $(target).parents('#popup_domination_lightbox_wrapper');
            }
            console.log(thisform);
            thisform.find(':text').each(function() {
                var $this = $(this), val = $this.val();
                if ($this.data('default_value') && val == $this.data('default_value')) {
                    checkArray.push(false);
                }
                if (val == '' && typeof $this.attr('required') != 'undefined' ) {
                    checkArray.push(false);
                } else {
                    if (val == $this.data('default_value')) {
                        checkArray.push(false);
                    } else {
                        checkArray.push(true);
                    }
                }
            });
            if (typeof thisform.find('.email').val() != 'undefined'){
                if (thisform.find('.email').val().indexOf('@') < 1) { checkArray.push(false); }
            }
            else {
                //this is a redirect theme (well, there's no email field)
                thisform.find('form').get(0).setAttribute('method','get'); //oh yes, another little jQuery "quirk" anyway, make it a get not a post
                //when we submit a form via a GET we need to make all URL query strings part of the form as any querystrings in the action URL
                //will be lost by the browser (html5 spec) and replaced by the contents of the form.
                var keys = [], vals = [], vars=[], hash;
                var q = thisform.find('form').get(0).getAttribute('action').split('?')[1];
                if(q != undefined){
                    q = q.split('&');
                    for(var i = 0; i < q.length; i++){
                        hash = q[i].split('=');
                        keys.push(hash[0]);
                        vals.push(hash[1]);
                    }
                    $.each(vals, function( index ) {
                        thisform.find('form').prepend('<input type="hidden" name="'+keys[index]+'" value="'+vals[index]+'">');
                    });
                }
            }
            //console.log(checkArray);
            checked = !($.inArray(false,checkArray) > -1);
            //console.log(checked);
            
            var email = thisform.find('.email').val();
            var name = thisform.find('.name').val();
            var custom1 = thisform.find('.custom1_input').val();
            var custom2 = thisform.find('.custom2_input').val();
            var customf2 = thisform.find('.custom_id2').val();
            var customf1 = thisform.find('.custom_id1').val();
            var listid = thisform.find('.listid').val();
            var mailingid = thisform.find('.mailingid').val();
            var mailnotify = thisform.find('.mailnotify').val();
            var master = thisform.find('.master').val();
            var campaignid = thisform.find('.campaignid').val();
            var campname = thisform.find('.campname').val();
            var provider = thisform.find('.provider').val();
            
            if (typeof email=="undefined") {
                campaignid = popup_domination.popupid;
                register_optin(provider,false,campaignid);
            } else if (checked) {
                thisform.find('input[type="submit"]').attr('disabled', 'disabled');
                thisform.find('.form input').hide();
                thisform.find('.wait').show();
                thisform.find('.error').hide();
                var data = '';
                
                //	if (provider != 'form' && provider != 'aw' && provider != 'nm') {
                
                data = {
                    action: 'popup_domination_lightbox_submit',
                    provider: provider,
                    listid: listid,
                    redirect: popup_domination.redirect,
                    mailingid: mailingid,
                    mailnotify: mailnotify,
                    master: master,
                    campaignid: campaignid,
                    campname: campname,
                    name: name,
                    email: email,
                    custom1: custom1,
                    custom2: custom2,
                    customf1: customf1,
                    customf2: customf2
                };
                jQuery.post(popup_domination_admin_ajax, data, function(response) {
                    if (response.length > 4 && response.indexOf("formcode") == -1) {
                        thisform.find('input[type="submit"]').removeAttr('disabled', 'disabled');
                        thisform.find('.form input').show();
                        thisform.find('.wait').hide();
                        if (provider == 'mc' || provider == "cc") thisform.find('.error').text(response).show();
                        else thisform.find('.error').text("There was an error, please check your fields.").show();

                    } else {
                        register_optin(provider,mailingid,campaignid);
                    }
                }).error(function() { // error submitting to email provider
                    register_optin(provider,false,campaignid);
                });

            } else{
                thisform.submit(function(e) {
                    e.preventDefault();
                });
                return false;
            }
            return false;
        }
        
        
        
        $('#popup_domination_lightbox_wrapper .sb_facebook').click(function() {
            if ($(this).hasClass('got_user') == true) {
                var email = $('#popup_domination_lightbox_wrapper .fbemail').val();
                var name = $('#popup_domination_lightbox_wrapper .fbname').val();
                var custom1 = $('#popup_domination_lightbox_wrapper .custom1_input').val();
                var custom2 = $('#popup_domination_lightbox_wrapper .custom2_input').val();
                var customf2 = $('#popup_domination_lightbox_wrapper .custom_id2').val();
                var customf1 = $('#popup_domination_lightbox_wrapper .custom_id1').val();
                var listid = $('#popup_domination_lightbox_wrapper .listid').val();
                var mailingid = $('#popup_domination_lightbox_wrapper .mailingid').val();
                var mailnotify = $('#popup_domination_lightbox_wrapper .mailnotify').val();
                var campaignid = $('#popup_domination_lightbox_wrapper .campaignid').val();
                var campname = $('#popup_domination_lightbox_wrapper .campname').val();
                var master = $('#popup_domination_lightbox_wrapper .master').val();
                $('#popup_domination_lightbox_wrapper input[type="submit"]').attr('disabled', 'disabled');
                $('#popup_domination_lightbox_wrapper .form input').hide();
                $('#popup_domination_lightbox_wrapper .wait').show();
                $('#popup_domination_lightbox_wrapper .error').hide();
                if (provider != 'form' && provider != 'aw' && provider != 'nm') {
                    var data = {
                        action: 'popup_domination_lightbox_submit',
                        name: name,
                        email: email,
                        custom1: custom1,
                        custom2: custom2,
                        customf1: customf1,
                        customf2: customf2,
                        provider: provider,
                        listid: listid,
                        mailingid: mailingid,
                        mailnotify: mailnotify,
                        master: master,
                        campaignid: campaignid,
                        campname: campname
                    };
                    
                    jQuery.post(popup_domination_admin_ajax, data, function(response) {
                        if (response.length > 4) {
                            $('#popup_domination_lightbox_wrapper input[type="submit"]').removeAttr('disabled', 'disabled');
                            $('#popup_domination_lightbox_wrapper .form input').show();
                            $('#popup_domination_lightbox_wrapper .wait').hide();
                        } else {
                            close_box(popup_domination.popupid,mailingid);
                            if (check_split_cookie() != true) {
                            var popupid = popup_domination.popupid;
                            var data = {
                                action: 'popup_domination_analytics_add',
                                stage: 'opt-in',
                                popupid: popup_domination.popupid
                            };
                            jQuery.post(popup_domination_admin_ajax, data, function() {
                                redirect(popup_domination.redirect, provider);
                            });
                            } else {
                                redirect(popup_domination.redirect, provider);
                            }
                        }
                    });
                    } else {
                        $('#popup_domination_lightbox_wrapper .email').val(email);
                        $('#popup_domination_lightbox_wrapper .name').val(name);
                        if (check_split_cookie() != true) {
                            var popupid = popup_domination.popupid;
                            var data = {
                                action: 'popup_domination_analytics_add',
                                stage: 'opt-in',
                                popupid: popup_domination.popupid
                            };
                            jQuery.post(popup_domination_admin_ajax, data, function() {
                                $('#popup_domination_lightbox_wrapper form').submit();
                                close_box(popup_domination.popupid,mailingid);
                            });
                            return false;
                        } else {
                            $('#popup_domination_lightbox_wrapper form').submit();
                            close_box(popup_domination.popupid,mailingid);
                        }
                    return false;
                }
                return false;
            }
        });
        
        
        $(function () {
            var ele = $(".lightbox-download-nums");
            var clr = null;
            var number = $(".lightbox-download-nums").text();
            number = parseInt(number);
            var rand = number;
            loop();
            function loop() {
                clearInterval(clr);
                inloop();
                setTimeout(loop, 1000);
            }
            function inloop() {
                ele.html(rand += 1);
                if (!(rand % 50)) {
                    return;
                }
                clr = setTimeout(inloop, 3000);
            }
        });
        
    });
    
    
    function redirect(page, provider) {
        if (page != '' && provider != 'form') {
            if (popup_domination_new_window != "yes") 
            {
                window.location.href = decodeURIComponent(page);
            }
            else window.open(decodeURIComponent(page),"popdom");
        }
    }
    
    //suspect this function is no longer required...
    function social_submit() {
        if ($('#popup_domination_lightbox_wrapper .fbemail').val() != 'none' && $('#popup_domination_lightbox_wrapper .fbemail').val() != 'none') {
            var checked = false;
            $('#popup_domination_lightbox_wrapper :text').each(function() {
                var $this = $(this), val = $this.val();
                if ($this.data('default_value') && val == $this.data('default_value')) {
                    if (checked)
                    $this.val('').focus();
                    checked = false;
                }
                if (val == '') {
                    checked = false;
                } else {
                    if (val == $this.data('default_value')) {
                        checked = false;
                    } else {
                        checked = true;
                    }
                }
            });
            if (checked) {
                var email = $('#popup_domination_lightbox_wrapper .fbemail').val();
                var name = $('#popup_domination_lightbox_wrapper .fbname').val();
                var listid = $('#popup_domination_lightbox_wrapper .listid').val();
                var provider = $('#popup_domination_lightbox_wrapper .provider').val();
                var mailingid = $('#popup_domination_lightbox_wrapper .mailingid').val();
                $('#popup_domination_lightbox_wrapper input[type="submit"]').attr('disabled', 'disabled');
                $('#popup_domination_lightbox_wrapper .form input').hide();
                $('#popup_domination_lightbox_wrapper .wait').show();
                $('#popup_domination_lightbox_wrapper .error').hide();
                if (provider != 'form' && provider != 'aw' && provider != 'nm') {
                    var data = {
                        action: 'popup_domination_lightbox_submit',
                        name: name,
                        email: email,
                        provider: provider,
                        listid: listid
                    };
                    jQuery.post(popup_domination_admin_ajax, data, function(response) {
                        if (response.length > 4) {
                            $('#popup_domination_lightbox_wrapper input[type="submit"]').removeAttr('disabled', 'disabled');
                            $('#popup_domination_lightbox_wrapper .form input').show();
                            $('#popup_domination_lightbox_wrapper .wait').hide();
                        } else {
                            close_box(popup_domination.popupid,mailingid);
                            if (check_split_cookie() != true) {
                                var popupid = popup_domination.popupid;
                                var data = {
                                    action: 'popup_domination_analytics_add',
                                    stage: 'opt-in',
                                    popupid: popup_domination.popupid
                                };
                                jQuery.post(popup_domination_admin_ajax, data, function() {
                                    redirect(popup_domination.redirect, provider);
                                });
                            } else {
                                redirect(popup_domination.redirect, provider);
                            }
                        }
                    });
                } else {
                    if (check_split_cookie() != true) {
                        var popupid = popup_domination.popupid;
                        var data = {
                            action: 'popup_domination_analytics_add',
                            stage: 'opt-in',
                            popupid: popup_domination.popupid
                        };
                        jQuery.post(popup_domination_admin_ajax, data, function() {
                            $('#popup_domination_lightbox_wrapper form').submit();
                            close_box(popup_domination.popupid,mailingid);
                        });
                        return false;
                    } else {
                        $('#popup_domination_lightbox_wrapper form').submit();
                        close_box(popup_domination.popupid,mailingid);
                    }
                    return false;
                }
            }
            return false;
        }
    }
    
    function register_view() {
        var data = '';
        if (check_split_cookie() != true) {
            data = {
                action: 'popup_domination_analytics_add',
                stage: 'show',
                popupid: popup_domination.popupid
            };
        } else {
            var date = new Date();
            date.setTime(date.getTime() + (86400*1000));
            set_cookie('popup_dom_split_show','Y', date);
            set_cookie('popup_domination_lightbox',popup_domination.popupid,date);
            data = {
                action: 'popup_domination_ab_split',
                stage: 'show',
                popupid: popup_domination.popupid,
                camp : popup_domination.campaign
            };
        }
        jQuery.post(popup_domination_admin_ajax, data);
    }
    
    function register_all_views() {
        var campaigns_on_page = [];
        $('.campaignid').each(function(){
            campaigns_on_page.push($(this).val());
        });
        $.unique(campaigns_on_page); //we won't register 2 views of the same campaign on the same page
        for (var i = 0; i < campaigns_on_page.length; i++ )
        {
            var data = '';
            if (check_split_cookie() != true) {
                data = {
                    action: 'popup_domination_analytics_add',
                    stage: 'show',
                    popupid: campaigns_on_page[i]
                };
            } else {
                var date = new Date();
                date.setTime(date.getTime() + (86400*1000));
                set_cookie('popup_dom_split_show','Y', date);
                set_cookie('popup_domination_lightbox',popup_domination.popupid,date);
                data = {
                    action: 'popup_domination_ab_split',
                    stage: 'show',
                    popupid: popup_domination.popupid,
                    camp : popup_domination.campaign
                };
            }
            if (!check_cookie(campaigns_on_page[i])) {
                jQuery.post(popup_domination_admin_ajax, data);
            }
        }
    }
    
    function register_optin(prov,mailingid,campaignid) {
        close_box(campaignid, mailingid);
        var data = '';
        if (check_split_cookie() != true) {
            data = {
                action: 'popup_domination_analytics_add',
                stage: 'opt-in',
                popupid: campaignid
            };
        } else {
            data = {
                action: 'popup_domination_ab_split',
                stage: 'opt-in',
                popupid: campaignid,
                camp : popup_domination.campaign
            };
        }
        
        //submit depending on provider
        if (prov == 'form' || prov == 'aw' || typeof prov == "undefined") {
            jQuery.post(popup_domination_admin_ajax, data, function() {
                $('#popup_domination_lightbox_wrapper form').submit();
            });
        } else {
            jQuery.post(popup_domination_admin_ajax, data, function() {
                redirect(popup_domination.redirect, prov);
            });
        }
        if (popup_domination.google_goal != "")
        {
            eval(popup_domination.google_goal);
        }
    }
    
    function enable_unload() {
        window.onbeforeunload = function(e) {
            if (exit_shown === false) {
                e = e || window.event;
                exit_shown = true;
                setTimeout(show_lightbox,1000);
                if (e) { e.returnValue = popup_domination.unload_msg; }
                return popup_domination.unload_msg;
            }
        };
    }

    function enable_link_select(classname) {
        if ( $.isFunction($.fn.on) ) {
            $('body').on('click', '.'+classname, function(e) {
                e.preventDefault();
                show_lightbox(true);
            }).css('cursor','pointer');
        } else {
            $('.'+classname).live('click',function(e) {
                e.preventDefault();
                show_lightbox(true);
            }).css('cursor','pointer');
        }
    }

    function window_mouseout(e) {
        var scrollTop = jQuery(window).scrollTop()+5;
        var scrollBottom = jQuery(window).scrollTop() + jQuery(window).height()-5;
        var scrollLeft = jQuery(window).scrollLeft()+5;
        var scrollRight = scrollLeft + jQuery(window).width()-5;
        var mX = e.pageX, mY = e.pageY, el = $(window).find('html');
        
        if ((mX <= scrollLeft) || (mY <= scrollTop) || (mY>= scrollBottom)) {
            show_lightbox();
        }
    }
    
    function show_lightbox(linkclick) {
        var siteWordpressUrl = popup_domination_admin_ajax.replace('/wp-admin/admin-ajax.php','');
        if (!isMobile && !isHidden && !isRefBlock && popup_domination.show_anim != "inpost")
        {
            var provider = '';
            $(document).unbind('focus',show_lightbox);
            $('html,body').unbind('mouseout',window_mouseout);
            show_animation();
            if (popup_domination.center && popup_domination.center == 'Y') {
                center_it();
            }
            register_view();
            
        }
        if (popup_domination.show_anim == "inpost")
        {
            if (!check_cookie(popup_domination.popupid,popup_domination.mailingid))
            {
                register_view();
            }
            else 
            {
                //check_campaign_cookies(true);
            }
        }
        provider = $('#popup_domination_lightbox_wrapper .provider').val();
        if (popup_domination_new_window == 'yes') { var new_window_target= "popdom"; }
        else { var new_window_target = ''; }
        $('#popup_domination_lightbox_wrapper .wait').remove(); // set up all forms to use same wait gif placement, after submit button
        $('#popup_domination_lightbox_wrapper input[type=submit]').after('<div class="wait" style="display:none;"><img src="'+siteWordpressUrl+'/wp-content/plugins/popup-domination/css/images/wait.gif" /></div><div class="error" style="display:none;color:red;"></div>');
        if (provider == 'aw') {
            var html = $('#popup_domination_lightbox_wrapper .form div').html();
            if ($('#popup_domination_lightbox_wrapper .form form').html() == null) {
                $('#popup_domination_lightbox_wrapper .form div').html('<form method="post" action="http://www.aweber.com/scripts/addlead.pl" target="'+new_window_target+'">'+html+'</form>')
            } else {
                $('#popup_domination_lightbox_wrapper .form form').remove();
                $('#popup_domination_lightbox_wrapper .form div').html('<form method="post" action="http://www.aweber.com/scripts/addlead.pl" target="'+new_window_target+'">'+html+'</form>')
            }
        }
        
        // IE placeholder workaround
        if ($.browser.msie  && $.browser.version < 10)
        {
            console.log('this is IE');
            window.setTimeout( function() {
                $('#popup_domination_lightbox_wrapper input[type=text]').each(function() {
                    $(this).val($(this).attr("placeholder"));
                    $(this).css('color','grey');
                    $(this).focus(function(e) {
                        if ($(this).val() == $(this).attr("placeholder") ) 
                        {
                            $(this).val('');
                            $(this).removeAttr('style');
                            e.preventDefault();
                        }
                    }).blur(function(e) {
                        if ($(this).val() == '') 
                        {
                            $(this).val($(this).attr("placeholder") );
                            $(this).css('color','grey');
                            e.preventDefault();
                        }
                    });
                });
            } , 100);
        }
        
        if (typeof popdom_preview == "undefined") {
            if (!linkclick) check_all_cookies(true);
            register_all_views();
        }
        return false;
    };
    
    /* 
        decides how lightbox is to show 
        this is all done with CSS transitions, so here we set the CSS
        styles for the "hidden" aspect, then just call center_it() to make it show up!
    */
    function show_animation() {
    
    if ($.browser.msie && $.browser.version < 10) {
        //IE.hate++ -=- Using a Javasccript animation fallback.
        $('#popup_domination_lightbox_wrapper >.lightbox-main').css("opacity",0);
        $('#popup_domination_lightbox_wrapper ').show();
        if (popup_domination.show_anim == 'fade') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({opacity:1},500,center_it);
        } else if (popup_domination.show_anim == 'slide') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({top: 0 - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false)},0);
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                top: ($(window).height() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false))/2,
                opacity:1
            },
            1500,
            center_it);
        } else if (popup_domination.show_anim == 'slideUp') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({top: $(window).height() + $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false)},0);
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                top: ($(window).height() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false))/2,
                opacity:1
            },
            1500,
            center_it);
        } else if (popup_domination.show_anim == 'slideLeft') { /* Slide TO the left, FROM the right! */
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false))/2,
                left: $(window).width() + $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false)
            },0);
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                left: ($(window).width() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerWidth(false))/2,
                opacity:1
            },
            1500,
            center_it);
        } else if (popup_domination.show_anim == 'slideRight') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false))/2,
                left: 0 - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false)
            },0);
            $('#popup_domination_lightbox_wrapper >.lightbox-main').animate({
                left: ($(window).width() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerWidth(false))/2,
                opacity:1
            },
            1500,
            center_it);
        } 
        else {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css("opacity",1);
        }
        center_it();
    }
    
    else {
    
        $('#popup_domination_lightbox_wrapper >.lightbox-main').css("transition","all 0");
        var theTransition = "all";
        if (popup_domination.show_anim == 'fade') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css({
                top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false))/2,
                left: ($(window).width() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false))/2,
                opacity:0
            });
            theTransition = "opacity";     
        } else if (popup_domination.show_anim == 'slide') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css({
                top: 0 - $(window).height(),
                left: ($(window).width() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false))/2
            });
            theTransition = "top";
        } else if (popup_domination.show_anim == 'slideUp') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css({
                top: $(window).height(),
                left: ($(window).width() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false))/2
            });
            theTransition = "top";

        } else if (popup_domination.show_anim == 'slideLeft') { /* Slide TO the left, FROM the right! */
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css({
                top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false))/2,
                left: $(window).width()
            });
            theTransition = "left";

        } else if (popup_domination.show_anim == 'slideRight') {
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css({
                top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper >.lightbox-main').outerHeight(false))/2,
                left: 0 - $(window).width()
            });
            theTransition = "left";
        } 
        else {
            //catch-all for open immediately or broken/no setting saved.
            $('#popup_domination_lightbox_wrapper >.lightbox-main').css("opacity",1);
            theTransition = "none";
        }
            $('#popup_domination_lightbox_wrapper').show();
            center_it(theTransition);
        }
        
    }

    function center_it(theTransition) {
        if ($.browser.msie && $.browser.version < 10)
        {
            transform = 1;
            if ($(window).width() <= $('.popup-dom-lightbox-wrapper .lightbox-main').outerWidth(false) ) {
                transform = $(window).width() / $('.popup-dom-lightbox-wrapper .lightbox-main').outerWidth(false);
            }
            if ( $(window).height() <= $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false) ) {
                transform = ($(window).height() / $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false) < transform) ? $(window).height() / $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false) : transform;
            }
            var styles = {
                position:'fixed',
                left: ($(window).width() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerWidth(false))/2,
                top: ($(window).height() - $('.popup-dom-lightbox-wrapper .lightbox-main').outerHeight(false))/2 + 10,
                transform: "scale("+transform+")"
            };
            if (styles.top < 10) styles.top = 10;
            $('.popup-dom-lightbox-wrapper .lightbox-main').css(styles);
        }
    
        else 
        {
            if(typeof theTransition == "undefined") theTransition = "all";
            scaleNumber=1;
            if ($('.popup-dom-lightbox-wrapper .lightbox-overlay').is(":visible") || $('.popup-dom-lightbox-wrapper .lightbox-overlay').length == 0)
            {
                if ($(window).width() <= $('#popup_domination_lightbox_wrapper  .lightbox-main').outerWidth(false) ) {
                    scaleNumber = $(window).width() / $('#popup_domination_lightbox_wrapper .lightbox-main').outerWidth(false);
                }
                if ( $(window).height() <= $('#popup_domination_lightbox_wrapper .lightbox-main').outerHeight(false) ) {
                    scaleNumber = ($(window).height() / $('#popup_domination_lightbox_wrapper .lightbox-main').outerHeight(false) < scaleNumber) ? $(window).height() / $('#popup_domination_lightbox_wrapper .lightbox-main').outerHeight(false) : scaleNumber;
                }
            
                var styles = {
                    display: "block",
                    position:'fixed',
                    left: ($(window).width() - $('#popup_domination_lightbox_wrapper .lightbox-main').outerWidth(false))/2,
                    top: 10 + ($(window).height() - $('#popup_domination_lightbox_wrapper .lightbox-main').outerHeight(false))/2,
                    transform: "scale("+scaleNumber+")",
                    transformOrigin: "center center 0",
                    opacity: 1,
                    transition: theTransition + " 1s"
                };
                $('#popup_domination_lightbox_wrapper .lightbox-main').css(styles);
            }
        }
    };
    
    function init_center() {
        center_it();
        if ($.browser.msie){
            $(window).resize(center_it);
        }
        else 
        {
            $(window).resize(function(){
                center_it('all');
            });
        }
    };

    function max_zindex() {
        var maxz = 0;
        $('body *').each(function() {
            var cur = parseInt($(this).css('z-index'));
            maxz = cur > maxz ? cur : maxz;
        });
        $('#popup_domination_lightbox_wrapper ').css('z-index',maxz+1);
    };
    
    function hide_box(popupid) {
        var theCampaigns;
        $('.campaignid').each(function(){
            if ($(this).val() == popupid)
            {
                $(this).parents('#popup_domination_lightbox_wrapper , #popdom-inline-container').hide();
            }
        });
        if ($('.campaignid').length == 0)
        {
            //console.log('no mailing list attached');
            $('#popup_domination_lightbox_wrapper , #popdom-inline-container').hide();
        }
    }
    
    //closes the popup, if mailingid is set sets cookie for mailing list too
    function close_box(id, mailingid) {
        var elem = $('#popup_domination_lightbox_wrapper , #popdom-inline-container');
        $('.campaignid').each(function(){
            if ($(this).val() == id)
            {
                elem = $(this).parents('#popup_domination_lightbox_wrapper , #popdom-inline-container');
            }
        });        
        clearTimeout(timer);
        if (typeof popdom_preview == "undefined") //if this is a preview window don't set the cookie!
        {
            if (popup_domination.cookie_time && popup_domination.cookie_time > 0) {
                var date = new Date();
                date.setTime(date.getTime() + (popup_domination.cookie_time*86400*1000));
            }
            else {
                var date = new Date();
                date.setTime(date.getTime() + (1*86400*1000));
            }
            if (id == '0') {
                id = 'zero';
            }else if (id == '1') {
                id = 'one';
            }else if (id == '3') {
                id = 'three';
            }else if (id == '4') {
                id = 'four';
            }
            if (popup_domination.show_opt != 'tab' || ($.browser.msie && $.browser.version < 10) )
            {
                set_cookie('popup_domination_hide_lightbox'+id,'Y',date);
                stop_video();
            }            
            if (check_split_cookie()) {
                set_cookie('popup_domination_hide_ab'+popup_domination.campaign,'Y',date);
            }
            if (mailingid != false) {
            //this means we're setting the cookie for the mailing list - someone opted in!
                date = new Date();
                date.setFullYear(date.getFullYear() + 100); // 100 years ought to do it
                set_cookie('popup_domination_hide_mailing'+mailingid,'Y',date);
            }
        }
        if (popup_domination.show_opt == 'tab' && !$.browser.msie )
        {
            create_tab();
        }
        else {
            elem.hide();
        }
    };
    
    function create_tab(classname) {
        $('#popup_domination_lightbox_wrapper, .lightbox-main').show();
        $('#popup_domination_lightbox_wrapper').css({height:0,minHeight:0});
        $('.popup-dom-lightbox-wrapper .lightbox-overlay').hide();
        var bigWidth = $('#popup_domination_lightbox_wrapper >.lightbox-main').outerWidth(false);
        var shift = 0 - (bigWidth / 15); //bigWidth divided by ten (as we scale to a tenth) and an additional half as much again - this gets it about a third of the width?
        theStyle = {
            position:"fixed",
            left: shift + "px",
            top: popup_domination.tab_height+"px",
            transform: "scale(0.1)",
            transformOrigin: "0 0 0"
        };
        var theOverlay = "<div id='pd_tab_overlay' title='Click to opt in'></div>";
        $('#popup_domination_lightbox_wrapper > .lightbox-main').prepend(theOverlay);
        $('#pd_tab_overlay').css({width: "101%",height:"101%",zIndex:'1',backgroundColor:"transparent",position:'fixed',cursor:'pointer',top:"-10px",left:0,boxShadow:"0 0 2em black , 0 0 2em black inset"});
        $('#popup_domination_lightbox_wrapper > .lightbox-main').css(theStyle);
        $('#pd_tab_overlay').click( function(){
        $('#popup_domination_lightbox_wrapper > .lightbox-main').css({transition:'all 1s'})
            $('.popup-dom-lightbox-wrapper .lightbox-overlay').show();
            $('#pd_tab_overlay').remove();
            $('#popup_domination_lightbox_wrapper').show();
            provider = $('#popup_domination_lightbox_wrapper .provider').val();
            if (popup_domination_new_window == 'yes') { var new_window_target= "popdom"; }
            else { var new_window_target = ''; }
            $('#popup_domination_lightbox_wrapper .wait').remove(); // set up all forms to use same wait gif placement, after submit button
            $('#popup_domination_lightbox_wrapper input[type=submit]').after('<div class="wait" style="display:none;"><img src="//'+document.domain+'/wp-content/plugins/popup-domination/css/images/wait.gif" /></div><div class="error" style="display:none;color:red;"></div>');
            if (provider == 'aw') {
                var html = $('#popup_domination_lightbox_wrapper .form div').html();
                if ($('#popup_domination_lightbox_wrapper .form form').html() == null) {
                    $('#popup_domination_lightbox_wrapper .form div').html('<form method="post" action="http://www.aweber.com/scripts/addlead.pl" target="'+new_window_target+'">'+html+'</form>')
                } else {
                    $('#popup_domination_lightbox_wrapper .form form').remove();
                    $('#popup_domination_lightbox_wrapper .form div').html('<form method="post" action="http://www.aweber.com/scripts/addlead.pl" target="'+new_window_target+'">'+html+'</form>')
                }
            }
            center_it();
            register_all_views();
        });
        $('#pd_tab_overlay').hover( 
            function(){
                $('#popup_domination_lightbox_wrapper > .lightbox-main').css({left:'-10px',transition:'all 0.5s'})
            },
            function(){
                $('#popup_domination_lightbox_wrapper > .lightbox-main').css({left:shift+'px',transition:'all 0.5s'})
            }
        );
    }
    
    function stop_video() {
        //Required for some plugins such as Vimeo
        $('#popup_domination_lightbox_wrapper .lightbox-video').remove();
    };

    function set_cookie(name,value,date) {
        window.document.cookie = [name+'='+escape(value),'expires='+date.toUTCString(),'path='+popup_domination.cookie_path].join('; ');
    };

    function check_cookie(id,mailingid) {
        if (id == '0') {
            id = 'zero';
        }else if (id == '1') {
            id = 'one';
        }else if (id == '3') {
            id = 'three';
        }else if (id == '4') {
            id = 'four';
        }
        if (get_cookie('popup_domination_hide_lightbox'+id) == 'Y' || get_cookie('popup_domination_hide_mailing'+mailingid) == 'Y')
        {
            stop_video();
            return true;
        }
        return false;
    };
    
    function check_all_cookies(close_matches)
    {
        var campaigns_on_page = [];
        campaigns_on_page.push(popup_domination.popupid);
        var mailings_on_page = [];
        $('.campaignid').each(function(){
            campaigns_on_page.push($(this).val());
        });
        $('.mailingid').each(function(){
            mailings_on_page.push($(this).val());
        });

        $.unique(campaigns_on_page);
        $.unique(mailings_on_page);
        for (var i = 0; i < campaigns_on_page.length; i++ )
        {
            var cookieExists = check_cookie(campaigns_on_page[i]);
            if (close_matches && cookieExists)
            {
                hide_box(campaigns_on_page[i]);
                console.log('campaign cookie match - hiding ('+campaigns_on_page[i]+")");
            }
        }
        for (var i = 0; i < mailings_on_page.length; i++ )
        {
            var cookieExists = check_cookie(false,mailings_on_page[i]);
            if (close_matches && cookieExists)
            {
                //this code duplicates the hide_box() code but uses the mailing list ID as the match
                $('.mailingid').each(function(){
                    if ($(this).val() == mailings_on_page[i])
                    {
                        $(this).parents('#popup_domination_lightbox_wrapper , #popdom-inline-container').hide();
                    }
                });  
                console.log('mailing list cookie match - hiding ('+mailings_on_page[i]+")");
            }
        } 
    }

    function check_split_cookie() {
        return popup_domination.splitcookie;
    }

    function check_impressions() {
        var ic = 1, date = new Date();
        if (ic = get_cookie('popup_domination_icount')) {
            ic = parseInt(ic);
            ic++;
            if (ic == popup_domination.impression_count) {
                date.setTime(date.getTime());
                set_cookie('popup_domination_icount',popup_domination.impression_count,date);
                return false;
            }
        } else {
            ic = 1;
        }
        date.setTime(date.getTime() + (7200*1000));
        set_cookie('popup_domination_icount',ic,date);
        return true;
    };
    
    function get_cookie(cname) {
        var cookie = window.document.cookie;
        if (cookie.length > 0) {
            var c_start = cookie.indexOf(cname+'=');
            if (c_start !== -1) {
                c_start = c_start + cname.length+1;
                var c_end = cookie.indexOf(';',c_start);
                if (c_end === -1) { c_end = cookie.length; }
                return unescape(cookie.substring(c_start,c_end));
            }
        }
        return false;
    };
    
})(jQuery);