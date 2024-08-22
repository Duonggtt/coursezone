package com.learn.coursezone.web.view;

import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyhttp.server.core.view.View;
import org.youngmonkeys.ecommerce.model.ShopDetailsModel;
import org.youngmonkeys.ecommerce.service.ShopDetailsService;
import org.youngmonkeys.ezyplatform.web.view.WebViewDecorator;

import javax.servlet.http.HttpServletRequest;

@EzySingleton
public class CourseZoneWebViewDecorator extends WebViewDecorator {
    private final ShopDetailsService shopDetailsService;

    public CourseZoneWebViewDecorator(ShopDetailsService shopDetailsService) {
        this.shopDetailsService = shopDetailsService;
    }

    @Override
    public void decorate(HttpServletRequest request, View view) {
        super.decorate(request, view);
        ShopDetailsModel shopDetailsModel
            = this.shopDetailsService.getDefaultActivatedShopDetails();
        view.setVariable("shopDetail", shopDetailsModel);
    }
}
