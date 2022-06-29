package com.sahingroup;

import com.sahingroup.ApartApp;
import com.sahingroup.config.EmbeddedMongo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = ApartApp.class)
@EmbeddedMongo
public @interface IntegrationTest {
}
