//-- 滚动条 --
(function($) {
  $.fn.extend( {
    mousewheel : function(Func) {
      return this.each(function() {
        var _self = this;
        _self.D = 0;// 滚动方向
          if ($.browser.msie || $.browser.safari) {
            _self.onmousewheel = function() {
              _self.D = event.wheelDelta;
              event.returnValue = false;
              Func && Func.call(_self);
            };
          } else {
            _self.addEventListener("DOMMouseScroll", function(e) {
              _self.D = e.detail > 0 ? -1 : 1;
              e.preventDefault();
              Func && Func.call(_self);
            }, false);
          }
        });
    }
  });
})(jQuery);

// 短信箱自动关闭时间，秒
var smsbox_close_timeout = 5;
var smsbox_close_countdown = null;
var smsbox_close_timer = null;

(function($) {
  $(document)
      .ready(function($) {
        if (!bSmsPriv)
          return;

        // 无新短信提示
          $('#no_sms').hide();
          $('#smsbox_tips').html($('#no_sms').html()).show();
          $('#smsbox_tips').bind('_show', function() {
            window.clearInterval(smsbox_close_countdown);
            window.clearTimeout(smsbox_close_timer);

            smsbox_close_countdown = window.setInterval(function() {
              var seconds = parseInt($('#smsbox_close_countdown').text());
              if (seconds > 0) {
                $('#smsbox_close_countdown').text(seconds - 1);
              } else {
                window.clearInterval(smsbox_close_countdown);
                $('#smsbox_close_countdown').text(smsbox_close_timeout);
              }
            }, 1000);

            smsbox_close_timer = window.setTimeout(function() {
              if ($('#smsbox_tips:visible').length > 0)
                close_window();
            }, smsbox_close_timeout * 1000);
          });

          // 分组按钮
          $('#group_by_name,#group_by_type').click(function() {
            if ($(this).attr('class').indexOf('active') >= 0)
              return;

            $('#group_by_name').toggleClass('active');
            $('#group_by_type').toggleClass('active');
            FormatSms();
          });

          // 滚动按钮
          $('#smsbox_scroll_up,#smsbox_scroll_down').hover(function() {
            $(this).addClass('active');
          }, function() {
            $(this).removeClass('active');
          });
          $('#smsbox_scroll_up').click(
              function() {
                var listContainer = $('#smsbox_list_container');
                var blockHeight = $('div.list-block:first', listContainer)
                    .outerHeight();
                listContainer.animate( {
                  scrollTop : listContainer.scrollTop() - blockHeight * 3
                }, 300);
              });
          $('#smsbox_scroll_down').click(
              function() {
                var listContainer = $('#smsbox_list_container');
                var blockHeight = $('div.list-block:first', listContainer)
                    .outerHeight();
                listContainer.animate( {
                  scrollTop : listContainer.scrollTop() + blockHeight * 3
                }, 300);
              });

          // 列表鼠标滚轮事件
          $("#smsbox_list_container").mousewheel(function() {
            $('#smsbox_list_container').stop().animate( {
              'scrollTop' : ($('#smsbox_list_container').scrollTop() - this.D)
            }, 0);
          });

          // 列表内容变化
          $('#smsbox_list_container').bind(
              '_change',
              function() {
                $('#smsbox_scroll_up,#smsbox_scroll_down').toggle(
                    $(this).outerHeight() < $(this).attr('scrollHeight'));
                if (newSmsArray.length > 0) {
                  $('#smsbox_tips').hide();
                  $('#no_sms').hide()
                  $('#smsbox_list_container > div.list-block:first').trigger(
                      'click');
                } else {
                  $('#smsbox_tips').html($('#no_sms').html()).show(0,
                      function() {
                        $(this).triggerHandler('_show');
                      });
                }
              });

          // 列表hover效果
          var listBlocks = $('#smsbox_list_container > div.list-block');
          listBlocks.live('mouseenter', function() {
            $(this).addClass('list-block-hover');
          });
          listBlocks.live('mouseleave', function() {
            $(this).removeClass('list-block-hover');
          });

          // 列表click事件
          listBlocks
              .live(
                  'click',
                  function() {
                    if ($(this).attr('class').indexOf('list-block-active') > 0)
                      return;
                    $('#smsbox_list_container > div.list-block').removeClass(
                        'list-block-active');
                    $(this).addClass('list-block-active');
                    $('table', this).removeClass('unread');

                    var bGroupByName = $('#group_by_name').attr('class')
                        .indexOf('active') >= 0;
                    var id = $(this).attr('group_id');
                    var html = '';
                    selectedRecvSmsIdStr = selectedSendSmsIdStr = '';
                    for ( var i = 0; i < newSmsArray.length; i++) {
                      if (bGroupByName
                          && !(newSmsArray[i].from_id == id
                              && newSmsArray[i].to_id == loginUser.seqId || newSmsArray[i].from_id == loginUser.seqId
                              && newSmsArray[i].to_id == id) || !bGroupByName
                          && newSmsArray[i].type_id != id)
                        continue;
                      // 接收的短信
                      if (newSmsArray[i].receive)
                        selectedRecvSmsIdStr += newSmsArray[i].sms_id + ',';
                      else
                        selectedSendSmsIdStr += newSmsArray[i].sms_id + ',';

                      newSmsArray[i].unread = 0;
                      var name = (bGroupByName && newSmsArray[i].receive == 1) ? newSmsArray[i].type_name
                          : newSmsArray[i].from_name;
                      var time = newSmsArray[i].send_time.indexOf(' ') > 0 ? newSmsArray[i].send_time
                          .substr(0, 5)
                          : newSmsArray[i].send_time;
                      html += CreateMsgBlock( {
                        "sms_id" : newSmsArray[i].sms_id,
                        "class" : (newSmsArray[i].receive ? "from" : "to"),
                        "user" : newSmsArray[i].from_id,
                        "name" : name,
                        "time" : time,
                        "content" : newSmsArray[i].content,
                        "url" : newSmsArray[i].url
                      });
                    }
                    $('#smsbox_msg_container').html(html);
                    window.setTimeout(function() {
                      $('#smsbox_msg_container').animate(
                          {
                            scrollTop : $('#smsbox_msg_container').attr(
                                'scrollHeight')
                          }, 300);
                    }, 1); // 延迟1毫秒后取出的scrollHeight就是正确的，奇怪的问题

                  });

          // 全部已阅
          $('#smsbox_read_all').click(function() {
            var array = GetSmsIds();
            RemoveSms(array.recv, array.send, 0);
            close_window();
          });

          // 全部删除
          $('#smsbox_delete_all').click(function() {
            var array = GetSmsIds();
            RemoveSms(array.recv, array.send, 1);
            close_window();
          });

          // 全部详情
          $('#smsbox_detail_all').click(
              function() {
                var sms_id_str = '';
                var sms_id_str0 = '';
                var nav_mail_url = '';
                for ( var i = 0; i < newSmsArray.length; i++) {
                	var sms_id=   nav_mail_url = newSmsArray[i].sms_id+"";
                  if (newSmsArray[i].receive != '1' ||sms_id.indexOf("r")!=-1)
                    continue;


                   sms_id_str += newSmsArray[i].sms_id + ',';

                  if (newSmsArray[i].type_id == '0')
                    sms_id_str0 += newSmsArray[i].sms_id + ',';
                }
                newSmsArray = [];
                FormatSms();
                markRead(sms_id_str);
                jQuery('#smsbox_msg_container').html('');
                if(""!=sms_id_str){
                openURL(contextPath + '/yh/core/funcs/system/ispirit/sms/act/YHSmsBox/viewDetails.act?smsIds=' + sms_id_str );
                selectedRecvSmsIdStr = selectedSendSmsIdStr = '';
              }
              });

          // 已阅
          $('#smsbox_toolbar_read').click(function() {
            //alert(selectedRecvSmsIdStr);
           RemoveSms(selectedRecvSmsIdStr, selectedSendSmsIdStr, 0);
           close_window();
          });

          // 删除
          $('#smsbox_toolbar_delete').click(function() {
            //alert(selectedRecvSmsIdStr);
            RemoveSms(selectedRecvSmsIdStr, selectedSendSmsIdStr, 1);
            close_window();
          });

          // 详情
          $('#smsbox_toolbar_detail').click(
              function() {//alert(selectedRecvSmsIdStr);
                if (!selectedRecvSmsIdStr)
                  return;
                var sms_id_str0 = '';
                var nav_mail_url = '';
                var array = [];
                for ( var i = 0; i < newSmsArray.length; i++) {
                  var id = newSmsArray[i].sms_id+"";
             
                  if (selectedRecvSmsIdStr.indexOf(id + ',') == 0
                      || selectedRecvSmsIdStr.indexOf(',' + id + ',') > 0 ||id.indexOf("r")!=-1) {
                    nav_mail_url = newSmsArray[i].url;
                    if (newSmsArray[i].type_id == '0')
                      sms_id_str0 += id + ',';
                    continue;
                  } else if (selectedSendSmsIdStr.indexOf(id + ',') == 0
                      || selectedSendSmsIdStr.indexOf(',' + id + ',') > 0) {
                    continue;
                  }

                  array[array.length] = newSmsArray[i];
                }
           
                var linkIdStr="";
                var idStr=selectedRecvSmsIdStr.split(",");
                for(var i=0;i<idStr.length;i++){
                	var dd=idStr[i];
                	if(dd.indexOf("r")==-1 && dd!=""){
                	  linkIdStr+=dd;
                	  linkIdStr+=",";
                	}
                }
                
               
                if(linkIdStr.indexOf("r")==-1 && linkIdStr!=""){
                    markRead(linkIdStr);
	                openURL(contextPath + '/yh/core/funcs/system/ispirit/sms/act/YHSmsBox/viewDetails.act?smsIds=' + linkIdStr);
	                newSmsArray = array;
	                FormatSms(); 
	                close_window();
                }
               
              });
          // 内容块hover效果
          var msgBlocks = $('#smsbox_msg_container > div.msg-block');
          msgBlocks.live('mouseenter', function() {
            $(this).addClass('msg-hover');
          });
          msgBlocks.live('mouseleave', function() {
            $(this).removeClass('msg-hover');
          });
          // 内容块click事件
          msgBlocks.live('click', function() {
            $('#smsbox_msg_container > div.msg-block')
                .removeClass('msg-active');
            $(this).addClass('msg-active');
            selectedRecvSignSmsId = $(this).attr("sms_id");
          });
          // 回复事件
          $('.head > .operation > a.reply', msgBlocks).live('click',
              function() {
                $('#smsbox_textarea').trigger('focus');
              });
          // 查看详情事件
          $('.head > .operation > a.detail', msgBlocks).live('click',
              function() {
                var sms_id = $(this).attr('sms_id');
                var url = $(this).attr('url');
                RemoveSms(sms_id, '', 0);
                openURL(url);
              });

          // 输入框Ctrl + Enter事件
          $('#smsbox_textarea').keypress(function(event) {
            if (event.keyCode == 10 || event.ctrlKey && event.which == 13)
              $('#smsbox_send_msg').triggerHandler('click');
          });

          // 发送
          // $('#smsbox_textarea').bind('focus', function(){
          // $(this.parentNode).addClass('center-reply-focus');
          // });
          // $('#smsbox_textarea').bind('blur', function(){
          // $(this.parentNode).removeClass('center-reply-focus');
          // });

          $('#smsbox_send_msg')
              .click(
                  function() {
                    var msg = $('#smsbox_textarea').val();
                    if (!msg)
                      return;

                    var user_id = $(
                        '#smsbox_msg_container > div.msg-active:first').attr(
                        'user')
                        || $(
                            '#smsbox_list_container > div.list-block-active:first')
                            .attr('user');
                    if (!user_id) {
                      alert('请选择发送用户');
                      return;
                    }

                    var reg = /\n/g;
                    var content = msg.replace(reg, "<br />");
                    var date = new Date();
                    var html = CreateMsgBlock( {
                      "sms_id" : "send_" + (maxSendSmsId++),
                      "class" : "to",
                      "user" : loginUser.seqId,
                      "name" : loginUser.userName,
                      "time" : date.toTimeString().substr(0, 5),
                      "content" : content
                    });
                    $('#smsbox_msg_container').append(html);
                    window.setTimeout(function() {
                      $('#smsbox_msg_container').animate(
                          {
                            scrollTop : $('#smsbox_msg_container').attr(
                                'scrollHeight')
                          }, 300);
                    }, 1);

                    newSmsArray[newSmsArray.length] = {
                      sms_id : "",
                      to_id : user_id,
                      from_id : loginUser.seqId,
                      from_name : loginUser.userName,
                      type_id : "0",
                      type_name : "个人短信",
                      send_time : date.toTimeString().substr(0, 5),
                      unread : 0,
                      content : content,
                      url : "",
                      receive : 0
                    };
                    // newSmsArray = $.merge(array, newSmsArray);

                    jQuery.ajax( {
                      type : 'POST',
                      url : contextPath + '/yh/core/funcs/message/act/YHMessageAct/addMessageBody.act',
                      data : {
                        'user' : user_id,
                        'content' : msg,
                        'smsType' : '0'
                      },
                      dataType : 'text',
                      success : function(data) {
                        $('#smsbox_textarea').val("");
                      },
                      error : function(request, textStatus, errorThrown) {
                        alert('微讯发送失败：' + textStatus);
                      }
                    });

                    $('#smsbox_textarea').focus();
                  });

          LoadSms();
        });
})(jQuery);

