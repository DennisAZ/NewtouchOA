function YHField(key,value){
	this.value = value;
	this.key = key;
}
YHField.prototype.getKey = _getKey;
YHField.prototype.getValue = _getValue;
function _getKey(){
	return this.key;
}
function _getValue(){
	return this.value;
}
// 定义一个record类用来存储多个field
// 一个record对应多个field
function YHRecord(){
	var _fields = new Array();
	this.fields = _fields ; 
	var _oprates = new Array();
	this.oprates = _oprates;
}
YHRecord.prototype.addField = _addField;
YHRecord.prototype.getField = _getField;
YHRecord.prototype.addOprates = _addOprates;
function _addField(field){
	this.fields.push(field);
	return this;
}
function _getField(key){
	var fields = this.fields ;
	for(var i = 0; i < fields.length; i ++){
		if(fields[i].getKey()==key){
			
			return fields[i];
		}
	}
}
function _addOprates(oprates){
  this.oprates = oprates;
}
// 定义一个解析json数据的类
function YHDataJSONParser(url){
	this.total ;
	this.url = url ;
}
YHDataJSONParser.prototype.parser = _parser ; 
YHDataJSONParser.prototype.getResu = _getResu ; 
// 将json数据解析成一组YHrecord对象 返回一个包含YHrecord对象的数组
function _parser(hdarray){
  if(this.url == null){return null;}
  var json =this.getResu();
  this.total = json.total;
	var rcs = new Array();
	var jsonText = json;
	for(var i = 0; i < jsonText.records.length; i++){
		var rc = new YHRecord();
		for(var j = 0; j < hdarray[0].length; j++){
			var key = hdarray[0][j].name ;
			var value = jsonText.records[i][key];
			var field = new YHField(key,value);
			rc.addField(field);
			rc.addOprates(hdarray[1].oprates);
		}
		rcs.push(rc);
	}
	return rcs;
}
// ----------------------------------------ajax-------------------------//
function _getResu(){
  var rtJson = getJsonRs(this.url);
  return rtJson;
}
// -----------------------------------------ajax------------------------//
/**
 * @param domId
 *          所要放置Grid组件的dom组件间id
 * @param hdarray
 *          grid头部信息数组
 * @param url
 *          获得数据的url地址
 * @param isSynchronous
 *          是否同步操作
 */
