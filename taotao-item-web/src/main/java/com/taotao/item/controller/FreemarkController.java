package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class FreemarkController {
	@Autowired
	private FreeMarkerConfigurer configurer;
	
	/*@RequestMapping("/GenHtml")
	@ResponseBody
	public String  GenHtml() throws Exception{
		// 1、从spring容器中获得FreeMarkerConfigurer对象。
		// 2、从FreeMarkerConfigurer对象中获得Configuration对象。
		Configuration configuration = configurer.getConfiguration();
		//3使用configuration获得template对象
		Template template = configuration.getTemplate("template.ftl");
		Map dataModel = new HashMap<>();
		dataModel.put("hello", "1000");
		// 5、创建输出文件的Writer对象。
		Writer out = new FileWriter(new File("F:/学习笔记/freemark/hello.html"));
		// 6、调用模板对象的process方法，生成文件。
		template.process(dataModel, out);

		return "ok";
		
	}*/
}