function Text2Object(data) {
  try {
    var func = new Function("return " + data);
    return func();
  } catch (ex) {
    return '数据错误：' + data;
  }
}

var maxSendSmsId = 0;
var newSmsArray = [];
var selectedRecvSmsIdStr = selectedSendSmsIdStr = "";
var selectedRecvSignSmsId = "";
function LoadSms(flag) {
  jQuery('#smsbox_tips').html('<div class="loading">正在加载，请稍候……</div>').show();
  flag = typeof (flag) == "undefined" ? "1" : "0";
  jQuery.ajax( {
    type : 'GET',
    url : contextPath + '/yh/core/funcs/system/ispirit/sms/act/YHSmsBox/getNewsMessageForBox.act',
    data : {
      'FLAG' : flag
    },
    success : function(data) {
      var array = Text2Object(data);
      if (typeof (array) != "object" || typeof (array.length) != "number"
          || array.length < 0) {
        jQuery('#smsbox_tips').html('<div class="error">' + array + '</div>')
            .show();
        return;
      } else if (array.length == 0) {
        jQuery('#smsbox_tips').html(jQuery('#no_sms').html()).show(0,
            function() {
              jQuery(this).triggerHandler('_show');
            });
        $('reply').hide();
        return;
      }

      for ( var i = 0; i < array.length; i++) {
        var sms_id = array[i].sms_id;
        var bFound = false;
        for ( var j = 0; j < newSmsArray.length; j++) {
          if (sms_id == newSmsArray[j].sms_id) {
            bFound = true;
            break;
          }
        }

        if (!bFound)
          newSmsArray[newSmsArray.length] = array[i];
      }

      FormatSms();
    },
    error : function(request, textStatus, errorThrown) {
      jQuery('#smsbox_tips').html(
          '<div class="error">获取短消息数据失败(' + request.status + ')：' + textStatus
              + '</div>').show();
    }
  });
}

