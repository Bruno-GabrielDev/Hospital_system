package br.ifsp.hospital.suite;

import org.junit.platform.suite.api.*;


@Suite
@SuiteDisplayName("Suíte Unitária – Todos os testes @UnitTest")
@SelectPackages("br.ifsp.hospital")
@IncludeTags("UnitTest")
public class UnitTestSuite {
}

