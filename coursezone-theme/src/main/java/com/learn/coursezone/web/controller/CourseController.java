package com.learn.coursezone.web.controller;

import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestParam;
import com.tvd12.ezyhttp.server.core.view.View;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.model.EClassModel;
import org.youngmonkeys.elearning.web.controller.decorator.WebEClassModelDecorator;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.elearning.web.response.WebEClassWithDescriptionResponse;
import org.youngmonkeys.elearning.web.service.WebEClassLessonService;
import org.youngmonkeys.elearning.web.service.WebEClassStudentLessonPointService;
import org.youngmonkeys.elearning.web.service.WebELearningSettingService;
import org.youngmonkeys.elearning.web.service.WebUserRegisteredEClassService;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.rx.Reactive;
import org.youngmonkeys.ezyplatform.util.HttpRequests;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.validator.WebCommonValidator;
import org.youngmonkeys.ezysupport.web.service.WebContactMethodService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;

@Controller
public class CourseController {
//    @DoGet("/course")
//    public View home() {
//        return View.builder()
//                .template("course")
//                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "course")
//                .build();
//    }

//    @DoGet("/course/detail")
//    public View courseDetail() {
//        return View.builder()
//                .template("course-detail")
//                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "course")
//                .build();
//    }

    private final WebContactMethodService contactMethodService;
    private final WebEClassLessonService eclassLessonService;
    private final WebEClassStudentLessonPointService eclassStudentLessonPointService;
    private final WebELearningSettingService elearningSettingService;
    private final WebProductCurrencyService productCurrencyService;
    private final WebUserRegisteredEClassService userRegisteredEClassService;
    private final WebEClassControllerService eclassControllerService;
    private final WebEClassModelDecorator eclassModelDecorator;
    private final WebCommonValidator webCommonValidator;

    @DoGet("/courses")
    public View classesGet(HttpServletRequest request, @UserId Long userId, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "12") int limit) {
        this.webCommonValidator.validatePageSize(limit);
        this.webCommonValidator.validateSearchKeyword(keyword);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        return View.builder()
            .template("course")
            .addVariable("pagination", pagination.getItems())
            .addVariable("paginationCount", pagination.getCount())
            .addVariable("defaultCurrency", defaultCurrency)
            .addVariable("searchKeyword", keyword)
            .addVariable("pageTitle", "courses")
            .build();
    }

    @DoGet("/courses/detail/{code}")
    public View classesCodeGet(HttpServletRequest request, @UserId Long userId, @PathVariable String code,@RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "8") int limit) {
        EClassModel model = this.eclassControllerService.getClassModelByCodeOrThrow(code);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        WebEClassWithDescriptionResponse course = this.eclassModelDecorator.decorate(model, HttpRequests.getLanguage(request), defaultCurrency.getId(), defaultCurrency.getFormat());
        long courseId = course.getId();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        return View.builder()
            .template("course-detail")
            .addVariable("courseId", courseId)
            .addVariable("course", course)
            .addVariable("pagination", pagination.getItems())
            .addVariable("defaultCurrency", defaultCurrency)
            .addVariable("pageTitle", course.getDisplayName())
            .build();
    }

    public CourseController(WebContactMethodService contactMethodService, WebEClassLessonService eclassLessonService, WebEClassStudentLessonPointService eclassStudentLessonPointService, WebELearningSettingService elearningSettingService, WebProductCurrencyService productCurrencyService, WebUserRegisteredEClassService userRegisteredEClassService, WebEClassControllerService eclassControllerService, WebEClassModelDecorator eclassModelDecorator, WebCommonValidator webCommonValidator) {
        this.contactMethodService = contactMethodService;
        this.eclassLessonService = eclassLessonService;
        this.eclassStudentLessonPointService = eclassStudentLessonPointService;
        this.elearningSettingService = elearningSettingService;
        this.productCurrencyService = productCurrencyService;
        this.userRegisteredEClassService = userRegisteredEClassService;
        this.eclassControllerService = eclassControllerService;
        this.eclassModelDecorator = eclassModelDecorator;
        this.webCommonValidator = webCommonValidator;
    }
}
