//新事务提醒消息
function ispirit_new_msg_remind(uid)
{
//   alert('用户 '+uid+' 有新消息')
}

//取消新微讯的头像闪烁
function ispirit_cancel_msg_remind(uid)
{
//   alert('用户 '+uid+' 已打开新消息')
}

//最近联系人变化接口
function ispirit_recent_changed()
{
   if(jQuery("#module_recent:visible").length > 0)
      LoadRecent();
}

function ispirit_get_client_type(uid, dept_id)
{
   var uid_index = jQuery.inArray(uid, online_uid_array);
   var client = uid_index >= 0 && uid_index < online_client_array.length ? online_client_array[uid_index] : '';
   if(client == '' && orgTree0.tree)   //非OA精灵缓存
   {
      var node = orgTree0.tree.getNodeByKey('user_' + uid + '_' + dept_id);
      if(node)
      {
         client = node.data.client;
      }
      else
      {
         orgTree0.tree.visit(function(node){
            if(node.data.uid == uid)
            {
               client = node.data.client;
               return false;
            }
         });
      }
   }
   
   return client;
}

function node_click(node)
{
   if(node.isStatusNode() || !node.data.uid || typeof(parent.send_msg) != 'function')
      return ;
   
   if(node.data.op_sms)
      parent.send_msg(node.data.uid, node.data.title);
   else
      window.open(node.data.url)
}

//最近联系人
function get_im_recent_user()
{
   if(typeof(window.external.OA_SMS) != 'undefined')
      return window.external.OA_SMS("", "", "RECENT_USER");
   else
      return '';
}

//获取OA精灵缓存用户信息
function get_im_user(uid)
{
   if(typeof(window.external.OA_SMS) == 'undefined')
      return;
   
   try{
      var xml = window.external.OA_SMS("GET_USER", uid, "ORG");
      var dom = document.createElement(xml);
      return {user_id: dom.getAttribute("h"), user_name: dom.getAttribute("b"), sex: dom.getAttribute("d")};
   }
   catch(ex){
      alert(ex.description);
   }
}

//发送群组消息
function send_im_group_msg(group_id, group_name)
{               
   if(typeof(window.external.OA_SMS) != 'undefined')
      window.external.OA_SMS(group_id, group_name, "SEND_IM_GROUP_MSG");
}

//父页面点击“组织”面板的回调事件
function onshow()
{
   if(jQuery("#sub_module_org_0:visible").length > 0)
      jQuery("#sub_module_org_0").triggerHandler("_show");
}

function tree_render(node, nodeSpan)
{
   if(!node.data.isFolder)
      jQuery(".dynatree-connector", nodeSpan).hide();
}

var count = 0;
function get_online_user(paras)
{
   online_uid_str = paras[0];
   online_client_str = paras[1];
   on_status_str = paras[2];
   online_uid_array = online_uid_str.substr(0,online_uid_str.length-1).split(",");
   online_client_array = online_client_str.substr(0,online_client_str.length-1).split(",");
   on_status_array = on_status_str.substr(0,on_status_str.length-1).split(",");
   
   if(parent.menu_id == 2 && jQuery("#sub_module_org_0:visible").length > 0)
   {
      var now = (new Date()).getTime();
      //如果非缓存模式，status_bar回调时不再reload
      if(useIspiritCache && useIspiritCache0 || Math.floor((now - online_last_ref)/1000) >= online_ref_sec/2)
         tree0_reload();
   }
   
   tree1_update();
   group_recent_update();
}

function tree0_reload()
{
   orgTree0.reload();
   online_last_ref = new Date();
}

function tree1_reload()
{
   orgTree1.reload();
}

