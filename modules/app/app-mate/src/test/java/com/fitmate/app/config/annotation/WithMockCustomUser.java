package com.fitmate.app.config.annotation;

import com.fitmate.app.config.factory.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class, setupBefore = TestExecutionEvent.TEST_EXECUTION)
public @interface WithMockCustomUser {

    String id() default "1";
    String username() default "abcd";

}
