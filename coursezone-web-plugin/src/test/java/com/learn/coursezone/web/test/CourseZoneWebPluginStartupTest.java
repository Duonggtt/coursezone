package com.learn.coursezone.web.test;

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
    "org.youngmonkeys.ezylogin",
    "org.youngmonkeys.ezyaccount"
})
public class CourseZoneWebPluginStartupTest {

    public static void main(String[] args) throws Exception {
        EzyHttpApplicationBootstrap.start(CourseZoneWebPluginStartupTest.class);
    }
}
