package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:11:45 PM<br>
 * @author zjr
 */
public class Attachment  implements java.io.Serializable {


    // Fields    

     private Long attId;
     private String attName;
     private Long attSize;
     private String attPath;
     private String attIsJunk;
     private Date attDate;
     private String attType;


    // Constructors

    /** default constructor */
    public Attachment() {
    }

    
    /** full constructor */
    public Attachment(String attName, Long attSize, String attPath,
			String attIsJunk, Date attDate, String attType) {
		this.attName = attName;
		this.attSize = attSize;
		this.attPath = attPath;
		this.attIsJunk = attIsJunk;
		this.attDate = attDate;
		this.attType = attType;
	}

   
    // Property accessors

    public Long getAttId() {
        return this.attId;
    }
    
    public void setAttId(Long attId) {
        this.attId = attId;
    }


    public String getAttName() {
        return this.attName;
    }
    
    public void setAttName(String attName) {
        this.attName = attName;
    }

    public Long getAttSize() {
        return this.attSize;
    }
    
    public void setAttSize(Long attSize) {
        this.attSize = attSize;
    }

    public String getAttPath() {
        return this.attPath;
    }
    
    public void setAttPath(String attPath) {
        this.attPath = attPath;
    }

    public String getAttIsJunk() {
        return this.attIsJunk;
    }
    
    public void setAttIsJunk(String attIsJunk) {
        this.attIsJunk = attIsJunk;
    }

    public Date getAttDate() {
        return this.attDate;
    }
    
    public void setAttDate(Date attDate) {
        this.attDate = attDate;
    }

    public String getAttType() {
        return this.attType;
    }
    
    public void setAttType(String attType) {
        this.attType = attType;
    }

}