//根据在线人员的数组更新全部人员中的人员状态
function tree1_update()
{
   if(orgTree1 && orgTree1.tree)
   {
      orgTree1.tree.visit(function(node){
         if(node.data.uid)
         {
            var uid_index = jQuery.inArray(node.data.uid, online_uid_array);
            if(uid_index >= 0)
            {
               var status = on_status_array[uid_index] ? on_status_array[uid_index] : 1;
               var client = online_client_array[uid_index] ? online_client_array[uid_index] : 1;
               node.data.icon = node.data.icon.substr(0, 2) + status + ".png";
               node.data.client = client;
               node.render();
            }
            else if(node.data.icon.substr(2, 1) != "0")
            {
               node.data.icon = node.data.icon.substr(0, 2) + "0.png";
               node.data.client = -1;
               node.render();
            }
         }
      });
   }
}

//根据在线人员的数组更新分组和最近联系人中的人员状态
function group_recent_update()
{
   jQuery("tr.sub-module-item[type='user'] td").each(function(){
      var uid = jQuery(this).parent().attr('uid');
      var className = jQuery(this).attr('class').substr(0, 2);
      
      var uid_index = jQuery.inArray(uid, online_uid_array);
      if(uid_index >= 0)
      {
         var status = on_status_array[uid_index] ? on_status_array[uid_index] : 1;
         this.className = className + status;
      }
      else
      {
         this.className = className + '0';
      }
   });
}
/*
jQuery.noConflict();
(function($){
   
   $(document).ready(function(){
      if(ispirit != "")
         $("#bottom").height(0);
      $(window).trigger('resize');
      
      //手动刷新
      $("#org_refresh").click(function(){
         var block = $('div.module-block:visible');
         if(block.length <= 0)
            return;
         
         var id = block.attr('id');
         if(id == "sub_module_org_0")
         {
            if(orgTree0 && orgTree0.tree)
               tree0_reload();
         }
         else if(id == "sub_module_org_1")
         {
            if(orgTree1 && orgTree1.tree)
               tree1_reload();
         }
         else if(id == "module_recent")
         {
            LoadRecent();
         }
         else
         {
            var module = id.substr(7);
            var options = {MODULE: module};
            if(module == 'user_group')
            {
               var sub_block = $("div.sub-module:visible", block);
               if(sub_block.length == 1)
                  options.INDEX = sub_block.attr("index");
            }
            
            $.get("module.php", options, function(data){
               $("#module_" + module).html(data);
               $("#body").scrollTop(0);
            });
         }
      });
      
      //人员、分组、最近、群 的点击事件
      $("#sub_tabs_ul > li > a").click(function(){
         if($(this).attr('className').indexOf('active') >= 0)
            return;
         
         $("#sub_tabs_ul > li > a").removeClass('active');
         $(this).addClass('active');
         
         var module = $(this).attr('module');
         var options = {MODULE: module};
         $("#modules > div.container").hide();
         $("#module_" + module).show();
         
         var index = $(this).attr('index');
         $.cookie('ONLINE_UI', index, {expires:1000});
         if(module == "org")
         {
            if($("#sub_menu_org > a.active[index='0']").length > 0)
            {
               $("#sub_menu_org > a[index='0']").triggerHandler('click');
            }
            else
            {
               $("#sub_menu_org > a[index='1']").triggerHandler('click');
            }
         }
         else if(module == "user_group")
         {
            if($("#sub_menu_user_group > a.active[index='0']").length > 0)
            {
               options.INDEX = "0";
               $("#sub_menu_user_group > a[index='0']").triggerHandler('click');
            }
            else
            {
               options.INDEX = "1";
               $("#sub_menu_user_group > a[index='1']").triggerHandler('click');
            }
         }
         else if(module == 'recent')
         {
            LoadRecent();
            return;
         }
         
         if($("#module_" + module).text() != "")
            return;
         
         $.get("module.php", options, function(data){
            $("#module_" + module).html(data);
         });
      });
      
      //人员 的下拉事件
      var menuTimerOrg = null;
      $("#sub_tabs_ul > li > a[module='org'] > span > span.dropdown").click(
         function(){
            $("#sub_menu_org").show(100);
            
            var offset = $(this).offset();
            $("#sub_menu_org").css({top:offset.top + $(this).height() - 1, left:offset.left});
         }
      );
      $("#sub_tabs_ul > li > a[module='org'] > span > span.dropdown").hover(
         function(){
         },
         function(){
            menuTimerOrg = window.setTimeout(function(){$("#sub_menu_org").hide(100);}, 300);
         }
      );
      
      //人员子菜单 的鼠标滑过事件
      $("#sub_menu_org").hover(
         function(){
            if(menuTimerOrg)
               window.clearTimeout(menuTimerOrg);
         },
         function(){
            $(this).hide(100);
         }
      );
      
      //人员子菜单 的点击事件
      $("#sub_menu_org > a").click(function(){
         var index = $(this).attr('index');
         if($('#sub_module_org_' + index).text() != "" && $('#sub_module_org_' + index + ':visible').length > 0)
         {
            $("#sub_menu_org").hide();
            return;
         }
         
         if(index == 0)
         {
            $('#label_org').html(td_lang.inc.msg_128);
            $('#sub_module_org_1').hide();
            $('#sub_module_org_0').show();
            $.cookie('ONLINE_UI_1', '0', {expires:1000});
            if(orgTree0 == null)
            {
               orgTree0 = new Tree('sub_module_org_0', jsonURL0, '/images/org/', false, 3, {"persist":false, "minExpandLevel":2, "LazyReadAPI":LoadNodes, "paras":{"node":jsonURL0, "flag": "0"}});
               orgTree0.BuildTree();
               
               online_last_ref = new Date();
               $("#sub_module_org_0").triggerHandler("_show");
            }
            else if(orgTree0.tree)
            {
               $("#sub_module_org_0").triggerHandler("_show");
            }
         }
         else
         {
            $('#label_org').html(td_lang.inc.msg_129);
            $('#sub_module_org_0').hide();
            $('#sub_module_org_1').show();
            $.cookie('ONLINE_UI_1', '1', {expires:1000});
            if(orgTree1 == null)
            {
               orgTree1 = new Tree('sub_module_org_1', jsonURL1, '/images/org/', false, 3, {"minExpandLevel":2, "LazyReadAPI":LoadNodes, "paras":{"node":jsonURL1, "flag": "1"}});
               orgTree1.BuildTree();
            }
         }
         
         $(this).siblings().removeClass('active');
         $(this).addClass('active');
         $("#sub_menu_org").hide();
      });
      
      //在线人员模块显示事件
      $("#sub_module_org_0").bind("_show", function(){
         var now = (new Date()).getTime();
         if(orgTree0 && orgTree0.tree && Math.floor((now - online_last_ref)/1000) >= online_ref_sec)
         {
            tree0_reload();
         }
         /*
         if(online_ref_interval)
            window.clearInterval(online_ref_interval);
            
         //在线人员自动刷新
         online_ref_interval = window.setInterval(function(){
            if(parent.menu_id == 2 && orgTree0 && orgTree0.tree && $('#sub_module_org_0:visible').length > 0)
            {
               tree0_reload();
               online_last_ref = new Date();
            }
         }, online_ref_sec*1000);
         
         //全部人员自动刷新
         all_ref_interval = window.setInterval(function(){
            if(parent.menu_id == 2 && orgTree1 && orgTree1.tree && $('#sub_module_org_1:visible').length > 0)
            {
               orgTree1.reload();
            }
         }, online_ref_sec*1000*5);
         
      });
      
      //分组 的下拉事件
      var menuTimerUserGroup = null;
      $("#sub_tabs_ul > li > a[module='user_group'] > span > span.dropdown").click(
         function(){
            $("#sub_menu_user_group").show(100);
            
            var offset = $(this).offset();
            $("#sub_menu_user_group").css({top:offset.top + $(this).height() - 1, left:offset.left - 35});
         }
      );
      $("#sub_tabs_ul > li > a[module='user_group'] > span > span.dropdown").hover(
         function(){
         },
         function(){
            menuTimerUserGroup = window.setTimeout(function(){$("#sub_menu_user_group").hide(100);}, 300);
         }
      );
      
      //分组子菜单 的鼠标滑过事件
      $("#sub_menu_user_group").hover(
         function(){
            if(menuTimerUserGroup)
               window.clearTimeout(menuTimerUserGroup);
         },
         function(){
            $(this).hide(100);
         }
      );
      
      //分组子菜单 的点击事件
      $("#sub_menu_user_group > a").click(function(){
         var index = $(this).attr('index');
         if(index == "0")
         {
            $("#sub_module_user_group_1").hide();
            $("#sub_module_user_group_0").show();
            $.cookie('ONLINE_UI_2', '0', {expires:1000});
         }
         else
         {
            $("#sub_module_user_group_0").hide();
            $("#sub_module_user_group_1").show();
            $.cookie('ONLINE_UI_2', '1', {expires:1000});
         }
         
         $(this).siblings().removeClass('active');
         $(this).addClass('active');
         $("#sub_menu_user_group").hide();
      });
      
      //分组表头双击向该分组所有人群发
      $("#modules tr.sub-module-head").live('click', function(){
         var span = $(".group-name", this);
         if(span.length <= 0)
            return;
         
         span.toggleClass("group-name-collapsed");
         
         var tbody = $("#" + this.id + "_tbody");
         if(tbody)
            tbody.toggle();
      });
      
      //分组群发事件
      var groupOpMenuTimer = null;
      $("#module_user_group_op_menu").hover(
         function(){
            if(groupOpMenuTimer)
            {
               window.clearTimeout(groupOpMenuTimer);
               groupOpMenuTimer = null;
            }
         },
         function(){
            $("#module_user_group_op_menu").hide();
         }
      );
      
      
      $("#module_user_group_op_menu a").click(function(){
         var uid_str = $(this).parent().attr('uid_str');
         var user_name_str = $(this).parent().attr('user_name_str');
         if(!uid_str || !user_name_str)
            return;
         
         var op = $(this).attr('op');
         if(bSmsPriv && op == "msg")
            parent.group_send_sms(uid_str, user_name_str);
         else if(bEmailPriv && op == "email")
            parent.send_email1(uid_str, user_name_str);
         else
            alert(td_lang.inc.msg_130);
      });
      
      $("#modules tr.sub-module-head .group-op a").live("mouseleave", function(){
            groupOpMenuTimer = window.setTimeout(function(){$("#module_user_group_op_menu").hide();}, 300);
         }
      );
      
      $("#modules tr.sub-module-head .group-op a").live('click', function(){
         $("#module_user_group_op_menu").show();
         var offset = $(this).offset();
         $("#module_user_group_op_menu").css({ top: offset.top+($.browser.msie ? -10 : 15), left: offset.left-50 });
         $("#module_user_group_op_menu").attr({ uid_str: $(this).attr('uid_str'), user_name_str: $(this).attr('user_name_str') });
         return false;
      });
      
      //子模块项目的鼠标滑过、单击、双击事件
      $("#modules tr.sub-module-item").live('mouseenter', function(){
         $(this).addClass('TableRowHover');
      });
      $("#modules tr.sub-module-item").live('mouseleave', function(){
         $(this).removeClass('TableRowHover');
      });
      $("#modules tr.sub-module-item").live('click', function(){
         $(this).siblings("tr.sub-module-item").removeClass('TableRowActive');
         $(this).addClass('TableRowActive');
         
         var type = $(this).attr('type');
         if(type == 'user')
         {
            var uid = $(this).attr('uid');
            var user_name = $(this).attr('user_name');
            
            parent.send_msg(uid, user_name);
         }
         else if(type == 'group')
         {
            var group_id = $(this).attr('group_id');
            var group_name = $(this).attr('group_name');
            
            send_im_group_msg(group_id, group_name);
         }
      });

      $("#sub_module_org_0 li[dtnode]").live('mouseenter', function(){
         var node = $(this).attr("dtnode");
         if(node.data.op_sms || typeof(node.data.client) != 'undefined' && node.data.client != '-1' && node.data.client != '2')
         {
            if($("#" + node.data.key + '_op').length <= 0)
            {
               var html = '<span id="' + node.data.key + '_op" op="' + node.data.op_sms + '" style="display:none;">';
               if(node.data.op_sms)
               {
                  html += '&nbsp;<img src="/images/msg.png" title="'+td_lang.inc.msg_135+'" onclick="parent.send_sms(\''+node.data.uid+'\',\''+ node.data.title +'\')" style="cursor:pointer;" />';
                  html += ' <img src="/images/email.png" title="'+td_lang.inc.msg_134+'" onclick="parent.send_email1(\''+node.data.uid+'\',\''+ node.data.title +'\')" style="cursor:pointer;" />';
               }
               
               if(typeof(node.data.client) != 'undefined' && node.data.client != '-1' && node.data.client != '2')
               {
                  html += ' <img src="/images/client_type_' + node.data.client + '.png" title="' + sprintf(td_lang.inc.msg_131, get_client_type(node.data.client)) + '">';
               }
               
               html += '</span>';
               
               $(this).append(html);
               $("span[op]", this).bind('click', function(event){
                  event.stopPropagation();
               });
            }
            $("#" + node.data.key + '_op').show();
         }
      });
      
      $("#sub_module_org_0 li[dtnode]").live('mouseleave', function(){
         var node = $(this).attr("dtnode");
            $("#" + node.data.key + '_op').hide();
      });
      
      $("#sub_module_org_0 li[dtnode] span[op]").live('mouseleave', function(){
         $(this).hide();
      });
      
      //第一次进入页面，根据online_ui确定加载的面板
      if(online_ui == "1" || online_ui == "")
         $("#sub_tabs_ul > li > a[index='1']").triggerHandler('click');
      else if(online_ui == "2")
         $("#sub_tabs_ul > li > a[index='2']").triggerHandler('click');
      else if(online_ui == "3")
         $("#sub_tabs_ul > li > a[index='3']").triggerHandler('click');
      else if(online_ui == "4")
         $("#sub_tabs_ul > li > a[index='4']").triggerHandler('click');
   });
   
   $(window).resize(function(){
      if(!parent.document.getElementById('frame1')) return;
      $("#bottom_center").css({width: "0px"});
      
      if($(document).height() > $("#sub_tabs").height() + $("#bottom").height())
         $("#body").height($(document).height() - $("#sub_tabs").height() - $("#bottom").height());
   
      var widthTotal = parseInt($("#bottom").width());
      var widthLeft = parseInt($("#bottom_left").width());
      var widthRight = parseInt($("#bottom_right").width());
      if(!isNaN(widthTotal) && !isNaN(widthLeft) && !isNaN(widthRight))
      {
         $("#bottom_center").width(widthTotal - widthLeft - widthRight);
      }
   });
   
})(jQuery);
*/
function LoadNodes(paras)
{
   var node = paras.node;
   var url = "";
   if(typeof(node) == "object" && typeof(node.data) == "object")
      url = node.data.json;
   else if(typeof(node) == "string")
      url = node;
   
   var nodes = null;
   if(paras.flag == "0")
   {
      nodes = useIspiritCache && useIspiritCache0 ? LoadNodesFromIspirit((typeof(node) == "object" ? node : null), paras.flag, true) : (url == "" ? null : LoadNodesFromURL(url, paras.flag));
   }
   else
   {
      nodes = useIspiritCache && useIspiritCache1 ? LoadNodesFromIspirit((typeof(node) == "object" ? node : null), paras.flag, false) : (url == "" ? null : LoadNodesFromURL(url, paras.flag));
   }
   
   return nodes;
}

