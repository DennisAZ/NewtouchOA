function loadData () {
	var url = contextPath + "/yh/core/funcs/allreport/act/YHAllReportAct/getMenuList.act";
	//alert(rsText);
    var json = getJsonRs(url);
   // alert(rsText);
    var data = {attachCtrl:'flow_list'
               ,accordionId:'subfield'
               ,data:[]
    };
    if(json.rtState == '0'){
 	  data.data = json.rtData;
    }
   
    var dd = new Accordion(data);// alert(rsText);
}
function getList(tmp) {
	
    parent.mainFrame.location = "new.jsp?rId="+tmp.extData;
}
function actionFuntion(tmp) {
	
	var rId="";
	var mId="";
	rId=tmp.extData.substr(0,tmp.extData.indexOf(":"));
	mId=tmp.extData.substr(tmp.extData.indexOf(":")+1,tmp.length);
    parent.mainFrame.location = "edit.jsp?rId="+rId+"&mId="+mId;
}
function iconActionFuntion(tmp) {
	
}




function jInit(){
    jQuery("#calc_sum").bind("click",function(){
       setCalc(jQuery(this).val())
    });
    
    jQuery("#calc_avg_weight").bind("click",function(){
       setCalc(jQuery(this).val())
    });

    jQuery("#calc_avg").bind("click",function(){
       setCalc(jQuery(this).val())
    });
    
    jQuery("#calc_def").bind("click",function(){
       setCalc(jQuery(this).val())
    });
  //  jQuery("form").bind("submit",function(){
    //  return check_form();
   // });
     
    jQuery("#form2").bind("submit",function(){
        return check_form();
      });
    jQuery("#form1").bind("submit",function(){
        return check_form2();
      });
    
     jQuery('#doAddFieldBtn').click(function() {
         if(true == addItem(false))  
         {
          jQuery.unblockUI();
          clearInput();
         }
     }); 
     
     jQuery('#CancelAddFieldBtn').click(function() {
         jQuery.unblockUI();
         clearInput();
     }); 
     
     jQuery('#doDelFieldBtn').click(function() {
         if(window.confirm("确定删除此项吗？"))
         {
          var vid = jQuery("#field_name").val();
          var vfield = jQuery("#field").val();
          jQuery("#"+vfield.substr(0,1)+"_"+vid).remove();
          jQuery.unblockUI();
          clearInput();
         }
     });
     
     jQuery('#addQueryBtn').click(function() {
    	 
         addOption2(mId);
    	 
    	 jQuery("#field").val("query_list"); 
    	 jQuery("#tcalc").hide();
    	 jQuery.blockUI({ message: jQuery('#field_div'), css: { width: '400px', cursor:'default',left:'150px'} }); 
    });   
     
     
     jQuery('#doUpdateFieldBtn').click(function() {
       updateItem();
       jQuery.unblockUI();
       clearInput();
     }); 
     
     jQuery(".sortable").sortable({placeholder: 'ui-state-highlight'});
     jQuery(".sortable").disableSelection();
	}



   
	function addOption(seqId){
		  var url = contextPath + "/yh/core/funcs/allreport/act/YHAllReportAct/getListItemAct.act?rId="+seqId;
	    var json = getJsonRs(url);
	    if(json.rtState == '0'){
	       var data=json.rtData.data;
	       for(var i=0;i<data.length;i++){
	          jQuery("#group_field").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
	          jQuery("#group_name").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
	          jQuery("#calc_weight").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
	       }
	    }
	    else{
	    	alert(json.rtMsrg);
	    }
	    
	    
	    var url1 = contextPath + "/yh/core/funcs/allreport/act/YHAllReportAct/getCalListItemAct.act?rId="+seqId;

	    var json1 = getJsonRs(url1);
	   // alert(rsText);
      if(json1.rtState == '0'){
         var data=json1.rtData.data;
         for(var i=0;i<data.length;i++){
          //  jQuery("#group_field").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
            jQuery("#field_name").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
            //jQuery("#calc_weight").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
          
         }
      }
      else{
        alert(json.rtMsrg);
      }
	    
	    
	    
	}
	
	
  //按分组列出详情
  function addOption2(seqId){  
      var url = contextPath + "/yh/core/funcs/allreport/act/YHAllReportAct/getListItemAct.act?rId="+seqId;
      var json = getJsonRs(url);
      if(json.rtState == '0'){
         var data=json.rtData.data;
         //alert(rsText);
       jQuery("#field_name").empty();
       jQuery("#calc_weight").empty();
         for(var i=0;i<data.length;i++){
            //jQuery("#group_field").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");  .....................
            
            jQuery("#field_name").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");

          //  jQuery("#calc_weight").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
         }
      }
      else{
        alert(json.rtMsrg);
      }
 
  }
	
  //按分组统计计算
  function addOption1(seqId){ 
    var url = contextPath + "/yh/core/funcs/allreport/act/YHAllReportAct/getCalListItemAct.act?rId="+seqId;
    var json = getJsonRs(url);
    if(json.rtState == '0'){
       var data=json.rtData.data;
       
       jQuery("#field_name").empty();
   
       jQuery("#field_name").append("<option value=''></option>");
       for(var i=0;i<data.length;i++){
       //   jQuery("#group_field").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
         jQuery("#field_name").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
       //   jQuery("#calc_weight").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
       }
    }
    else{
      alert(json.rtMsrg);
    }

}


   function clo(){
       jQuery('#CancelAddFieldBtn').click(function() {
           jQuery.unblockUI();
           clearInput();
       }); 
	   }

   function addFieldBtn() 
   {  
       if(jQuery("input[name='group_type']:checked").length <= 0)
       {
          alert("请先选择统计方式！");
          return;
       }
       else
       {
          clearInput();
          if(jQuery("input[name='group_type']:checked").val() == "1")
          {
            addOption2(mId);
          }else{
        	  addOption1(mId);
          }
       }
       jQuery("#field").val("field_list");
       jQuery("#tcalc").show();
      
       jQuery.blockUI({ message: jQuery('#field_div'), css: { width: '400px', cursor:'default',left:'150px'} }); 
   }

   function clearInput()
   {
	   jQuery("input[name='calc_type']").each(function(){this.disabled = false;this.checked = false;})
     jQuery("#disp_name").val("");
	   jQuery("#field_name").val("");
	   jQuery("#formula_div").hide();
	   jQuery("#weight_div").hide();
	   jQuery("#doDelFieldBtn").hide();
	   jQuery("#doUpdateFieldBtn").hide();
	   jQuery("#doAddFieldBtn").show();
   }

   function setCalc(val)
   {
      if(val=="1")
      {
         jQuery("#weight_div").show();
         jQuery("#formula_div").hide();
      }
      else if(val=="3")
      {
         jQuery("#formula_div").show();
         jQuery("#weight_div").hide();
      }
      else
      {
         jQuery("#formula_div").hide();
         jQuery("#weight_div").hide();
      } 
   }

   function addItem(data)
   { 
      var obj = jQuery("<li></li>");
      var vfield = jQuery("#field").val();
      if(!data)
      {
         
         
         if(jQuery("#field_name").val()=="" && jQuery("input[name='calc_type']:checked").val()!=3)
         {
            alert("请选择表单字段");
            return false;
         }
         else if(jQuery("#disp_name").val()=="")
         {
            alert("显示名称不能为空");
            return false;       
         }

         if(jQuery("input[name='group_type']:checked").val() == "0" && jQuery("#field").val() == "field_list")
         {
            if(jQuery("input[name='calc_type']:checked").length <= 0 && jQuery("#calc_formula").val()==""){
               alert("请选择计算方式");
               return false;
            }
         }
         
         var vid;
         if(jQuery("#field_name").val()=="")
            vid = vfield.substr(0,1)+"_"+jQuery("#disp_name").val();
         else
            vid = vfield.substr(0,1)+"_"+jQuery("#field_name").val();
         var vname = jQuery("#disp_name").val();//alert(vname);
         var vtext = jQuery("#field_name").find("option:selected").text();//alert(vtext);
         if(vname=="")
            vname = vtext
         var vcalc_type = vformulate = vweight = "";
         if(jQuery("input[name='group_type']:checked").length > 0)
         {
            if(jQuery("input[name='calc_type']:checked").length > 0)
            {
               vcalc_type = jQuery("input[name='calc_type']:checked").val();
               if(vcalc_type=="1")
                  vweight = jQuery("#calc_weight").val();
               if(vcalc_type == "3")
                  vformulate = jQuery("#calc_formula").val();
            }
         }
       }
      else
      {
         var vid = vfield.substr(0,1)+"_"+data.field;
         var vname = data.disp_name;//alert(vname);
         var vcalc_type = data.calc_type;//alert(vcalc);
         var vformulate = data.formulate;//alert(vformulate);
         var vweight = data.weight;
         var vtext = data.disp_name;//alert(vtext);
      }
     
     obj.attr("class","ui-state-default")
      .attr("id",vid)
      .attr("disp_name",vname)
      .html(vname)
      .dblclick(function(){modifyItem(obj)});
      if(vfield == "field_list")
         obj.attr("calc_type",vcalc_type).attr("formulate",vformulate).attr("weight",vweight);
     
     obj.appendTo("#"+vfield);  
     return true;
   }


   function modifyItem(obj)
   {   
      jQuery.blockUI({ message: jQuery('#field_div'), css: { width: '400px', cursor:'default',left:'150px'} });
      jQuery("#doDelFieldBtn").show();
      jQuery("#doUpdateFieldBtn").show();
      jQuery("#doAddFieldBtn").hide();
      jQuery("#field").val(obj.parent().attr("id"));
   
      if(jQuery("input[name='group_type']:checked").val() == "1")
      {       
         jQuery("#calc_sum").attr("disabled",true);
         jQuery("#calc_avg_weight").attr("disabled",true);
         jQuery("#calc_avg").attr("disabled",true);
      }else if(jQuery("#field").val() != "query_list"){
    	 addOption1(mId);
      }

      if(jQuery("#field").val() == "query_list"){
         //jQuery("#tcalc").hide();
         addOption2(mId);
      }
      jQuery("#field_name").val(obj.attr("id").substr(2));
      jQuery("#item_old").val(obj.attr("id"));
      jQuery("#disp_name").val(obj.attr("disp_name"));
      //jQuery("input[name='calc_type']").get(obj.attr("calc_type")).checked = true;
      if(obj.attr("calc_type") == "0")
      {
         jQuery("#calc_sum").attr("checked",true);
      }
      else if(obj.attr("calc_type") == "1")
      {
         jQuery("#calc_avg_weight").attr("checked",true);
         jQuery("#weight_div").show();
      }
      else if(obj.attr("calc_type") == "2")
      {
         jQuery("#calc_avg").attr("checked",true);   
      }
      else if(obj.attr("calc_type") == "3") 
      {
         jQuery("#formula_div").show();
         jQuery("#calc_def").attr("checked",true);
      }
      
      if(jQuery("#field").val() == "field_list")
      {
         jQuery("#calc_formula").val(obj.attr("formulate"));
         jQuery("#calc_weight").val(obj.attr("weight"));
      }
   }

   function updateItem()
   {  
      var vfield = jQuery("#field").val();
      
      var vid = vfield.substr(0,1)+"_"+jQuery("#field_name").val();
     
      if(jQuery("#"+vid).length<=0)
      { 
         jQuery("#"+jQuery("#item_old").val()).remove();
         addItem();
         return;
      }
      
      var vname = jQuery("#disp_name").val();
      var vtext = jQuery("#field_name").find("option:selected").text();//alert(vtext);
      if(vname=="")
         vname = vtext
      var vcalc_type = vformulate = vweight = "";
      if(jQuery("input[name='group_type']:checked").length > 0)
      {
         if(jQuery("input[name='calc_type']:checked").length > 0)
         {
           vcalc_type = jQuery("input[name='calc_type']:checked").val();
           if(vcalc_type=="1")
                  vweight = jQuery("#calc_weight").val();
           if(vcalc_type == "3")
              vformulate = jQuery("#calc_formula").val();
         }
      }
      
      jQuery("#"+vid).attr("disp_name",vname).html(vname);
      if(vfield == "field_list") //统一属性
         jQuery("#"+vid).attr("calc_type",vcalc_type).attr("formulate",vformulate).attr("weight",vweight);         
   }
   
   function get_item_str(field)
   {
       var item_str = "";
       jQuery("#"+field+" .ui-state-default").each(function(){
           item_str += jQuery(this).attr("id").substr(2)+"`"+jQuery(this).attr("disp_name");
           if(field == "field_list")
            item_str += "`"+jQuery(this).attr("calc_type")+"`"+jQuery(this).attr("formulate")+"`"+jQuery(this).attr("weight")+",";
           else
            item_str += ",";
           });
       return item_str;
   }

   //统计一个字符串在另一个字符串中出现的个数
  function   getlen(str,ch){ 
    var   ret=0; 
    for(var i=0;i<str.length;i++)
    {
      if(str.charAt(i)==ch)
        ret++;
    } 
    return ret; 
  }


   function check_form()
   {

       if(jQuery("#r_name").val()=="")
       {
           alert("请填写模板名称！");
           jQuery("#r_name").focus();
           return false;
       }
       if(jQuery("input[name='group_type']:checked").length <= 0)
       {
         alert("请选择统计方式！");
         return false;
       }

       document.form2.list_item.value = get_item_str("field_list");

       document.form2.query_item.value = get_item_str("query_list");
       if(document.form2.list_item.value == "")
       {
         alert("报表统计字段不能为空！");
         return false;      
     }
     if(jQuery("#calc_formula").val()!="")
     {
       if(getlen(jQuery("#calc_formula").val(), "(") != getlen(jQuery("#calc_formula").val(), ")"))
       {
         alert("自定义公式括号不匹配!");
         return false;
       }
     }

       return true;
   }
   
   
   function check_form2()
   { 

       if(jQuery("#r_name").val()=="")
       {
           alert("请填写模板名称！");
           jQuery("#r_name").focus();
           return false;
       } 
       if(jQuery("input[name='group_type']:checked").length <= 0)
       {
         alert("请选择统计方式！");
         return false;
       }
       document.form1.list_item.value = get_item_str("field_list");
       document.form1.query_item.value = get_item_str("query_list");

       if(document.form1.list_item.value == "")
       {
         alert("报表统计字段不能为空！");
         return false;      
     }
     if(jQuery("#calc_formula").val()!="")
     {
       if(getlen(jQuery("#calc_formula").val(), "(") != getlen(jQuery("#calc_formula").val(), ")"))
       {
         alert("自定义公式括号不匹配!");
         return false;
       }
     }

       return true;
   }
   
  function getDataTypeStr(name,item,dataType){
	 
	  if(dataType=="STR"){
	      return name+"：<input class=\"SmallInput\" type=\"text\" size=20 name=\""+item+"\" value=\"\">";
	  }else if(dataType=="USER"){ 
	      return name+": <input type=\"hidden\" name=\""+item+"\" id=\""+item+"\" value=\"\">"
                 +" <input type=\"text\" readonly name=\""+item+"_name\" id=\""+item+"_name\" value=\"\">"
                 +"<a href=\"javascript:;\" class=\"orgAdd\" onClick=\"selectUser(['"+item+"' , '"+item+"_name'])\"> 选择</a>"
                 +"<a href=\"javascript:;\" class=\"orgClear\" onClick=\"$('"+item+"').value='' , $('"+item+"_name').value = ''\">清空</a>";

	      
	  }else if(dataType=="DEPT"){
		 return name+": <input type=\"hidden\" id=\""+item+"\" name=\""+item+"\" value=\"\">"
		       +"<textarea cols=30 name=\""+item+"_NAME\"  id=\""+item+"_NAME\"  rows=4 class=\"BigStatic\" wrap=\"yes\" readonly></textarea>"
		       +"<a href=\"javascript:;\" class=\"orgAdd\" onClick=\"selectDept(['"+item+"','"+item+"_NAME'])\">添加</a>"
		       +"<a href=\"javascript:;\" class=\"orgClear\" onClick=\"$('"+item+"').value='' , $('"+item+"_NAME').innerHTML = ''\">清空</a>";

	  }else if(dataType=="DATE"){

		  return  name+":  <input type=\"text\"  name=\""+item+"\" id=\""+item+"\" size=\"11\" maxlength=\"10\"  class=\"BigInput\" value=\"\" readonly=\"true\">"
	             +"<img id=\""+item+"_date\" align=\"middle\" src=\"\\yh\\core\\styles\\style1\\img\\calendar.gif\" align=\"middle\" border=\"0\" style=\"cursor:pointer\" >"
		         +" 至:  <input type=\"text\"  name=\""+item+"1\" id=\""+item+"1\" size=\"11\" maxlength=\"10\"  class=\"BigInput\" value=\"\" readonly=\"true\">"
                 +"<img id=\""+item+"1_date\" align=\"middle\" src=\"\\yh\\core\\styles\\style1\\img\\calendar.gif\" align=\"middle\" border=\"0\" style=\"cursor:pointer\" >";
	  
	  }else{
		  return ""
	  }
	  
  } 
   
 function setQueryDate(item,dataType){
	 if(dataType=="DATE"){
	     var date1Parameters = {
			     inputId:item,
			     property:{isHaveTime:false}
			     ,bindToBtn:item+'_date'
			  };
	     new Calendar(date1Parameters);
	     var date2Parameters = {
			     inputId:item+"1",
			     property:{isHaveTime:false}
			     ,bindToBtn:item+'1_date'
			  };
	     new Calendar(date2Parameters);
	 }
 }
   