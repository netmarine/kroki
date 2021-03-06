package io.kroki.server.service;

import io.kroki.server.security.SafeMode;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class C4PlantumlServiceTest {

  @Test
  void should_include_c4_puml_file_from_classloader() throws IOException {
    String diagram = "@startuml\n" +
      "!include /path/to/c4.puml # comment \n" +
      "@enduml";
    String result = Plantuml.sanitize(diagram, SafeMode.SECURE);
    assertThat(result).contains("https://github.com/plantuml-stdlib/C4-PlantUML");
  }

  @Test
  void should_preserve_stdlib_include() throws IOException {
    String diagram = "@startuml\n" +
      "!include <c4/C4.puml> # comment \n" +
      "@enduml\n";
    String result = Plantuml.sanitize(diagram, SafeMode.SECURE);
    assertThat(result).isEqualTo(diagram);
  }

  @Test
  void should_not_sanitize_in_unsafe_mode() throws IOException {
    String diagram = "@startuml\n" +
      "!include /path/to/file.puml # comment \n" +
      "!include https://foo.bar\n" +
      "  !includeurl   https://foo.bar\n" +
      "@enduml\n";
    String result = Plantuml.sanitize(diagram, SafeMode.UNSAFE);
    assertThat(result).isEqualTo(diagram);
  }
}