function LoadNodesFromURL(url, para)
{
   var nodes = null;
   jQuery.ajax({
      async:false,
      url: url,
      dataType: 'json',
      success: function(data){
         nodes = data;
      },
      error: function (request, textStatus, errorThrown){}
   });
   
   return nodes;
}

function LoadNodesFromIspirit(node, flag, recursive)
{
//   if(node == null)
//      get_online_uid_str();
   
   var id = (node && node.data && node.data.key) ? node.data.key.substr(5) : "0";
   var xmlString = '';
   if(flag == "0")
      xmlString = window.external.OA_SMS("GET_ONLINE_TREE", online_uid_str, "ORG");
   else
      xmlString = window.external.OA_SMS("GET_ORG_TREE", id, "ORG");
   //xmlString = flag == "1" ? TestAPI0(id) : TestAPI2();
   var xml = new XML();
   xml.loadFromString(xmlString);
   var org = xml.getRoot();
   
   recursive = typeof(recursive) == "undefined" ? (jQuery('#sub_module_org_0:visible').length > 0 ? true : false) : recursive;
   var nodes = [];
   if(id == "0")
   {
      var title = org.getAttribute("b");
      nodes = [{
         "title": title,
         "isFolder": true,
         "isLazy": false,
         "expand": true,
         "key": "dept_0",
         "dept_id": "0",
         "icon": "root.png",
         "tooltip": title,
         "children": xmlDom2TreeNodes(org.childNodes, recursive)
      }];
   }
   else
   {
      nodes = xmlDom2TreeNodes(org.childNodes, recursive);
   }
   
   return nodes;
}

