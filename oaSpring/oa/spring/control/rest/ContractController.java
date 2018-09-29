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

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oa.spring.service.ContractService;
import oa.spring.service.FinanceService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHBeanKeys;

@Controller
@RequestMapping("/contract")
public class ContractController {
	@Autowired
	private ContractService contractService;
	@Autowired
	private FinanceService financeService;

	private static final Logger logger = Logger
			.getLogger(ContractController.class);

	@RequestMapping(value = "addContract", method = RequestMethod.POST)
	public @ResponseBody
	String addContract(HttpServletRequest request, HttpServletResponse response) {
		String str = "0";
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String remark = request.getParameter("remark");
		String attachmentId = request.getParameter("attachmentId");
		String attachmentName = request.getParameter("attachmentName");
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("name", name);
		map.put("type", type);
		map.put("remark", remark);
		map.put("attachment_id", attachmentId);
		map.put("attachment_name", attachmentName);
		try {
			contractService.addContract(map);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "updateContract", method = RequestMethod.POST)
	public @ResponseBody
	String updateContract(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "0";
		String conId = request.getParameter("conId");
		String code = request.getParameter("code");
		String name = request.getParameter("name");
		String type = request.getParameter("type");
		String remark = request.getParameter("remark");
		String attachmentId = request.getParameter("attachmentId");
		String attachmentName = request.getParameter("attachmentName");
		Map<String, String> map = new HashMap<String, String>();
		map.put("conId", conId);
		map.put("code", code);
		map.put("name", name);
		map.put("type", type);
		map.put("remark", remark);
		map.put("attachment_id", attachmentId);
		map.put("attachment_name", attachmentName);
		try {
			contractService.updateContract(map);
		} catch (Exception e) {
			str = "1";
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "contractManage/{timestamp}", method = RequestMethod.POST)
	public @ResponseBody
	String contractManage(HttpServletRequest request,
			HttpServletResponse response) {
		Connection dbConn = null;
		String str = "";
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request
					.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			String sql = "select id,code,name,type,remark,attachment_id,attachment_name from erp_contract";
			logger.debug(sql);
			str = new String(financeService.yhPageDataJson(dbConn,
					request.getParameterMap(), sql).getBytes("UTF-8"),
					"ISO-8859-1");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	@RequestMapping(value = "getContract", method = RequestMethod.POST)
	public @ResponseBody
	Map getContract(HttpServletRequest request, HttpServletResponse response) {
		Connection dbConn = null;
		String conId = request.getParameter("conId");
		Map map = contractService.getContract(conId);
		return map;
	}

	@RequestMapping(value = "deleteContract", method = RequestMethod.POST)
	public @ResponseBody
	String deleteContract(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "0";
		String conId = request.getParameter("conId");
		try {
			contractService.deleteContract(conId);
		} catch (Exception e) {
			e.printStackTrace();
			str = "1";
		}
		return str;
	}

}
