package br.ifsp.hospital.suite;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Suíte Mutation – Testes criados para matar mutantes")
@SelectPackages("br.ifsp.hospital")
@IncludeTags("Mutation")
public class MutationTestSuite {
}