function FormatSms() {
  var bGroupByName = jQuery('#group_by_name').attr('class').indexOf('active') >= 0;
  var array = [];
  var count = 0;
  for ( var i = newSmsArray.length - 1; i >= 0; i--) {
    if (newSmsArray[i].receive != '1')
      continue;

    var id = bGroupByName ? newSmsArray[i].from_id : newSmsArray[i].type_id;
    if (typeof (array[id]) != "undefined") {
      array[id].count++;
      continue;
    }

    count++;
    var name = bGroupByName ? newSmsArray[i].from_name
        : newSmsArray[i].type_name;
    var time = newSmsArray[i].send_time.indexOf(' ') > 0 ? newSmsArray[i].send_time
        .substr(0, 5)
        : newSmsArray[i].send_time;
    var unread = array[id] && array[id].unread ? (array[id].unread || newSmsArray[i].unread)
        : newSmsArray[i].unread;
    array[id] = {
      name : name,
      count : 1,
      time : time,
      content : newSmsArray[i].content,
      unread : unread
    };
  }

  if (count == 0) {
    jQuery('#smsbox_tips').html(jQuery('#no_sms').html()).show(0, function() {
      jQuery(this).triggerHandler('_show');
    });
    return;
  }

  var html = '';
 // var smscount = 0;
  for ( var id in array) {
    // 取短信内容的前2行内容显示
  //  alert(smscount)
    var content = array[id].content;
    var pos = content.indexOf('<br />');
    if (pos >= 0) {
      var pos2 = content.indexOf('<br />', pos + 6);
      if (pos2 >= 0)
        content = content.substr(0, pos2);
    }

    html += '<div class="list-block" group_id="' + id + '"'
        + (bGroupByName ? ' user="' + id + '"' : '') + '>';
    html += '   <table class="' + (array[id].unread ? "unread" : "") + '">';
    html += '      <tr><td class="name">' + array[id].name
        + '</td><td class="count">' + array[id].count
        + '</td><td class="time">' + array[id].time + '</td></tr>';
    html += '      <tr><td colspan="3" class="msg">' + content + '</td></tr>';
    html += '   </table>';
    html += '</div>';
    //smscount ++;

  }
  jQuery('#smsbox_list_container').html(html);
  jQuery('#smsbox_list_container').triggerHandler('_change');
}
function CreateMsgBlock(msg) {
  var html = '';
  var url = contextPath + msg["url"];
  if (/:\/\//.test(msg["url"])) {
    url = msg["url"];
  }
  html += '<div class="msg-block ' + msg["class"] + '" sms_id="'
      + msg["sms_id"] + '" user="' + msg["user"] + '">';
  html += '   <div class="head">';
  html += '      <div class="name">' + msg["name"] + '&nbsp;' + msg["time"]
      + '</div>';
  if (msg["url"]) {
    html += '   <div class="operation">';
    html += '      <a href="javascript:;" class="reply" hidefocus="hidefocus">回复</a>';
    html += '      <a href="javascript:;" class="detail" sms_id="'
        + msg["sms_id"] + '" url="' + url
        + '" hidefocus="hidefocus">查看详情</a>';
    html += '   </div>';
  }
  html += '   </div>';
  html += '   <div class="msg">' + msg["content"] + '</div>';
  html += '</div>';
  return html;
}

