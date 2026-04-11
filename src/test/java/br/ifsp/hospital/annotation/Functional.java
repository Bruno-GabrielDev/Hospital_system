package br.ifsp.hospital.annotation;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

/**
 * Marca um teste criado a partir da Técnica Funcional (caixa-preta).
 * Deve ser usado junto com @UnitTest.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Tag("Functional")
public @interface Functional {
}
