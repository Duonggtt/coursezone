package com.learn.coursezone.web.controller.view.controller;

import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.view.View;
import lombok.AllArgsConstructor;
import org.youngmonkeys.ezysupport.web.view.SupportViewBuilderDecorator;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;
import static org.youngmonkeys.ezyplatform.util.HttpRequests.getLanguage;

@Controller
@AllArgsConstructor
public class HomeController {

    private final SupportViewBuilderDecorator viewBuilderDecorator;

    @DoGet("/")
    public View home(
        HttpServletRequest request
    ) {
        View.Builder viewBuilder = View.builder()
            .template("home")
            .addVariable(VIEW_VARIABLE_PAGE_TITLE, "home");
        viewBuilderDecorator.addWebBannerImageUrl(viewBuilder);
        String language = getLanguage(request);
        viewBuilderDecorator.addPageFragments(
            viewBuilder,
            language,
            Collections.singletonMap("main_page_heading", "mainHeading")
        );
        return viewBuilder.build();
    }
}
