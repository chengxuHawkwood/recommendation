package com.hawkwood.recommendation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hawkwood.recommendation.dao.IndexNodeDao;

@Controller
public class IndexNodeController {
	@Autowired
	IndexNodeDao indexNodeDao;
	@RequestMapping("/")
	public String test(){
		return null;
	}
}
