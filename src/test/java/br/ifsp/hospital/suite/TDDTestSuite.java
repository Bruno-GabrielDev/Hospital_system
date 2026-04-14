package br.ifsp.hospital.suite;

import org.junit.platform.suite.api.*;


@Suite
@SuiteDisplayName("Suíte TDD – Testes criados com Test-Driven Development")
@SelectPackages("br.ifsp.hospital")
@IncludeTags("TDD")
public class TDDTestSuite {
}