function YHGrid(hdarray,url,isSynchronous,numOfPage){
	this.body ;
	this.total ; 
	this.herder = hdarray ;
	this.url = url ;
	this.records = 1;
	this.isSynchronous = isSynchronous ;
	this.dataTab;
	this.numOfPage = numOfPage;
	this.init = function(){
	  var gridBottomBar ;
		this.body = _createGridPanel();
		this.dataTab = _createDataTable(this.herder);
		this.dataTab.pageNum = this.numOfPage;
	
		var gridTopBar = _createTopBar();
		var gridDataPanel = _createDataPanel();
		gridDataPanel.appendChild(this.dataTab);
	  this.body.appendChild(gridTopBar);
    this.body.appendChild(gridDataPanel);
		if(this.url != null){
  		 var urli = this.url + '&&pageNum=0&&pageRows=' +  this.numOfPage+'';
  		 this.total =this.loadData(this.dataTab,this.herder,urli);
  		 if(this.numOfPage > 0){
         var num = Math.ceil(this.total/this.numOfPage);
         if(num == 0){
           num = 1;
         }
         gridBottomBar = _createPageButtonBar(this.numOfPage,this.herder,this.url,this.dataTab,num,this);
       }else{
         gridBottomBar = _createBottomBar();
       }
		 }else{
		   gridBottomBar = _createBottomBar();
		 }
		this.body.appendChild(gridBottomBar);
	}
	function _createGridPanel(){
		var gridPanel = $C('div');
		gridPanel.className = 'YH_grid';
		return gridPanel;
	}
	// 拆分成3个方法
	/**
   * grid 的底部bar 可以定制，暂未完成。
   */
	function _createBottomBar(){
		var gridBottomBar = $C('div');
		gridBottomBar.className = 'YH_grid_bottomBar';
		return gridBottomBar;
	}
	// _____________________________________
	this.setJson = function(jsons){
	  this.json = jsons; 
	}
	// ---------------------------------------
	 function _createPageButtonBar(numOfpage,hdarray,url,_table,total,grid){
	  var  gridBottomBar = _createBottomBar();
	 
	  var button = $C('button');
    var input  = $C('input');
    input.className = 'YH_grid_bottomBar_input';
    input.id = 'YH_grid_bottomBar_inputID';
    input.type = 'text';
    input.value = '1';
    input.size = '2';
    input.onkeydown= function(event){
      var e = event ? event :(window.event ? window.event : null); 
      if(e.keyCode == 13){
        if(this.value <= total && this.value >= 1)
        { 
          if(this.value == 1){
            document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton';
            document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton';
            document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton2';
            document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_priButtonID').disabled = true;
            document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton2';
            document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = true ;
          }else if(this.value == total){
            document.getElementById('YH_grid_bottomBar_priButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton';
            document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton';
            document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton2';
            document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = true;
            document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton2';
            document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = true;
          }else{
            document.getElementById('YH_grid_bottomBar_priButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton';
            document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton';
            document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton';
            document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = false;
            document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton';
            document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = false;
          }
          var inurl =  url + '&&pageNum='+ (this.value - 1) +'&&pageRows=' + numOfpage;
          document.getElementById('YH_grid_bottomBar_priButtonID').page = this.value-1;
          document.getElementById('YH_grid_bottomBar_nextButtonID').page = this.value-1;
          grid.loadData(_table,hdarray,inurl);
        }else{
          alert("输入错误，请检查！");
          /*
           * alert(this.clientHeight); alert(this.clientWidth); var div =
           * $C('div'); alert(div.style); div.style.position = 'absolute';
           * div.style.top = this.clientHeight; div.style.left =
           * this.clientWidth; div.style.backgroundColor="#FFCC80";
           * div.innerHTML = "sssss"; document.body.appendChild(div);
           */
        }
      }
    }
    button.className = 'YH_grid_bottomBar_firstButton2';
    button.page = 0;
    button.id = 'YH_grid_bottomBar_firstButtonID';
    button.url = url + '&&pageNum=0&&pageRows=' + numOfpage;
    button.disabled = true ;
    button.onclick = function(){
      if(this.page == 0){
        this.className = 'YH_grid_bottomBar_firstButton2';
        this.disabled = true ;
      }else{
        this.className = 'YH_grid_bottomBar_firstButton';
        this.disabled = false ;
      }
      document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton';
      document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton';
      document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton2';
      document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = false;
      document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = false;
      document.getElementById('YH_grid_bottomBar_priButtonID').disabled = true;
      document.getElementById('YH_grid_bottomBar_priButtonID').page = this.page ;
      document.getElementById('YH_grid_bottomBar_nextButtonID').page = this.page ;
      document.getElementById('YH_grid_bottomBar_inputID').value = 1 ;
      grid.loadData(_table,hdarray,this.url);
    }
    gridBottomBar.appendChild(button);
    button = $C('button');
    button.className = 'YH_grid_bottomBar_priButton2';
    button.id = 'YH_grid_bottomBar_priButtonID';
    button.page = 0 ;
    button.disabled = true ;
    button.url = url + '&&pageNum=0&&pageRows=' + numOfpage;
    button.onclick = function(){
        if(this.page == 1){
          this.className = 'YH_grid_bottomBar_priButton2';
          this.disabled = true;
          document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton2';
          document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = true ;
        }else{
          this.className = 'YH_grid_bottomBar_priButton';
          this.disabled = false ;
        }
        this.page = this.page - 1;
        this.url =  url + '&&pageNum='+ this.page+'&&pageRows=' + numOfpage;
        document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton';
        document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton';
        document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = false;
        document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = false;
        document.getElementById('YH_grid_bottomBar_nextButtonID').page = this.page;
        document.getElementById('YH_grid_bottomBar_inputID').value = this.page+1;
        grid.loadData(_table,hdarray,this.url);
    }
    gridBottomBar.appendChild(button);
    var span = $C('span');
    span.innerHTML = ' page ';
    gridBottomBar.appendChild(span);
    gridBottomBar.appendChild(input);
    span = $C('span');
    span.innerHTML = ' of ';
    gridBottomBar.appendChild(span);
    span = $C('span');
    span.innerHTML = total;
    gridBottomBar.appendChild(span);
    button = $C('button');
    if(total == 1){
      button.className = 'YH_grid_bottomBar_nextButton2';
      button.disabled = true ;
    }else{
    button.className = 'YH_grid_bottomBar_nextButton';
    }
    button.page = 0 ;
    button.url = url + '&&pageNum=0&&pageRows=' + numOfpage;
    button.id = 'YH_grid_bottomBar_nextButtonID';
    button.onclick = function(){
          if(this.page == total -2){
            this.className = 'YH_grid_bottomBar_nextButton2';
            this.disabled = true ;
            document.getElementById('YH_grid_bottomBar_lastButtonID').className = 'YH_grid_bottomBar_lastButton2';
            document.getElementById('YH_grid_bottomBar_lastButtonID').disabled = true ;
          }else{
            this.className = 'YH_grid_bottomBar_nextButton';
            this.disabled = false;
          }
        this.page = this.page + 1;
        this.url = url + '&&pageNum=' + this.page + '&&pageRows=' + numOfpage;
        document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton';
        document.getElementById('YH_grid_bottomBar_priButtonID').disabled = false;
        document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton';
        document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = false;
        document.getElementById('YH_grid_bottomBar_priButtonID').page = this.page ;
        document.getElementById('YH_grid_bottomBar_inputID').value = this.page + 1;
        grid.loadData(_table,hdarray,this.url);
    }
    gridBottomBar.appendChild(button);
    button = $C('button');
    if(total == 1){
      button.className = 'YH_grid_bottomBar_lastButton2';
      button.disabled = true ;
    }else{
    button.className = 'YH_grid_bottomBar_lastButton';
    }
    button.id = 'YH_grid_bottomBar_lastButtonID';
    button.page = total - 1 ;
    button.url = url + '&&pageNum='+ button.page +'&&pageRows=' + numOfpage;
    button.onclick = function(){
      if(this.page == (total - 1)){
        this.className = 'YH_grid_bottomBar_lastButton2';
        this.disabled = true;
      }else{
        this.disabled = false;
        this.className = 'YH_grid_bottomBar_lastButton';
      }
      document.getElementById('YH_grid_bottomBar_priButtonID').disabled = false;
      document.getElementById('YH_grid_bottomBar_priButtonID').className = 'YH_grid_bottomBar_priButton';
      document.getElementById('YH_grid_bottomBar_firstButtonID').className = 'YH_grid_bottomBar_firstButton';
      document.getElementById('YH_grid_bottomBar_firstButtonID').disabled = false;
      document.getElementById('YH_grid_bottomBar_nextButtonID').className = 'YH_grid_bottomBar_nextButton2';
      document.getElementById('YH_grid_bottomBar_nextButtonID').disabled = true;
      document.getElementById('YH_grid_bottomBar_priButtonID').page = this.page ;
      document.getElementById('YH_grid_bottomBar_nextButtonID').page = this.page ;
      document.getElementById('YH_grid_bottomBar_inputID').value = this.page+1 ;
      grid.loadData(_table,hdarray,this.url);
    }
    gridBottomBar.appendChild(button);
	  return gridBottomBar;
	}
	 // ----------------------------------------------------------------------------------
	/**
   * GRID 的顶部bar 可以定制，暂未完成。
   */
	function _createTopBar(){
		var gridTopBar = $C('div');
		gridTopBar.className = 'YH_grid_topBar';
		return gridTopBar;
	}
  // ----------------------------------------------------------------------------------
	/**
   * GRID 的数据panel 可以定制，暂未完成。
   */
	function _createDataPanel(){
		var gridDataPanel= $C('div');
		gridDataPanel.className = 'YH_grid_data';
		return gridDataPanel;
	}
  // ----------------------------------------------------------------------------------
	function _createDataTable(hdarray){
		var gridDataTable= $C('table');
		gridDataTable.id = 'YH_grid_table';
		gridDataTable.className = 'YH_grid_table';
		gridDataTable.cellSpacing = 0;
		gridDataTable.cellPadding = 3;
		gridDataTable =new  function(){
			var tabRow = gridDataTable.insertRow(0);
			tabRow.className = 'YH_grid_data_header';
			for(var i = 0;i < hdarray[0].length;i++){
			  if(hdarray[0][i].hidden){
			  }else{
				var th = document.createElement('th');
				th.className = 'YH_grid_data_th';
				th.appendChild(document.createTextNode(hdarray[0][i].header));
				th.setAttribute('name',hdarray[0][i].name);
				tabRow.appendChild(th);
			  }
			}
			if(hdarray[1].header){
			 var th = document.createElement('th');
       th.className = 'YH_grid_data_th';
			 th.appendChild(document.createTextNode(hdarray[1].header));
       th.setAttribute('name',hdarray[1].name);
       tabRow.appendChild(th);
			}
			return gridDataTable;
		}
		return gridDataTable;
	}
	// ---------------------------------------------------------------------------------------//
	this.reShow = function(url){
	  var pageNum = document.getElementById('YH_grid_bottomBar_priButtonID').page;
	  var _url = url+'&&pageNum='+ pageNum +'&&pageRows='+this.numOfPage;
	  this.loadData(this.dataTab,this.herder,_url);
	}
	this.loadData = function(_table,hdarray,url){
	  var data = new YHDataJSONParser(url);
    var records = data.parser(hdarray);
    this.records = records;
    var record = null;
    var oprates = null;
    var condition ;
    var table_length = _table.rows.length;
		if(table_length > 1 ){
			for(var i = 1; i <table_length;i ++){
				_table.deleteRow(1)	;
			}
		}
		if(_table.pageNum){
		  condition = _table.pageNum;
		}else{
		  condition = records.length;
		}
		for(var i = 1; i <= condition;i ++){
		  if(i <= records.length){
		    record = records[i-1];
		    oprates = record.oprates;
		  }else{
		    record = null;
		    oprates = null;
		  }
			var tabRow = _table.insertRow(i);
			if((i % 2) != 0){
			tabRow.className = 'YH_grid_table_line1';
			}else{
			  tabRow.className = 'YH_grid_table_line2';
			}
			tabRow.isslect = false ;
			var j ,td,h = 0;
			for(j = 0 ; j < hdarray[0].length; j ++){
			  if(hdarray[0][j].hidden){
			    h--;
        }else{
			  td = tabRow.insertCell(h);
			  td.style.cursor = "pointer";
			  td.id = 'YH_Grid_table_td_'+hdarray[0][j].name+i;
			  if(record != null){
					  td.innerHTML = record.getField(hdarray[0][j].name).getValue();
					}else{
					 td.innerHTML =  '&nbsp;';
					}
			  if(hdarray[0][j].width)
			  td.width = hdarray[0][j].width ;
        }
			  h++;
			}
			if(oprates){
			var oprateTd = tabRow.insertCell(h);
		  if(hdarray[1].width){
		    oprateTd.width = hdarray[1].width ;
        }
			if(oprates != null){
      var oprateDiv = $C('div');
      oprateDiv.className = 'YH_Grid_OpratePanel';
      var orpateSelect = $C('select');
      orpateSelect.row = i;
      orpateSelect.className = 'YH_Grid_OpratePanel_select';
      orpateSelect.record = record ;
      var option = $C('option');
      option.text =  "请选择操作";
      try
      {
        orpateSelect.add(option,null); 
      }
      catch(ex)
      {
      orpateSelect.add(option); 
      }
      
      oprateDiv.className = 'YH_Grid_data_opratePanel';
      var defualtOprate ;
      if(oprates.length > 2 ){
        for(var k = 0; k < oprates.length; k++ ){
          if(oprates[k].isDefault){
            defualtOprate = $C('a');
            defualtOprate.className = 'YH_Grid_data_opratePanel_A';
            defualtOprate.record = record;
            defualtOprate.row = i;
            defualtOprate.innerHTML = oprates[k].name;
            defualtOprate.fun = oprates[k].oprate;
            defualtOprate.onclick = function(){
              var fun = this.fun;
              fun(this.record,this.row);
            }
            oprateDiv.appendChild(defualtOprate);
            oprateDiv.appendChild(document.createTextNode(" "));
          }else{
            var option = $C('option');
            option.text =  oprates[k].name;
            option.fun = oprates[k].oprate;
            try
            {
              orpateSelect.add(option,null); 
            }
            catch(ex)
            {
            orpateSelect.add(option); 
            }
          orpateSelect.onchange = function(){
           var index= this.selectedIndex;
           var op = this.options[index];
           if(op.fun){
           var fun = op.fun;
           fun(this.record,this.row);
           }
          }
          }
        }
        }else{
          for(var k = 0; k < oprates.length; k++ ){
              defualtOprate = $C('a');
              defualtOprate.row = i;
              defualtOprate.record = record;
              defualtOprate.innerHTML = oprates[k].name;
              defualtOprate.fun = oprates[k].oprate;
              defualtOprate.onclick = function(){
                var fun = this.fun;
                fun(this.record,this.row);
              }
              oprateDiv.appendChild(defualtOprate);
              oprateDiv.appendChild(document.createTextNode(" "));
          }
        }
      if(orpateSelect.options.length != 1)
      oprateDiv.appendChild(orpateSelect);
      oprateTd.appendChild(oprateDiv);
      
      }
			}
    }
			// selectRow(_table) ;//行选控件的开启
		return data.total;
	}
  // ----------------------------------------------------------------------------------
	this.rendTo = function(dom){
		this.init();
		var con = window.document.getElementById(dom);
		con.appendChild(this.body);
	}
	this.rendToDom = function(dom){
    this.init();
    dom.appendChild(this.body);
    return dom;
  }
	this.getRec = function(){
	  return this.records;
	}
  // ----------------------------------------------------------------------------------
	/*
   * 行选控件 function selectRow(table) { var sTable = table ; for(var i=1;i <
   * sTable.rows.length;i++) // 遍历除第一行外的所有行 { var row = sTable.rows[i]; var
   * className = row.className; row.onmouseover = function(){ className =
   * this.className ; this.className = 'YH_grid_trmin'; } row.onmouseout =
   * function(){ this.className = className; } row.onclick = function(){ var
   * className; for(var j = 1;j < sTable.rows.length;j++){ if((j % 2) != 0){
   * className = 'YH_grid_table_line1'; }else{ className =
   * 'YH_grid_table_line2'; } if(this == sTable.rows[j]){ if(!this.isslect){
   * sTable.rows[j].className = 'YH_grid_trmin'; sTable.rows[j].onmouseover =
   * ""; // 增加onmouseover 事件 sTable.rows[j].onmouseout = "";// 增加onmouseout 事件
   * className = this.className ; this.isslect = true; }else{
   * sTable.rows[j].className = className; sTable.rows[j].onmouseover =
   * function(){ className = this.className ; this.className = 'YH_grid_trmin';
   * }// 增加onmouseover 事件 sTable.rows[j].onmouseout = function(){
   * //alert(this.className); this.className = className; }// 增加onmouseout 事件
   * this.isslect = false; } }else{ sTable.rows[j].className = className;
   * sTable.rows[j].onmouseover = function(){ className = this.className ;
   * this.className = 'YH_grid_trmin'; } // 增加onmouseover 事件
   * sTable.rows[j].onmouseout = function(){ this.className = className; }//
   * 增加onmouseout 事件 sTable.rows[j].isslect = false; } } } } }
   */
	 function $C(tag){
	    return document.createElement(tag);
	  }
}
// 定义一个oprate类
/**
 * @param name
 *          操作名
 * @param isDefault
 *          true/false
 * @param oprate
 *          function
 */
function YHOprate(name,isDefault,oprate){
  this.name = name;
  this.isDefault = isDefault;
  this.oprate = oprate;
}
/** *********单选全选*************** */
	/*
   * // 移过时tr的背景色 function rowOver(target) { target.className =
   * 'YH_grid_trmout'; } // 移出时tr的背景色 function rowOut(target) { target.className =
   * 'YH_grid_trmin'; } // 恢复tr的的onmouseover事件配套调用函数 function resumeRowOver() {
   * rowOver(this); } // 恢复tr的的onmouseout事件配套调用函数 function resumeRowOut() {
   * rowOut(this); } // 全选 function allchecks(o) { var
   * obj=document.getElementsByName(o); var len=obj.length for (var i=0;i<len
   * ;i++ ) { obj[i].checked=true; } } // 反选 function fallchecks(o) { var
   * obj=document.getElementsByName(o); var len=obj.length for (var i=0;i<len
   * ;i++ ) { obj[i].checked=false; } } // 全选函数 function allcheck() { var
   * obj=document.getElementById("allcheck");// 得到全选控件
   * 
   * var obj2=document.getElementById("fcheck");// 得到反选控件
   * 
   * obj2.checked=false;// 如果全选按钮被选则反选按钮不能被选则
   * 
   * if (obj.checked == true) { allchecks("datacheck"); selectRow(); }else
   * if(obj.checked == false){ fallchecks("datacheck"); selectRow(); } } // 反选函数
   * function allfcheck() { var obj=document.getElementById("allcheck");//
   * 得到全选控件
   * 
   * var obj2=document.getElementById("fcheck");// 得到反选控件
   * 
   * obj.checked=false;// 如果反选按钮被选则全选按钮不能被选则
   * 
   * if (obj2.checked == true) { fcheck("datacheck"); selectRow(); }else
   * if(obj2.checked == false){ fcheck("datacheck"); selectRow(); } } // 反选
   * function fcheck(o) { var obj=document.getElementsByName(o); var
   * len=obj.length for (var i=0;i<len ;i++ ) { obj[i].checked=!obj[i].checked; } }
   */


