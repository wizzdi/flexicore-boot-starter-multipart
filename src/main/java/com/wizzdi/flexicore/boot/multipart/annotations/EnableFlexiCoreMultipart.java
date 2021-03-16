package com.wizzdi.flexicore.boot.multipart.annotations;

import com.wizzdi.flexicore.boot.multipart.init.MultiPartModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(MultiPartModule.class)
@Configuration
public @interface EnableFlexiCoreMultipart {
}
