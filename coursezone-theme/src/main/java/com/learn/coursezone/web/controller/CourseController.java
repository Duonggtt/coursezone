package com.learn.coursezone.web.controller;

import com.learn.coursezone.web.pagination.CourseZoneEClassFilter;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestParam;
import com.tvd12.ezyhttp.server.core.view.View;
import lombok.AllArgsConstructor;
import org.youngmonkeys.ecommerce.model.ProductCategoryModel;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.service.ProductCategoryService;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.entity.EClassStatus;
import org.youngmonkeys.elearning.model.EClassModel;
import org.youngmonkeys.elearning.pagination.EClassFilter;
import org.youngmonkeys.elearning.web.controller.decorator.WebEClassModelDecorator;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.elearning.web.response.WebEClassWithDescriptionResponse;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.util.HttpRequests;
import org.youngmonkeys.ezyplatform.util.Keywords;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.validator.WebCommonValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@AllArgsConstructor
public class CourseController {

    private final WebProductCurrencyService productCurrencyService;
    private final WebEClassControllerService eclassControllerService;
    private final WebEClassModelDecorator eclassModelDecorator;
    private final WebCommonValidator webCommonValidator;
    private final ProductCategoryService productCategoryService;

    @DoGet("/courses")
    public View classesGet(
        HttpServletRequest request,
        @UserId Long userId,
        @RequestParam("categoryIds") Set<Long> categoryIds,
        @RequestParam("keyword") String keyword,
        @RequestParam("nextPageToken") String nextPageToken,
        @RequestParam("prevPageToken") String prevPageToken,
        @RequestParam("lastPage") boolean lastPage,
        @RequestParam(value = "limit", defaultValue = "8") int limit
    ) {
        this.webCommonValidator.validatePageSize(limit);
        this.webCommonValidator.validateSearchKeyword(keyword);
        SimpleProductCurrencyModel defaultCurrency =
            this.productCurrencyService.getSimpleDefaultCurrency();
        EClassFilter filter = CourseZoneEClassFilter.builder()
            .statuses(Arrays.asList(EClassStatus.SHOW,
                EClassStatus.REGISTER_OPENED,
                EClassStatus.VIDEO_OPENED))
            .keywords(Keywords.toKeywords(keyword, true))
            .categoryIds(categoryIds != null && !categoryIds.isEmpty() ? categoryIds : null)
            .build();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService
                .getClasses(userId,
                    filter,
                    nextPageToken,
                    prevPageToken,
                    lastPage,
                    limit,
                    defaultCurrency.getId(),
                    defaultCurrency.getFormat());
        List<ProductCategoryModel> categories = this.productCategoryService
            .getProductCategoriesByTypeAndStatuses("CATEGORY", Collections.singletonList("SHOW"));
        int totalPages = (int) Math.ceil((double) pagination.getCount() / limit);
        return View.builder()
            .template("course")
            .addVariable("pagination", pagination.getItems())
            .addVariable("categories", categories)
            .addVariable("selectedCategories", categoryIds)
            .addVariable("paginationCount", pagination.getCount())
            .addVariable("totalPages", totalPages)
            .addVariable("defaultCurrency", defaultCurrency)
            .addVariable("searchKeyword", keyword)
            .addVariable("limit", limit)
            .addVariable("pageTitle", "courses")
            .build();
    }


    @DoGet("/courses/detail/{code}")
    public View classesCodeGet(HttpServletRequest request,
                               @UserId Long userId,
                               @PathVariable String code,
                               @RequestParam("keyword") String keyword,
                               @RequestParam("nextPageToken") String nextPageToken,
                               @RequestParam("prevPageToken") String prevPageToken,
                               @RequestParam("lastPage") boolean lastPage,
                               @RequestParam(value = "limit", defaultValue = "8") int limit) {
        EClassModel model = this.eclassControllerService.getClassModelByCodeOrThrow(code);
        SimpleProductCurrencyModel defaultCurrency
            = this.productCurrencyService.getSimpleDefaultCurrency();
        WebEClassWithDescriptionResponse course
            = this.eclassModelDecorator.decorate(
                model,
                HttpRequests.getLanguage(request),
                defaultCurrency.getId(),
                defaultCurrency.getFormat());
        long courseId = course.getId();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService
            .getPublicClasses(
                userId,
                keyword,
                nextPageToken,
                prevPageToken,
                lastPage,
                limit,
                defaultCurrency.getId(),
                defaultCurrency.getFormat());
        return View.builder()
            .template("course-detail")
            .addVariable("courseId", courseId)
            .addVariable("course", course)
            .addVariable("pagination", pagination.getItems())
            .addVariable("defaultCurrency", defaultCurrency)
            .addVariable("pageTitle", course.getDisplayName())
            .build();
    }

}
