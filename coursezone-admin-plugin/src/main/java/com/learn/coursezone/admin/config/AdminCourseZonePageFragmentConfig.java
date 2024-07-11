package com.learn.coursezone.admin.config;

import com.learn.coursezone.config.CourseZonePageFragmentConfig;
import com.tvd12.ezyfox.bean.annotation.EzyConfigurationAfter;
import org.youngmonkeys.ezyarticle.sdk.manager.PageFragmentManager;

@EzyConfigurationAfter
public class AdminCourseZonePageFragmentConfig extends CourseZonePageFragmentConfig {

    public AdminCourseZonePageFragmentConfig(PageFragmentManager pageFragmentManager) {
        super(pageFragmentManager);
    }
}
