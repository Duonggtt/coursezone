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
import org.youngmonkeys.ezyarticle.sdk.entity.PostStatus;
import org.youngmonkeys.ezyarticle.sdk.entity.PostType;
import org.youngmonkeys.ezyarticle.sdk.pagination.DefaultPostFilter;
import org.youngmonkeys.ezyarticle.web.controller.service.WebPostControllerService;
import org.youngmonkeys.ezyarticle.web.response.WebPostContentResponse;
import org.youngmonkeys.ezyarticle.web.response.WebPostDetailsResponse;
import org.youngmonkeys.ezyarticle.web.response.WebPostResponse;
import org.youngmonkeys.ezyplatform.data.StorageFilter;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.util.Keywords;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.validator.WebCommonValidator;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;
import static org.youngmonkeys.ezyplatform.util.HttpRequests.getLanguage;

@Controller
public class BlogController {

    private final WebPostControllerService postControllerService;
    private final WebEClassControllerService eclassControllerService;
    private final WebCommonValidator webCommonValidator;
    private final WebProductCurrencyService productCurrencyService;
    private final WebProductCategoryControllerService productCategoryControllerService;

    public BlogController(WebPostControllerService postControllerService, WebEClassControllerService eclassControllerService, WebCommonValidator webCommonValidator, WebProductCurrencyService productCurrencyService, WebProductCategoryControllerService productCategoryControllerService) {
        this.postControllerService = postControllerService;
        this.eclassControllerService = eclassControllerService;
        this.webCommonValidator = webCommonValidator;
        this.productCurrencyService = productCurrencyService;
        this.productCategoryControllerService = productCategoryControllerService;
    }

    @DoGet("/blogs")
    public View getBlogs(HttpServletRequest request, @UserId Long userId, @PathVariable String code, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "20") int limit) {
        this.webCommonValidator.validatePageSize(limit);
        this.webCommonValidator.validateSearchKeyword(keyword);
        String language = getLanguage(request);
        DefaultPostFilter filter = DefaultPostFilter.builder()
            .keywords(Keywords.toKeywords(keyword, true))
            .postType(PostType.POST.toString()).postStatus(PostStatus.PUBLISHED.toString())
            .build();
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        PaginationModel<WebPostContentResponse> posts = this.postControllerService.getPostContentPagination(filter, language, nextPageToken, prevPageToken, lastPage, limit);
        for(WebPostContentResponse post : posts.getItems()) {
            System.out.println(post.getAuthor().getDisplayName());
        }
        return View.builder()
                .template("blog")
                .addVariable("posts", posts.getItems())
                .addVariable("postsCount", posts.getCount())
                .addVariable("courses", pagination.getItems())
                .addVariable("searchKeyword", keyword)
                .addVariable(VIEW_VARIABLE_PAGE_TITLE, "blog")
                .build();
    }

    @DoGet("/blogs/detail/{slug}")
    public View getDetail(HttpServletRequest request,@PathVariable String slug, @UserId Long userId, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "6") int limit) {
        String language = getLanguage(request);
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        WebPostDetailsResponse post = this.postControllerService.getPostBySlug(slug, language);
        return View.builder()
            .template("blog-detail")
            .addVariable("courses", pagination.getItems())
            .addVariable("post", post)
            .addVariable(VIEW_VARIABLE_PAGE_TITLE, "blog-detail")
            .build();
    }

}
