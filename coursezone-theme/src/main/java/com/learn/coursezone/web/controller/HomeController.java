package com.learn.coursezone.web.controller;

import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestParam;
import com.tvd12.ezyhttp.server.core.view.View;
import lombok.AllArgsConstructor;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.model.EClassModel;
import org.youngmonkeys.elearning.web.controller.decorator.WebEClassModelDecorator;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.elearning.web.response.WebEClassWithDescriptionResponse;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.util.HttpRequests;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezysupport.web.controller.service.WebWhyChooseUsControllerService;
import org.youngmonkeys.ezysupport.web.response.WebWhyChooseUsResponse;
import org.youngmonkeys.ezysupport.web.view.SupportViewBuilderDecorator;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;
import static org.youngmonkeys.ezyplatform.util.HttpRequests.getLanguage;

@Controller
@AllArgsConstructor
public class HomeController {

    private final SupportViewBuilderDecorator viewBuilderDecorator;
    private final WebEClassControllerService eclassControllerService;
    private final WebProductCurrencyService productCurrencyService;
    private final WebWhyChooseUsControllerService whyChooseUsControllerService;
    private final WebEClassModelDecorator eclassModelDecorator;

    @DoGet("/")
    public View home(HttpServletRequest request, @UserId Long userId, @RequestParam("keyword") String keyword, @RequestParam("nextPageToken") String nextPageToken, @RequestParam("prevPageToken") String prevPageToken, @RequestParam("lastPage") boolean lastPage, @RequestParam(value = "limit",defaultValue = "6") int limit) {
        SimpleProductCurrencyModel defaultCurrency = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination = this.eclassControllerService.getPublicClasses(userId, keyword, nextPageToken, prevPageToken, lastPage, limit, defaultCurrency.getId(), defaultCurrency.getFormat());
        String language = getLanguage(request);
        int skip = 0;
        int whyChooseUsLimit = 4;
        List<WebWhyChooseUsResponse> whyChooseUsResponses = this.whyChooseUsControllerService.getWhyChooseUsList(language, skip, whyChooseUsLimit);
        View.Builder viewBuilder = View.builder()
            .template("home")
            .addVariable("whyChooseUsList", whyChooseUsResponses)
            .addVariable("pagination", pagination.getItems())
            .addVariable(VIEW_VARIABLE_PAGE_TITLE, "home");
        viewBuilderDecorator.addWebBannerImageUrl(viewBuilder);
        viewBuilderDecorator.addPageFragments(
            viewBuilder,
            language,
            Collections.singletonMap("main_page_heading", "mainHeading")
        );
        return viewBuilder.build();
    }
}
