package com.feicheng.blog.controller;

import com.feicheng.blog.entity.Techlogy;
import com.feicheng.blog.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * @author Lenovo
 * 技能管理Controller
 */
@Controller
public class TechnologyController {


    @Autowired
    private TechnologyService technologyService;

}
