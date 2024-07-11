package com.learn.coursezone.web.controller.view.controller;

import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.view.View;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;

@Controller
public class CourseController {
    @DoGet("/course")
    public View home() {
        return View.builder()
                .template("course")
                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "course")
                .build();
    }

    @DoGet("/course/detail")
    public View courseDetail() {
        return View.builder()
                .template("course-detail")
                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "course")
                .build();
    }
}
