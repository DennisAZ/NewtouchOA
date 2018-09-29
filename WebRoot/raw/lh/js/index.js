var oaMenu = {
    rtState:0,
    rtMsrg:'成功返回',
    rtData:[
            {menuName:'组织机构',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'单位管理',address:'/general/system/unit/'}
               ,{name:'部门管理',address:'/general/system/dept/'}
               ,{name:'用户管理',address:'/general/system/user/'}
               ,{name:'权限管理',address:'/general/system/user_priv/'}
               ]
            }
            ,{menuName:'短信',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'内部短信',address:'/general/sms/'}
               ,{name:'手机短信',address:'/general/mobile_sms/'}
               ,{name:'短信提醒设置',address:'/general/system/remind/'}
               ,{name:'手机短信设置',address:'/general/system/mobile_sms/'}
               ]
            }
            ,{menuName:'网络硬盘',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'网络硬盘',address:'/general/netdisk/'}
               ,{name:'网络硬盘设置',address:'/general/system/netdisk/'}
               ]
            }
            ,{menuName:'文件柜',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'个人文件柜',address:'/general/file_folder/index2.php'}
               ,{name:'公共文件柜',address:'/general/file_folder/index1.php'}
               ]
            }
            ,{menuName:'新闻',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'新闻',address:'/general/news/show/'}
               ,{name:'新闻管理',address:'/general/news/manage/'}
               ]
            }
            ,{menuName:'工作日志',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'工作日志',address:'/general/diary/'}
               ,{name:'工作日志查询',address:'/general/diary/info/'}
               ]
            }
            ,{menuName:'日程安排',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'日程安排查询',address:'/general/calendar/info/'}
               ,{name:'日程安排',address:'/general/calendar/'}
               ]
            }
            ,{menuName:'个人考勤',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'个人考勤',address:'/general/attendance/personal/'}
               ,{name:'考勤设置',address:'/general/system/attendance/'}
               ]
            }
            ,{menuName:'工作流',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
               {name:'新建工作',address:'/general/workflow/new/'}
               ,{name:'我的工作',address:'/general/workflow/list/'}
               ,{name:'工作查询',address:'/general/workflow/query/'}
               ,{name:'工作监控',address:'/general/workflow/manage/'}
               ,{name:'工作委托',address:'/general/workflow/rule/'}
               ,{name:'工作销毁',address:'/general/workflow/destroy/'}
               ,{name:'流程日志查询',address:'/general/workflow/logs/'}
              ]
            }
            ,{menuName:'图片',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
                {name:'图片',address:'/general/picture/'}
                ,{name:'图片设置',address:'/general/system/picture/'}
               ]
            }
            ,{menuName:'公告通知',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
                 {name:'公告通知',address:'/general/notify/show/'}
                ,{name:'公告通知管理',address:'/general/notify/manage/'}
                ,{name:'公告通知审批',address:'/general/notify/auditing/'}
                ,{name:'公告通知设置',address:'/general/system/notify/'}
               ]
            }
            ,{menuName:'电子邮件',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
                 {name:'内部邮件',address:'/general/email/'}
                ,{name:'Internet邮件',address:'/general/notify/manage/'}
               ]
            }
            ,{menuName:'通讯簿',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
                 {name:'通讯簿',address:'/general/address/private/'}
                ,{name:'公共通讯簿',address:'/general/address/public/'}
                ,{name:'公共通讯簿设置',address:'/general/system/address/'}
                ]
            }
            ,{menuName:'档案管理',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:true,childMenu:
              [
                 {name:'卷库管理',address:'/general/roll_manage/roll_room/'}
                ,{name:'案卷管理',address:'/general/roll_manage/'}
                ,{name:'文件管理',address:'/general/roll_manage/roll_file/'}
                ,{name:'案卷借阅',address:'/general/roll_manage/roll_lend/'}
                ,{name:'借阅审批',address:'/general/roll_manage/roll_lend/confirm.php'}
                ,{name:'借阅统计',address:'/general/roll_manage/roll_statistic/lend.php'}
                ,{name:'案卷统计',address:'/general/roll_manage/roll_statistic/roll.php'}
                ,{name:'档案销毁',address:'/general/roll_manage/destory/'}
                
                ]
            }
            //,{menuName:'网络传真',menuImage:'/yh/core/styles/imgs/avatar/1.gif',isHaveChild:false,address:'/general/fax/'}
           ]   
}

//http://localhost:8080/general/picture/
function doInit(){
  var navTrees = $('navTrees');
  var ul = document.createElement('ul');
  navTrees.appendChild(ul);
  for(var i = 0 ; i < oaMenu.rtData.length ; i++){
    var menu = oaMenu.rtData[i];
   // "<li style="display:none;"><a href="#">浮动菜单1</a> <img src="./img/navdown.gif" align="absmiddle"/></li>"
    var li = document.createElement('li');
    with(li){
      id = 'oaFunc-' + i;
      style.display = 'none';
    }
    var a = document.createElement("a");
   
    a.innerHTML = menu.menuName;
    a.href = "#";
    var isHaveChild = menu.isHaveChild;
    if(!isHaveChild){
      var address = baseUrl + menu.address;
      $(a).observe('click',open.bindAsEventListener(this,address));
      li.appendChild(a);
      ul.appendChild(li);
    }else{
     //<img src="./img/navdown.gif" align="absmiddle"/> 
      var img = document.createElement("img");
      img.src = "./img/navdown.gif";
      img.align = "absmiddle";
      img.id = "oaFuncImg-" + i ;
      li.appendChild(a);
      li.appendChild(img);
      ul.appendChild(li);
      bindToCntrl("oaFuncImg-" + i , menu.childMenu);
      bindToCntrl("oaFunc-" + i , menu.childMenu);
    }
  }
}

function bindToCntrl(cntrl , childMenu){
  var popMenuManager = new TDJsPopMenuManager();
  var menuItemDef = [];
   for (var j = 0, cnt = childMenu.length; j < cnt; j++) {
    var actionName = childMenu[j].name;
    menuItemDef.add([actionName, function(index){
      $("contentFrame").src = baseUrl + childMenu[index].address;
    }]);
  }
  popMenuManager.addMenu('funcMenu',
    menuItemDef,
    130,
    120);
  popMenuManager.bindMenuToCntrl(cntrl , 'funcMenu', "click");
}
function open(){
  $("contentFrame").src = arguments[1];
}