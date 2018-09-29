package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 消息附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:12:45 PM<br>
 * @author zjr
 */
public class AttMes extends Attachment implements java.io.Serializable {
    // Fields    
     private Message message;

    // Constructors

    /** default constructor */
    public AttMes() {
    }
    
    /** full constructor */
    public AttMes(Message message) {
        this.message = message;
    }
   
    // Property accessors


    public Message getMessage() {
        return this.message;
    }
    
    public void setMessage(Message message) {
        this.message = message;
    }


}