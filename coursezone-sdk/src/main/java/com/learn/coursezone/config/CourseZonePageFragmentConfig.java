package com.learn.coursezone.config;

import com.tvd12.ezyfox.bean.EzyBeanConfig;
import lombok.AllArgsConstructor;
import org.youngmonkeys.ezyarticle.sdk.manager.PageFragmentManager;

@AllArgsConstructor
public class CourseZonePageFragmentConfig implements EzyBeanConfig {

    private final PageFragmentManager pageFragmentManager;

    @Override
    public void config() {
        pageFragmentManager.registerFragmentNames(
            "common",
            "popular_courses"
        );
    }
}
