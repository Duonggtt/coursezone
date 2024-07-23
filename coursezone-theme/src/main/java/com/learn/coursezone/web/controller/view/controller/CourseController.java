package com.learn.coursezone.web.controller.view.controller;

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
    public View classesCodeGet(HttpServletRequest request, @UserId Long userId, @PathVariable String code) {
        EClassModel model = this.eclassControllerService.getClassModelByCodeOrThrow(code);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        WebEClassWithDescriptionResponse eclass = this.eclassModelDecorator.decorate(model, HttpRequests.getLanguage(request), defaultCurrency.getId(), defaultCurrency.getFormat());
        long classId = eclass.getId();
        boolean userIsTeacher = userId != null && userId == model.getTeacherId();
        Reactive.Multiple var10000 = Reactive.multiple().register("eclassRegistered", () -> {
            return userId != null && this.userRegisteredEClassService.isRegisteredClass(userId, classId);
        }).register("finishedLessons", () -> {
            return userId == null ? 0 : this.eclassLessonService.countFinishedLessonByClassId(classId);
        }).register("cancelledLessons", () -> {
            return userId == null ? 0 : this.eclassLessonService.countCancelledLessonByClassIdAndCancellerId(classId, userId);
        }).register("students", () -> {
            return userIsTeacher ? this.eclassControllerService.getStudentsByClassId(classId) : Collections.emptyList();
        }).register("studentAveragePoint", () -> {
            return userId == null ? BigDecimal.ZERO : this.eclassStudentLessonPointService.getStudentAveragePointByClassIdAndStudentId(classId, userId);
        });
        WebContactMethodService var10002 = this.contactMethodService;
        var10002.getClass();
        var10000 = var10000.register("mainContactMethod", var10002::getTopActivatedContactMethod);
        WebELearningSettingService var10 = this.elearningSettingService;
        var10.getClass();
        return (View)var10000.register("allowRegisterClass", var10::isAllowRegisterClass).blockingGet((it) -> {
            return View.builder()
                .template("course-detail")
                .addVariables(it.valueMap())
                .addVariable("eclass", eclass)
                .addVariable("defaultCurrency", defaultCurrency)
                .addVariable("pageTitle", eclass.getDisplayName())
                .build();
        });
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
