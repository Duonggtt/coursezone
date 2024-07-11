package com.learn.coursezone.admin.test;

import com.tvd12.ezyhttp.server.boot.EzyHttpApplicationBootstrap;
import com.tvd12.ezyhttp.server.core.annotation.ComponentsScan;
import com.tvd12.ezyhttp.server.core.annotation.PropertiesSources;

@PropertiesSources({
    "config.properties",
    "setup.properties"
})
@ComponentsScan({
    "org.youngmonkeys.ezyplatform",
    "com.learn.coursezone",
    "org.youngmonkeys.ezysupport",
    "org.youngmonkeys.ezymail",
    "org.youngmonkeys.ezyarticle",
    "org.youngmonkeys.elearning",
    "org.youngmonkeys.ecommerce",
    "org.youngmonkeys.ezylogin"
})
public class CourseZoneAdminPluginStartupTest {

    public static void main(String[] args) throws Exception {
        EzyHttpApplicationBootstrap.start(CourseZoneAdminPluginStartupTest.class);
    }
}
