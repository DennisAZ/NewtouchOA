-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.76-enterprise-gpl-nt-log


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


USE t9;
INSERT INTO `doc_flow_process` (`SEQ_ID`,`FLOW_SEQ_ID`,`PRCS_ID`,`PRCS_NAME`,`PRCS_USER`,`PRCS_ITEM`,`HIDDEN_ITEM`,`PRCS_DEPT`,`PRCS_PRIV`,`PRCS_TO`,`SET_LEFT`,`SET_TOP`,`PLUGIN`,`PRCS_ITEM_AUTO`,`PRCS_IN`,`PRCS_OUT`,`FEEDBACK`,`PRCS_IN_SET`,`PRCS_OUT_SET`,`AUTO_TYPE`,`AUTO_USER_OP`,`AUTO_USER`,`USER_FILTER`,`TIME_OUT`,`TIME_EXCEPT`,`SIGNLOOK`,`TOP_DEFAULT`,`USER_LOCK`,`MAIL_TO`,`SYNC_DEAL`,`SYNC_DEAL_CHECK`,`TURN_PRIV`,`CHILD_FLOW`,`GATHER_NODE`,`ALLOW_BACK`,`ATTACH_PRIV`,`AUTO_BASE_USER`,`CONDITION_DESC`,`RELATION`,`REMIND_FLAG`,`DISP_AIP`,`TIME_OUT_TYPE`,`METADATA_ITEM`,`EXTEND`,`EXTEND1`,`AUTO_SELECT_ROLE`,`DOC_CREATE`,`DOC_ATTACH_PRIV`,`DOC_SMS_STYLE`) VALUES 
 (695,572,1,'拟稿','','主题词,内容描述,文种,定密依据,保密期限,主送,抄送,页数,附件标题,备注,公开属性,附件数量,印刷份数,印发日期,文件字,拟稿人,密级,紧急程度,拟稿日期,校对,拟稿单位,编号,联系方式,[A@],标题,',NULL,'0','','2,',131,18,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'','2,3,4,5,',''),
 (701,572,7,'分发','',NULL,NULL,'0','','',131,446,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'','','#{办理人}:#{标题}请你审核。拟稿人：#{拟稿人}\r\n'),
 (696,572,2,'科室领导审核','',NULL,NULL,'0','','3,',141,103,NULL,NULL,NULL,NULL,'0',NULL,NULL,'0',NULL,NULL,'0',NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'','2,3,4,5,','#{办理人}:#{标题}请你审核。拟稿人：#{拟稿人}'),
 (697,572,3,'局领导审核','','联系方式,归档字段,设定书签,编号,docType,拟稿单位,校对,拟稿日期,紧急程度,密级,拟稿人,文件字,印发日期,印刷份数,附件数量,公开属性,备注,附件标题,页数,抄送,主送,保密期限,定密依据,文种,内容描述,主题词,标题,',NULL,'0','','4,',141,181,NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'1','2,3,4,5,','#{办理人}:#{标题}请你审核。拟稿人：#{拟稿人}'),
 (698,572,4,'编号核稿','',NULL,NULL,'0','','5,',141,265,NULL,NULL,NULL,NULL,'1',NULL,NULL,'0',NULL,NULL,'0',NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','1',NULL,'','2,3,4,5,','#{办理人}:#{标题}请你审核。拟稿人：#{拟稿人}\r\n'),
 (699,572,5,'排版打印','',NULL,NULL,'0','','6,7,',141,350,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'','5,','#{办理人}:#{标题}请你排版打印。拟稿人：#{拟稿人}\r\n'),
 (700,572,6,'盖章归档','',NULL,NULL,'0','','7,',299,350,NULL,NULL,NULL,NULL,'1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1','','0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,768,0,'0',NULL,'','',NULL,'','','#{办理人}:#{标题}请你审核。拟稿人：#{拟稿人}\r\n'),
 (694,573,1,'填写办理意见','','原文编号,收文日期,部门,承办人,密级,来文单位,拟办意见,领导批示,标题,附注,等级,联系人,联系电话,文号,年月日,处室意见,','行文类型ID,收文ID,','0','','0,',0,0,'T9DocReciveAfterPlugin',NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'00','0','0','1',NULL,'0',NULL,'1',0,'0','0','1,2,3,4,',0,NULL,NULL,0,0,'0',NULL,NULL,NULL,NULL,NULL,NULL,NULL);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
