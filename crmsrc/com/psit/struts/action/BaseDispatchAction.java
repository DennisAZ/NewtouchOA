package com.psit.struts.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.actions.DispatchAction;

import yh.core.funcs.person.data.YHPerson;

public abstract class BaseDispatchAction extends DispatchAction{
	YHPerson getPerson(HttpServletRequest request)
	{
		YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		return person;
	}
	
	String getPersonName(HttpServletRequest request)
	{
		YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		return person.getNickName();
	}
	long getPersonId(HttpServletRequest request)
	{
		YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		return person.getSeqId();
	}
	/*
	 *     session.setAttribute("LOGIN_USER", person);
    	   session.setAttribute("sessionToken", sessionToken);
           session.setAttribute("LOGIN_IP", ip);
           session.setAttribute("STYLE_INDEX", getStyleIndex(request));
	 * */

}
