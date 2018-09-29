/*
 * Copyright 2010-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oa.spring.control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;



/**
 * @author Roy Clarkson
 */
@XmlRootElement(name = "message")
public class Message {
    private long id;

    private String subject;

    private String text;
	private List<State> stateList=new ArrayList();

    public List getStateList() {
		return stateList;
	}

	public void setStateList(List<State> stateList) {
		this.stateList = stateList;
	}

	public Message() {
    }

    public Message(long id, String subject, String text) {
        this.id = id;
        this.subject = subject;
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String toString() {
   
    	for(State a:this.stateList){
    		System.out.println(a.getName());
    	}
    	
        return "Id:[" + this.getId() + "] Subject:[" + this.getSubject() + "] Text:[" + this.getText() + "]";
        
    }
}
