function loadData () {
	var url = contextPath + moduleSrcPath + "/act/YHFlowSortAct/getSortList.act";
    var json = getJsonRs(url);
    var data = {attachCtrl:'flow_list'
         ,accordionId:'subfield'
           ,data:[]
    };
    if(json.rtState == '0'){
 	  data.data = json.rtData;
    }
    var dd = new Accordion(data);
}
function getList(tmp) {
	
 // parent.mainFrame.location = "list.jsp?sortId=" + tmp.extData + "&sortName=" + encodeURI(tmp.title);
}
function actionFuntion(tmp) {
	var id=tmp.extData;
	id=id.substr(0,id.indexOf(":"));
    parent.mainFrame.location.href="list.jsp?fId="+id;
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



   
		function addOption(){
			  var url = contextPath + moduleSrcPath + "/act/YHFlowReportAct/getListItemAct.act?fId="+fId;
		    var json = getJsonRs(url);
		    if(json.rtState == '0'){
		       var data=json.rtData.data;
		       for(var i=0;i<data.length;i++){
		          jQuery("#group_field").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
		          jQuery("#field_name").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
		          jQuery("#calc_weight").append("<option value='"+data[i].value+"'>"+data[i].text+"</option>");
		       }
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
             jQuery("#calc_sum").attr("disabled",true);
             jQuery("#calc_avg_weight").attr("disabled",true);
             jQuery("#calc_avg").attr("disabled",true);
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
            vid = vfield.substr(0,1)+"_"+jQuery("#field_name").val();//alert(vid);
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
      }
      if(jQuery("#field").val() == "query_list")
         jQuery("#tcalc").hide();
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
      var vid = vfield.substr(0,1)+"_"+jQuery("#field_name").val();//alert(vid);
      if(jQuery("#"+vid).length<=0)
      {
         jQuery("#"+jQuery("#item_old").val()).remove();
         addItem();
         return;
      }
      
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
   