<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsComment,oa.core.funcs.bbs.act.BBSUtil" %>

<%@page import="java.io.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%
String rpath=BBSUtil.bbsPath;
 String aid=request.getParameter("attachId");
 String []aids=BBSUtil.split(aid,"_");
 String ain=request.getParameter("attachName");
 File file = new File(rpath +"bbs/"+aids[0]+"/"+ aids[1]+"."+ain);

        //得到下载文件的名字
        //String filename=request.getParameter("filename");
        //设置response的编码方式
        response.setContentType("application/x-download");
        //写明要下载的文件的大小
        response.setContentLength((int)file.length());
        //设置附加文件名
       // response.setHeader("Content-Disposition","attachment;filename="+filename);
        
        //解决中文乱码
    response.setHeader("Content-Disposition","attachment;filename="+new String
(ain.getBytes("gbk"),"iso-8859-1"));       
        //读出文件到i/o流
        FileInputStream fis=new FileInputStream(file);
        BufferedInputStream buff=new BufferedInputStream(fis);
        byte [] b=new byte[1024];//相当于我们的缓存
        long k=0;//该值用于计算当前实际下载了多少字节
        //从response对象中得到输出流,准备下载
        OutputStream myout=response.getOutputStream();
        //开始循环下载
        while(k<file.length()){
            int j=buff.read(b,0,1024);
            k+=j;
            //将b中的数据写到客户端的内存
            myout.write(b,0,j);
        }
        //将写入到客户端的内存的数据,刷新到磁盘
        myout.flush();
%>
