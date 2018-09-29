package com.psit.struts.entity;

import java.util.Date;


/**
 * 
 * 客户服务附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:18:21 PM<br>
 * @author zjr
 */

public class AttServ extends Attachment  implements java.io.Serializable {

    // Fields    
     private CusServ cusServ;

    // Constructors

    /** default constructor */
    public AttServ() {
    }
    
    /** full constructor */
    public AttServ(CusServ cusServ) {
        this.cusServ = cusServ;
    }

    // Property accessors

    public CusServ getCusServ() {
        return this.cusServ;
    }
    
    public void setCusServ(CusServ cusServ) {
        this.cusServ = cusServ;
    }

}