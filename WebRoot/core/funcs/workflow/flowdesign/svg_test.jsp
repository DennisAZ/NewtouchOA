<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
int styleIndex = 1;
String stylePath = contextPath + "/core/styles/style" + styleIndex;
String imgPath = stylePath + "/img";
String cssPath = stylePath + "/css";
String flowId = request.getParameter("flowId");
%>
<html>
<head>
<title>test</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script language=javascript >
var contextPath = '<%=contextPath%>';
var twConstants=
{
DIALECT_SVG:'svg',
DIALECT_VML:'vml',
NS_SVG:'http://www.w3.org/2000/svg',
NS_XLINK:'http://www.w3.org/1999/xlink'
}
var isIE=false;
function checkBrowser(){
    return navigator.appName == "Microsoft Internet Explorer";       
}
isIE=checkBrowser();
function getSVGDocument(svg){
    var result=null;
    result=svg.ownerDocument;
    return result;
}
function getSVGRoot(svg,doc){
    if(svg.tagName.toLowerCase()=="embed"){
        if(doc){
            return doc.documentElement;
        }else{
            return getSVGDocument(svg).documentElement;
        }
    }else if(svg.tagName.toLowerCase()=="svg"){
        return svg;
    }return null;
}
//空SVG文件路径
var emptySVGSrc="a.svg";
//在Html中创建一个SVG节点，指定id，父亲节点，对于IE创建<embed />，其他浏览器创建<SVG />
function createSVG(id,parent){
    var svg=document.createElementNS(twConstants.NS_SVG,'svg');
    svg.setAttribute("id",id);
    svg.setAttribute("width","100%");
    svg.setAttribute("height","100%");
    svg.setAttribute("onmousedown","Grab(evt)");
    svg.setAttribute("onmousemove","Drag(evt)");
    svg.setAttribute("onmouseup","Drop(evt)");
    parent.appendChild(svg);
    return svg;
}
//得到SVGDocument
function getSVGDocument(svg){
    var result=null;
    result=svg.ownerDocument;
    return result;
}
//得到SVG根结点
function getSVGRoot(svg,doc){
    if(svg.tagName.toLowerCase()=="embed"){
        if(doc){
            return doc.documentElement;
        }else{
            return getSVGDocument(svg).documentElement;
        }
    }else if(svg.tagName.toLowerCase()=="svg"){
        return svg;
    }return null;
}
var svg;
var addRect=function(svg){
    var svgdoc=getSVGDocument(svg);
    var svgRoot=getSVGRoot(svg);
    var rect=svgdoc.createElementNS(twConstants.NS_SVG,'rect');
    rect.setAttribute("id","rect2");
    rect.setAttribute("x",10);
    rect.setAttribute("y",10);
    rect.setAttribute("width",200);
    rect.setAttribute("height",200);
    rect.setAttribute("fill",'red');
    svgRoot.appendChild(rect);
}
function call(){
    var body=document.getElementsByTagName('body')[0];
    svg=createSVG('svgid',body);
    Init();
	createVml();
}

//开始，结束
function createOval(prcs){
  var prcsTo = prcs.prcsTo.replace(",0",",结束")
  if(prcsTo == '0' || !prcsTo || prcsTo == "0,"){
    prcsTo = "结束";
  }
  var prcsToTitle = "·下一步骤:" + prcsTo;
  var sOval = "<ellipse id="
    + prcs.prcsId +" table_id="
    + prcs.tableId + " flowType='"
    + prcs.flowType + "'  passCount='0'  onDblClick='Edit_Process2("+ prcs.tableId +")'  flowTitle='"
    + prcs.flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
    + prcs.fillcolor + "'  style='LEFT:" 
    + prcs.leftVml + "; TOP:" 
    + prcs.topVml + ";WIDTH: 120; POSITION: absolute; HEIGHT: 60;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
    + prcs.flowTitle + "\n" + prcsToTitle + "'/>"; 
  
   var styleText = 'cursor:pointer;TEXT-ALIGN:center;z-index:1';
   var svgdoc=getSVGDocument(svg);
    var svgRoot=getSVGRoot(svg);
    var ellipse=svgdoc.createElementNS(twConstants.NS_SVG,'ellipse');
    ellipse.setAttribute("id",prcs.prcsId);
    ellipse.setAttribute("table_id",prcs.tableId);
    ellipse.setAttribute("flowType",prcs.flowType);
    ellipse.setAttribute("passCount",'0');
    ellipse.setAttribute("onclick","Edit_Process2()");
    ellipse.setAttribute("flowTitle",prcs.flowTitle);
    ellipse.setAttribute("flowFlag",'0');
    ellipse.setAttribute("readOnly",'0');
    ellipse.setAttribute("receiverID",'');
    ellipse.setAttribute("receiverName",'');
    ellipse.setAttribute("arcsize",'4321f');
    ellipse.setAttribute("coordsize",'21600,21600');
    ellipse.setAttribute("style",styleText);
    
    ellipse.setAttribute("cx",prcs.leftVml+50);
    ellipse.setAttribute("cy",prcs.topVml);
    ellipse.setAttribute("rx",60);
    ellipse.setAttribute("ry",30);
    ellipse.setAttribute("fill",prcs.fillcolor);
    ellipse.setAttribute("title","▲"+prcs.flowTitle+"\n" + prcsToTitle);
    svgRoot.appendChild(ellipse);  
}
function Edit_Process2(){
alert('a');
 // editProcess2(flowId , seqId); 
}

