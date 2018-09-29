package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 消息附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:12:45 PM<br>
 * @author zjr
 */
public class AttNews extends Attachment implements java.io.Serializable {
    // Fields    
     private News news;

    // Constructors

    /** default constructor */
    public AttNews() {
    }
    
    /** full constructor */
    public AttNews(News news) {
        this.news = news;
    }

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
   
    // Property accessors
}