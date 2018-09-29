package oa.spring.control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import oa.spring.po.OrderProductOut;
import oa.spring.po.PoPro;
import oa.spring.po.Product;
import oa.spring.service.PoProService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/poPro")
public class PoProController {

	@Autowired
	private PoProService poProService;

	private static final Logger logger = Logger
			.getLogger(PoProController.class);


}
