package com.ilway.skystat.framework.profile;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Profile("default")
public @interface Default {
}
