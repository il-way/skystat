package com.ilway.skystat.bootstrap.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Profile("production")
public @interface Production {
}