function RemoveSms(recvIdStr, sendIdStr, del) {
  if (!recvIdStr)
    return;
  var url = '';
  var data = '';
  if(del == 0){
    url = contextPath + '/yh/core/funcs/sms/act/YHSmsAct/resetFlag.act?';
    data = {
        'seqId' : recvIdStr,
        'DEL' : del
      };
  }else{
    url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/delSms.act";
    data = {
        'bodyId' : recvIdStr,
        'deType':1,
        'DEL' : del
      };
  }
  jQuery
      .ajax( {
        type : 'POST',
        url : url,
        data : data,
        dataType : 'text',
        success : function(data) {
          var array = [];
          for ( var i = 0; i < newSmsArray.length; i++) {
            var id = newSmsArray[i].sms_id;
            if (id == recvIdStr || recvIdStr.indexOf(id + ',') == 0
                || recvIdStr.indexOf(',' + id + ',') > 0 || id == sendIdStr
                || sendIdStr.indexOf(id + ',') == 0
                || sendIdStr.indexOf(',' + id + ',') > 0)
              continue;

            array[array.length] = newSmsArray[i];
          }
          newSmsArray = array;

          if (recvIdStr.indexOf(',') > 0) // 多条
          {
            selectedRecvSmsIdStr = selectedSendSmsIdStr = '';
            FormatSms();
          } else // 一条
          {
            jQuery(
                '#smsbox_msg_container > div.msg-block[sms_id="' + recvIdStr + '"]')
                .remove();

            if (selectedRecvSmsIdStr.indexOf(recvIdStr + ',') == 0)
              selectedRecvSmsIdStr = selectedRecvSmsIdStr
                  .substr(recvIdStr.length + 1);
            if (selectedRecvSmsIdStr.indexOf(',' + recvIdStr + ',') > 0)
              selectedRecvSmsIdStr = selectedRecvSmsIdStr.replace(
                  ',' + recvIdStr + ',', '');

            if (jQuery('#smsbox_msg_container > div.msg-block').length == 0)
              FormatSms();
          }
        },
        error : function(request, textStatus, errorThrown) {
          alert('操作失败：' + textStatus);
        }
      });
}

