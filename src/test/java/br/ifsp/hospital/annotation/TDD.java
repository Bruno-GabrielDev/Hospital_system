package br.ifsp.hospital.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

/**
 * Marca um teste criado seguindo a abordagem Test-Driven Development.
 * Deve ser usado junto com @UnitTest.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("TDD")
public @interface TDD {
}
