package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	@RequestMapping("/page/{page}")
	public String showPage(@PathVariable String page,Model model,String redirect){
		model.addAttribute("redirect", redirect);
		return page;
	}
}