function xmlDom2TreeNodes(dom, recursive)
{
   if(!dom.length)
      return null;
   
   var nodes = [];
   for(var i=0; i<dom.length; i++)
   {
      var node = dom[i];
      var name = node.nodeName;
      var id = node.getAttribute("a");
      var title = node.getAttribute("b");
      
      if(name == "d")
      {
         var is_org = node.getAttribute("c") == "1";
         nodes[nodes.length] = {
            "title": title,
            "isFolder": true,
            "isLazy": recursive ? false : true,
            "expand": (jQuery('#sub_module_org_0:visible').length > 0 ? true : false),
            "key": "dept_" + id,
            "dept_id": id,
            "icon": is_org ? "org.png" : false,
            "tooltip": title,
            "url": "",
            "target": para_target,
            "children": (recursive ? xmlDom2TreeNodes(node.childNodes, recursive) : null)
         };
      }
      else
      {
         var user_id = node.getAttribute("h");
         var dept_id = node.getAttribute("i");
         var sex = node.getAttribute("d");
         var url = para_url2 + "?UID=" + id + "&USER_ID=" + user_id + "&DEPT_ID=" + dept_id;
         
         var uid_index = jQuery.inArray(id, online_uid_array);
         var client = uid_index >= 0 ? online_client_array[uid_index] : -1;
         nodes[nodes.length] = {
            "title": title,
            "isFolder": false,
            "isLazy": false,
            "key": "user_" + id + "_" + dept_id,
            "uid": id,
            "user_id": user_id,
            "icon": "U" + sex + (uid_index >= 0 ? on_status_array[uid_index] : "0") + ".png",    //******
//            "tooltip": title,
            "op_sms": (op_sms && !find_id(exclude_uid_str, id)),
            "client": client,
            "url": url,
            "target": para_target
         };
      }
   }
   
   return nodes;
}

function LoadRecent(flag)
{
   var recent_uid = get_im_recent_user();
   if(!flag && recent_uid == '')
   {
      window.setTimeout("LoadRecent(1)", 1000);
      return;
   }
   
   var recent_array = recent_uid.split(",");
   var html_tr = '';
   
   for(var i=0; i < recent_array.length; i++)
   {
      var uid = recent_array[i];
      if(uid == "")
         continue;
      
      var uid_index = jQuery.inArray(uid, online_uid_array);
      var on_status = uid_index >= 0 ? on_status_array[uid_index] : "0";
      
      var user = get_im_user(uid);
      html_tr = '<tr class="TableData sub-module-item" type="user" uid="' + uid + '" user_id="' + user.user_id + '" user_name="' + user.user_name + '"><td class="U' + user.sex + on_status + '">' + user.user_name + '</td></tr>' + html_tr;
   }
   
   var html = '<table class="TableList" width="97%" align="center">';
   if(html_tr == "")
      html += '<tr class="TableData"><td align="center"><br>' + td_lang.inc.msg_136 + '<br><br></td></tr>';
   else
      html += html_tr;
   html += '</table>';
   
   jQuery("#module_recent").html(html);
   jQuery("#body").scrollTop(0);
}
