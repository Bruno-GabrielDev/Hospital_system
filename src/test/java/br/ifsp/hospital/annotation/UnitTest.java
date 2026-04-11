package br.ifsp.hospital.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

/**
 * Marca um teste como teste de unidade.
 * Usado em conjunto com @TDD ou @Functional.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("UnitTest")
public @interface UnitTest {
}
