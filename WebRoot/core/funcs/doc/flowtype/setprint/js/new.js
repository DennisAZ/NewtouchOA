function dealDis(selected,disselected){
	
   for(var i=0;i<selected.length;i++){
     for(var j=0;j<disselected.length;j++){
    	  // alert(selected[i].value+"/"+disselected[j].value);
		   if(selected[i].value==disselected[j].value){
			   disselected.splice(j,1);
			   continue;
			   }
       }     
	  }
  
    return disselected;
	 }
	function getDisSelected(){
		 var rtJson = getJsonRs( contextPath + moduleSrcPath +"/act/YHFlowPrintAct/getDisSelectByFlowIdAct.act", "flow_id="+flow_id);
		  if (rtJson.rtState == "0") {
		    var data=rtJson.rtData;
		    
		    return data
		  }else {
		    alert(rtJson.rtMsrg);
		    return false;
		  }
		}
	function getSelected(){
	     var rtJson = getJsonRs( contextPath+ moduleSrcPath +"/act/YHFlowPrintAct/getSelectByFlowIdAct.act", "seq_id="+seq_id);
	      if (rtJson.rtState == "0") {
	        var data=rtJson.rtData;
	        
	        return data
	      }else {
	        alert(rtJson.rtMsrg);
	        return false;
	      }
	    }

	 function setSelectValue(value){
      var ids="";
      for(var i=0;i<value.length;i++){
          var data=value[i];
          ids+=data.value;
          ids+=",";  
          }
     $("selectValue").value=ids;
		
		 }

	  
	function saveFile()
	{
	    var obj = $("HWPostil1");
	    var tName=$("tName").value;
	  if(tName=="")
	  {
	    alert("请输入模板名称！");
	    return;
	  }
	    var tType=$("tType").value;
	    var prcsStr=$("selectValue").value;
	    var content = obj.GetCurrFileBase64();

	    var param="prcsStr="+prcsStr+"&seqId="+seq_id+"&tName="+ $("tName").value+"&flowId="+flow_id+"&tType="+$("tType").value+"&content="+content.replace(/\+/g, '%2B');

	     var url =  contextPath + moduleSrcPath +"/act/YHFlowPrintAct/updateAipAct.act";
	     var rtJson = getJsonRs(url,param);
	       if (rtJson.rtState == "0") {  
	       window.location.reload();
	    }else {
	      alert(rtJson.rtMsrg);
	    }
	}
	
/**
 * 加载AIP数据
 */

 
function loadAIP() {
  var rtJson = getJsonRs( contextPath + moduleSrcPath +"/act/YHFlowPrintAct/loadAip.act", "seq_id="+seq_id);
  if (rtJson.rtState == "0") {
	  var data=rtJson.rtData;
	   bindJson2Cntrl(data);
    return data.content;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
}

function showOrHide(id)
{
  if($(id).style.display=='none')
    $(id).style.display='';
  else
    $(id).style.display='none';
}

function addField()
{
  var obj = $("HWPostil1");
    var vRet = obj.Login("sys_admin", 5, 32767, "", "");
    if(0 == vRet);
      obj.InDesignMode = true;
}

function NotifyLineAction(lPage,lStartPos,lEndPos)
{  
  var lStartY = (lStartPos>>16)& 0x0000ffff;
  var lStartX = ((lStartPos<<16)>>16) & 0x0000ffff; 

  var lEndY = (lEndPos>>16)& 0x0000ffff;
  var lEndX = ((lEndPos<<16)>>16) & 0x0000ffff; 
 
  ShowDialog('setFrm',30);
    argObj = {"page":lPage,"StartX":lStartX,"StartY":lStartY,"EndX":lEndX,"EndY":lEndY};
}


function ShowDialog(id,vTopOffset)
{
   if(typeof arguments[1] == "undefined")
     vTopOffset = 90;
     
   var bb=(document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
   $("overlay").style.width = Math.max(parseInt(bb.scrollWidth),parseInt(bb.offsetWidth))+"px";
   $("overlay").style.height = Math.max(parseInt(bb.scrollHeight),parseInt(bb.offsetHeight))+"px";

   $("overlay").style.display = 'block';
   $(id).style.display = 'block';

   $(id).style.left = ((bb.offsetWidth - $(id).offsetWidth)/2)+"px";
   $(id).style.top  = (vTopOffset + bb.scrollTop)+"px";
}
function HideDialog(id)
{
   $("overlay").style.display = 'none';
   $(id).style.display = 'none';
}


function setField(field_name,field_type,font_family,font_size,font_color,border_style,halign,valign)
{
  HideDialog('setFrm');
  if(argObj == null)
      return;
  var obj = $("HWPostil1");
  var field_width = argObj.EndX - argObj.StartX;
  var field_height = argObj.EndY - argObj.StartY; 
  var vRet = obj.InsertNote(field_name,argObj.page,field_type,argObj.StartX,argObj.StartY,field_width,field_height);
  if(vRet=="")
    {
        alert("此字段映射已经添加！");
        ShowDialog('setFrm');
        return;
    }
    font_size = font_size.replace(/pt/ig,"");
    font_color = font_color.replace(/#/ig,"0x00");
    var fontColor = 0;
    fontColor = parseInt(font_color);
    obj.SetValue(field_name,":PROP::LABEL:3");
    obj.SetValue(field_name,":PROP:BORDER:"+border_style);
    obj.SetValue(field_name,":PROP:FACENAME:"+font_family);
    obj.SetValue(field_name,":PROP:FONTSIZE:"+font_size);
    obj.SetValue(field_name,":PROP:FRONTCOLOR:" + fontColor);
    obj.SetValue(field_name,":PROP:HALIGN:"+halign);
    obj.SetValue(field_name,":PROP:VALIGN:"+valign);
  
  $("FIELD_STR").value += field_name+",";
  argObj = null;
}