function GetSmsIds() {
  var recvIds = sendIds = '';
  for ( var i = newSmsArray.length - 1; i >= 0; i--) {
    if (newSmsArray[i].sms_id == '' )
      continue;
    

    if (newSmsArray[i].receive == '1')
      recvIds += newSmsArray[i].sms_id + ',';
    else
      sendIds += newSmsArray[i].sms_id + ',';
  }

  return {
    recv : recvIds,
    send : sendIds
  };
}
function openURL(url) {
  var mytop = (screen.availHeight - 500) / 2 - 30;
  var myleft = (screen.availWidth - 780) / 2;
  window
      .open(
          url,
          "查看详情",
          "height=548,width=780,status=0,toolbar=no,menubar=yes,location=no,scrollbars=yes,top="
              + mytop + ",left=" + myleft + ",resizable=yes");
}

function send_sms(seqId, fromId) {
  mytop = (document.body.clientHeight - 265) / 2;
  myleft = (document.body.clientWidth - 430) / 2;
  window
      .open(
          contextPath + "/core/funcs/message/smsback.jsp?fromId="
              + fromId,
          "",
          "height=380,width=750,status=0,toolbar=no,menubar=no,location=no,scrollbars=no,top="
              + mytop + ",left=" + myleft + ",resizable=yes");
}

function close_window() {
  window.clearTimeout(smsbox_close_timer);
  try {
    if (!window.external || typeof (window.external.OA_SMS) == 'undefined'
      || !window.external.OA_SMS("", "", "GET_VERSION")
      || window.external.OA_SMS("", "", "GET_VERSION") < '20110223') {
      window.close();
    } else {
      window.external.OA_SMS("", "", "CLOSE_WINDOW")
    }
  } catch (e) {
    
  } finally {
    //关闭yh短信窗口
    if (top && top.sms && top.sms.hideShortMsrg) {
      top.sms.hideShortMsrg();
    }
  }

}

if (top.YH && top.YH.isTouchDevice) {
  window.showModalDialog = function () {
    top.YH && top.showModalDialog.apply(this, arguments);
  }
  
  window.open = function () {
    top.YH && top.open.apply(this, arguments);
  }
}
