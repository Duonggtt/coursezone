package com.learn.coursezone.web.controller;

import com.tvd12.ezyhttp.server.core.annotation.*;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.pagination.DefaultEClassFilter;
import org.youngmonkeys.elearning.pagination.EClassFilter;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.util.Keywords;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.validator.WebCommonValidator;

import com.tvd12.ezyhttp.server.core.view.View;

import java.util.Collections;

@Controller("/account")
public class AccountController {
    private final WebProductCurrencyService productCurrencyService;
    private final WebEClassControllerService eclassControllerService;
    private final WebCommonValidator webCommonValidator;

    private static final String VIEW_VARIABLE_PAGE_TITLE = "My Classes";

    @DoGet("/classes")
    public View classesGet(
        @UserId long userId,
        @RequestParam(value = "keyword") String keyword,
        @RequestParam(value = "nextPageToken") String nextPageToken,
        @RequestParam(value = "prevPageToken") String prevPageToken,
        @RequestParam(value = "lastPage", defaultValue = "false") boolean lastPage,
        @RequestParam(value = "limit", defaultValue = "12") int limit
    ) {
        this.webCommonValidator.validatePageSize(limit);
        this.webCommonValidator.validateSearchKeyword(keyword);
        EClassFilter filter = DefaultEClassFilter.builder()
            .studentId(userId)
            .keywords(Keywords.toKeywords(keyword, true))
            .build();
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> classes = this.eclassControllerService.getClasses(
            userId, filter, nextPageToken, prevPageToken, lastPage, limit,
            defaultCurrency.getId(), defaultCurrency.getFormat()
        );

        View.Builder viewBuilder = View.builder()
            .template("my-classes")
            .addVariable("searchKeyword", keyword)
            .addVariable(VIEW_VARIABLE_PAGE_TITLE, "My Classes");

        if (classes != null) {
            viewBuilder.addVariable("classes", classes.getItems())
                .addVariable("classesCount", classes.getCount());
        } else {
            viewBuilder.addVariable("classes", Collections.emptyList())
                .addVariable("classesCount", 0)
                .addVariable("message", "Bạn chưa có lớp học nào.");
        }

        return viewBuilder.build();
    }

    public AccountController(WebProductCurrencyService productCurrencyService,
                             WebEClassControllerService eclassControllerService,
                             WebCommonValidator webCommonValidator) {
        this.productCurrencyService = productCurrencyService;
        this.eclassControllerService = eclassControllerService;
        this.webCommonValidator = webCommonValidator;
    }
}