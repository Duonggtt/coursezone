package com.learn.coursezone.web.controller;

import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestParam;
import com.tvd12.ezyhttp.server.core.view.View;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.web.controller.service.WebProductCategoryControllerService;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.model.EClassModel;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.validator.WebCommonValidator;

import javax.servlet.http.HttpServletRequest;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;

@Controller
public class BlogController {

    private final WebEClassControllerService eclassControllerService;
    private final WebCommonValidator webCommonValidator;
    private final WebProductCurrencyService productCurrencyService;
    private final WebProductCategoryControllerService productCategoryControllerService;

    public BlogController(WebEClassControllerService eclassControllerService, WebCommonValidator webCommonValidator, WebProductCurrencyService productCurrencyService, WebProductCategoryControllerService productCategoryControllerService) {
        this.eclassControllerService = eclassControllerService;
        this.webCommonValidator = webCommonValidator;
        this.productCurrencyService = productCurrencyService;
        this.productCategoryControllerService = productCategoryControllerService;
    }

    @DoGet("/blog")
    public View getBlogs(HttpServletRequest request, @UserId Long userId, @PathVariable String code, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "6") int limit) {
        this.webCommonValidator.validatePageSize(limit);
        this.webCommonValidator.validateSearchKeyword(keyword);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        return View.builder()
                .template("blog")
                .addVariable("courses", pagination.getItems())
                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "blog")
                .build();
    }

    @DoGet("/blog/detail/{code}")
    public View getDetail(HttpServletRequest request, @UserId Long userId, @PathVariable String code, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "6") int limit) {
        EClassModel model = this.eclassControllerService.getClassModelByCodeOrThrow(code);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        return View.builder()
            .template("blog-detail")
            .addVariable("courses", pagination.getItems())
            .addVariable(VIEW_VARIABLE_PAGE_TITLE, "blog")
            .build();
    }

}