var requestURL;
var flowId = '<%=flowId%>';
function createVml(){
  requestURL = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowProcessAct";
  var url = requestURL + "/getProcessList.act";
  var json = getJsonRs(url , 'flowId=' + flowId);
  if(json.rtState == '1'){
    alert(json.rtMsrg);
    return ;
  }
  prcsJson = json.rtData.prcsList;
  var isSetPriv = json.rtData.isSetPriv;
  var firstPrcsSeqId;
  for(var i = 0 ;i < prcsJson.length ; i++){
    var prcs = prcsJson[i];
    if(prcs.prcsId == 1){
      firstPrcsSeqId = prcs.tableId;
    }
    if(prcs.flowType == 'start' || prcs.flowType == 'end'){
      createOval(prcs);
    }
    }
}
</script>
 <script type="text/javascript">
      var SVGDocument = null; 
      var SVGRoot = null; 
      var TrueCoords = null; 
      var GrabPoint = null; 
      var BackDrop = null; 
      var DragTarget = null; 

      function Init(evt) 
      { 
         SVGDocument = getSVGDocument(svg); 
         SVGRoot = getSVGRoot(svg);

         // these svg points hold x and y values... 
         //    very handy, but they do not display on the screen (just so you know) 
         TrueCoords = SVGRoot.createSVGPoint(); 
         GrabPoint = SVGRoot.createSVGPoint(); 

         // this will serve as the canvas over which items are dragged. 
         //    having the drag events occur on the mousemove over a backdrop 
         //    (instead of the dragged element) prevents the dragged element 
         //    from being inadvertantly dropped when the mouse is moved rapidly 
         BackDrop = SVGDocument.getElementById('svgid'); 
      } 

      function Grab(evt) 
      { 
         // find out which element we moused down on 
         var targetElement = evt.target; 

         // you cannot drag the background itself, so ignore any attempts to mouse down on it 
         if ( BackDrop != targetElement ) 
         { 
            //set the item moused down on as the element to be dragged 
            DragTarget = targetElement; 

            // move this element to the "top" of the display, so it is (almost) 
            //    always over other elements (exception: in this case, elements that are 
            //    "in the folder" (children of the folder group) with only maintain 
            //    hierarchy within that group 
            DragTarget.parentNode.appendChild( DragTarget ); 

            // turn off all pointer events to the dragged element, this does 2 things: 
            //    1) allows us to drag text elements without selecting the text 
            //    2) allows us to find out where the dragged element is dropped (see Drop) 
            DragTarget.setAttributeNS(null, 'pointer-events', 'none'); 

            // we need to find the current position and translation of the grabbed element, 
            //    so that we only apply the differential between the current location 
            //    and the new location 
            var transMatrix = DragTarget.getCTM(); 
            GrabPoint.x = TrueCoords.x - Number(transMatrix.e); 
            GrabPoint.y = TrueCoords.y - Number(transMatrix.f); 

         } 
      }; 


      function Drag(evt) 
      { 
         // account for zooming and panning 
         GetTrueCoords(evt); 

         // if we don't currently have an element in tow, don't do anything 
         if (DragTarget) 
         { 
            // account for the offset between the element's origin and the 
            //    exact place we grabbed it... this way, the drag will look more natural 
            var newX = TrueCoords.x - GrabPoint.x; 
            var newY = TrueCoords.y - GrabPoint.y; 

            // apply a new tranform translation to the dragged element, to display 
            //    it in its new location 
            DragTarget.setAttributeNS(null, 'transform', 'translate(' + newX + ',' + newY + ')'); 
         } 
      }; 


      function Drop(evt) 
      { 
         // if we aren't currently dragging an element, don't do anything 
         if ( DragTarget ) 
         { 
            // since the element currently being dragged has its pointer-events turned off, 
            //    we are afforded the opportunity to find out the element it's being dropped on 
            var targetElement = evt.target; 

            // turn the pointer-events back on, so we can grab this item later 
            DragTarget.setAttributeNS(null, 'pointer-events', 'all'); 
            if ( 'Folder' == targetElement.parentNode.id ) 
            { 
               // if the dragged element is dropped on an element that is a child 
               //    of the folder group, it is inserted as a child of that group 
               targetElement.parentNode.appendChild( DragTarget ); 
              
            } 
            else 
            { 
               // for this example, you cannot drag an item out of the folder once it's in there; 
               //    however, you could just as easily do so here 
            } 

            // set the global variable to null, so nothing will be dragged until we 
            //    grab the next element 
            DragTarget = null; 
         } 
      }; 


      function GetTrueCoords(evt) 
      { 
         // find the current zoom level and pan setting, and adjust the reported 
         //    mouse position accordingly 
         var newScale = SVGRoot.currentScale; 
         var translation = SVGRoot.currentTranslate; 
         TrueCoords.x = (evt.clientX - translation.x)/newScale; 
         TrueCoords.y = (evt.clientY - translation.y)/newScale; 
      }; 

   </script> 
</head>
<body onload='call()'>
</body>
</html> 