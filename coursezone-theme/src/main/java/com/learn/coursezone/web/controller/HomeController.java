package com.learn.coursezone.web.controller;

import com.learn.coursezone.web.generator.PasswordGenerator;
import com.tvd12.ezyhttp.core.exception.HttpBadRequestException;
import com.tvd12.ezyhttp.core.response.ResponseEntity;
import com.tvd12.ezyhttp.server.core.annotation.*;
import com.tvd12.ezyhttp.server.core.view.View;
import lombok.AllArgsConstructor;
import org.youngmonkeys.ecommerce.model.SimpleProductCurrencyModel;
import org.youngmonkeys.ecommerce.web.service.WebProductCurrencyService;
import org.youngmonkeys.elearning.web.controller.service.WebEClassControllerService;
import org.youngmonkeys.elearning.web.response.WebEClassResponse;
import org.youngmonkeys.ezyplatform.model.PaginationModel;
import org.youngmonkeys.ezyplatform.web.annotation.UserId;
import org.youngmonkeys.ezyplatform.web.controller.service.WebUserControllerService;
import org.youngmonkeys.ezyplatform.web.request.RegisterRequest;
import org.youngmonkeys.ezysupport.web.controller.service.WebWhyChooseUsControllerService;
import org.youngmonkeys.ezysupport.web.response.WebWhyChooseUsResponse;
import org.youngmonkeys.ezysupport.web.view.SupportViewBuilderDecorator;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.youngmonkeys.ezyplatform.constant.CommonConstants.VIEW_VARIABLE_PAGE_TITLE;
import static org.youngmonkeys.ezyplatform.util.HttpRequests.getLanguage;

@Controller
@AllArgsConstructor
public class HomeController {

    private final SupportViewBuilderDecorator viewBuilderDecorator;
    private final WebEClassControllerService eclassControllerService;
    private final WebProductCurrencyService productCurrencyService;
    private final WebWhyChooseUsControllerService whyChooseUsControllerService;
    private final WebUserControllerService userControllerService;

    @DoGet("/home")
    public View home(HttpServletRequest request,
                     @UserId Long userId,
                     @RequestParam("keyword") String keyword,
                     @RequestParam("nextPageToken") String nextPageToken,
                     @RequestParam("prevPageToken") String prevPageToken,
                     @RequestParam("lastPage") boolean lastPage,
                     @RequestParam(value = "limit", defaultValue = "4") int limit) {
        SimpleProductCurrencyModel defaultCurrency
            = this.productCurrencyService.getSimpleDefaultCurrency();
        PaginationModel<WebEClassResponse> pagination
            = this.eclassControllerService.getPublicClasses(
                userId,
                keyword,
                nextPageToken,
                prevPageToken,
                lastPage,
                limit,
                defaultCurrency.getId(),
                defaultCurrency.getFormat());
        String language = getLanguage(request);
        int skip = 0;
        int whyChooseUsLimit = 4;
        List<WebWhyChooseUsResponse> whyChooseUsResponses
            = this.whyChooseUsControllerService
            .getWhyChooseUsList(language, skip, whyChooseUsLimit);
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

    @DoPost("/register")
    public ResponseEntity registerUser(
        @RequestParam("username") String username,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone) {
        Map<String, Object> response = new HashMap<>();

        try {
            String password = PasswordGenerator.generateRandomPassword(8);

            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setDisplayName(username);
            registerRequest.setUsername(username);
            registerRequest.setEmail(email);
            registerRequest.setPhoneNumber(phone);
            registerRequest.setPassword(password);

            // Đăng ký người dùng
            userControllerService.registerUser(registerRequest, "active");

            response.put("success", true);
            response.put("message", "Registration successful!");
            return ResponseEntity.ok(response);

        } catch (HttpBadRequestException e) {
            response.put("success", false);
            response.put("errors", e.getCause());
            return ResponseEntity.badRequest(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed due to an unexpected error.");
            return ResponseEntity.ok(ResponseEntity.status(500));
        }
    }



}
