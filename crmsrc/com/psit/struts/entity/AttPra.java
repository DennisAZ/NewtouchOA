package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 来往记录附件 <br>
 *
 * create_date: Aug 27, 2010,3:13:41 PM<br>
 * @author zjr
 */
public class AttPra extends Attachment implements java.io.Serializable {

    // Fields    
     private SalPra salPra;

    // Constructors

    /** default constructor */
    public AttPra() {
    }
    
    /** full constructor */
    public AttPra(SalPra salPra) {
        this.salPra = salPra;
    }

    // Property accessors

    public SalPra getSalPra() {
        return this.salPra;
    }
    
    public void setSalPra(SalPra salPra) {
        this.salPra = salPra;
    }

}