
CREATE TABLE `doc_flow_run` (
  `RUN_ID` int(11) NOT NULL,
  `DOC_ID` varchar(200) default NULL,
  `DOC_NAME` varchar(200) default NULL,
  `SEQ_ID` int(11) NOT NULL auto_increment,
  `DOC_STYLE` varchar(200) default NULL,
  `DOC_NUM` int(10) unsigned default NULL,
  `DOC` text,
  `DOC_YEAR` varchar(45) default NULL,
  `DOC_WORD` int(10) unsigned default NULL,
  `DRAFT_TIME` datetime default NULL,
  `WRITTEN_TIME` datetime default NULL,
  `TO_FILE_TIME` datetime default NULL,
  `DOC_TYPE` int(10) unsigned default NULL,
  PRIMARY KEY  (`RUN_ID`),
  UNIQUE KEY `SEQ_ID` (`SEQ_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=173 DEFAULT CHARSET=utf8;


