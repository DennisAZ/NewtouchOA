(function() {
  var defaultOpt = {
    type: "classic",
    smsCallCount: 3,
    smsInterval: 3,
    smsAlertTimer: null,
    smsRef: 30,
    smsOn: "0",
    callSound: 2,
    userId: 0
  };
  
  window.sms = {
    initialize: function(opt) {
      $.extend(this, defaultOpt, opt);
      this.checkShortMsrg();
    },
    checkShortMsrg: function() {
      sms.ajaxCount = sms.ajaxCount || 0;
      if (this.shortMsrgTimer) {
        clearTimeout(this.shortMsrgTimer);
      }
      var self = this;
      var rtJson = null;
    
      var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/remindCheck2.act";
      $.ajax({
        type: "GET",
        dataType: "text",
        url: url,
        success: function(text){
          var rtJson = YH.parseJson(text);
          if (rtJson.rtState == '0') {
            if (rtJson.rtData == 0) {
            //存在未读短信，打开短信展示窗口，然后返回（注意，关闭窗口后，轮训调用下一次检查）
            } else if (rtJson.rtData > 0) {
              //显示短信提示按钮
              sms.showSmsButton(rtJson.rtData);
              //showShortMsrg();
            } else if (rtJson.rtData < 0){
              //空闲强制自动离线
              doLogout();
            }
          }
        },
        complete: function() {
          sms.shortMsrgTimer = setTimeout(function() {
            sms.checkShortMsrg();
          }, sms.smsRef * 1000);
        }
      });
    },
    /**
     * 展示内部短信窗口
     */
    showShortMsrg: function () {
      
      var left = 5;
      var top = $(document).height() - 360 - 5;
      if (!sms.smsWindow) {
        var cfg = {
          'title' : 'OA平台',
          'draggable': true,
          resizable: {},
          tbar: [{
            id: 'close',
            preventDefault: true,
            handler: function(e, t, p) {
              p.hide();
            }
          }],
          lazyContainer: true,
          showEffect: function(speed, callback) {
            var self = this;
            this.expand(speed, callback);
          },
          hideEffect: function() {
            this.collapse.apply(this, arguments)
          },
          cls: 'window-group window-active',
          'type': 'iframe',
          'width': 530,
          'icon': imgPath + '/mainframe/smsIcon.png',
          'height': 430,
          'src': contextPath + '/core/frame/ispirit/smsBox1.jsp',
          'listeners': {
            'hide': {
              after: function(e, t) {
                sms.smsAlert = false;
                //隐藏短信窗口,设置iframe指向空                t.setSrc('about:blank');
                sms.checkShortMsrg();
              }
            },
            show: {
              after: function(e, t) {
                sms.count = 0;
                clearTimeout(sms.shortMsrgTimer);
              }
            }
          }
        };
        if (this.type == "classic") {
          cfg.left = 5;
          cfg.top = $(window).height() - 480;
        }
        else {
          cfg.modal = true
        }
        sms.smsWindow = new YH.Window(cfg);
      }
      else {
        sms.smsWindow.setSrc(contextPath + '/core/frame/ispirit/smsBox1.jsp');
      }
      sms.smsWindow.show();
      sms.hideSmsButton();
    },
    /**
     * 显示左下状态栏短信按钮
     * @return
     */
    showSmsButton: function () {
      if (sms.smsOn == '0') {
        if (!sms.smsAlert) {
          sms.smsAlert = true;
          sms.count = sms.smsCallCount;
          YH.getCmp('smsBtn') && YH.getCmp('smsBtn').setStatus('active');
          $('#smsFlash').css({
            'display': 'block'
          });
          clearTimeout(sms.smsAlertTimer);
          sms.smsCallSound();
        }
      }
      else {
        if (sms.callSound >= 0) {
          sms.playSound(contextPath + '/core/frame/common/sms/audio/' + sms.callSound + '.wav');
        }
        else {
          sms.playSound(contextPath + '/theme/sound/' + sms.userId + '.swf');
        }
        sms.showShortMsrg();
      }
    },
    /**
     * 关闭内部短信窗口
     */
    hideShortMsrg: function () {
      if (sms.smsWindow){
        sms.smsWindow.hide();
      }
    },
    /**
     * 隐藏左下状态栏短信按钮
     * @return
     */
    hideSmsButton: function (){
      YH.getCmp('smsBtn') && YH.getCmp('smsBtn').setStatus('default');
      $('#smsFlash').css({
        'display': 'none'
      });
    },
    /**
     * 播放flash音频
     * @param name
     * @return
     */
    playSound: function (name) {
      if ($.browser.msie) {
        if (!$("bgsound").length) {
          var bgsound = $("<bgsound></bgsound>");
          $("head").append(bgsound);
        }
        $("bgsound").attr("src", name);
      }
      else {
        if (!$('#soundPlayer').length) {
          $("body").append('<div id="soundPlayer" style="visibility: hidden"><audio src="' + name + '" autoplay><embed loop="false" autostart="true" src="' + name + '"></embed></audio></div>');
        }
        else if ($('#soundPlayer audio').length) {
          $('#soundPlayer audio')[0].play();
        }
        else {
          $('#soundPlayer').remove();
          sms.playSound(name);
        }
      }
    },
    /**
     * 播放短信提示音
     * @return
     */
    smsCallSound: function () {
      if (sms.count < 1) {
        return;
      }
      if (sms.count == 1) {
        if (sms.smsAlertTimer) {
          clearTimeout(sms.smsAlertTimer);
        }
      }else if (sms.count > 1) {
        if (sms.smsAlertTimer) {
          clearTimeout(sms.smsAlertTimer);
        }
        sms.smsAlertTimer = setTimeout(function() {
          sms.smsCallSound();
        }, sms.smsInterval * 60 * 1000);
      }
      if (sms.callSound >= 0) {
        sms.playSound(contextPath + '/core/frame/common/sms/audio/' + sms.callSound + '.wav');
      }
      else {
        sms.playSound(contextPath + '/theme/sound/' + sms.userId + '.swf');
      }
      sms.count--;
    }
  };
}) ();
