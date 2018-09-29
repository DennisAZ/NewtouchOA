package com.psit.struts.entity;

import java.util.Date;


/**
 * 
 * 报告附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:18:08 PM<br>
 * @author zjr
 */

public class AttRep extends Attachment  implements java.io.Serializable {

    // Fields   
     private Report report;

    // Constructors

    /** default constructor */
    public AttRep() {
    }
    
    /** full constructor */
    public AttRep(Report report) {
        this.report = report;
    }
   
    // Property accessors

    public Report getReport() {
        return this.report;
    }
    
    public void setReport(Report report) {
        this.report = report;
    }
}