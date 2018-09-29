//得到控件对象
var FCKObject = plugins({comName:"NCalendar"
  ,item:"NCalendar"
  ,page:"ncalendar.jsp"
  ,inco:"calendar.gif"
  ,width:340
  ,height:200
  ,dlgTitle:"日历控件属性"
  ,btnTitle:"日历控件"
});
//为控件对象注测行为

//注册add 方法
function addCalendar(code){
  FCK.InsertHtml(code);
 // alert(code);
}
FCKObject.RegisterAction('Add',addCalendar);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function( contextMenu, tag, tagName ){
  if(tagName == 'IMG' && tag.className == "DATE"){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NCalendar','日历控件属性');
  }
}
FCK.ContextMenu.RegisterListener( DSMenuListener ) ;