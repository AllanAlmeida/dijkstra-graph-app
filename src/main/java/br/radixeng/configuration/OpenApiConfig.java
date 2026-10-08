package br.radixeng.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Documentação da API, exposta em /swagger-ui.html.
 *
 * Substitui a antiga SwaggerConfiguration baseada em springfox, que foi
 * abandonada em 2020 e carregava o CVE-2019-17495.
 */
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI graphOpenApi() {

		return new OpenAPI().info(new Info()
				.title("Dijkstra Graph API")
				.description("Menor caminho e rotas possíveis entre dois vértices de um grafo")
				.version("1.0.0")
				.contact(new Contact()
						.name("Allan Almeida")
						.url("https://www.linkedin.com/in/allan-almeida-33014275/"))
				.license(new License()
						.name("Apache 2.0")
						.url("https://www.apache.org/licenses/LICENSE-2.0.html")));
	}
}
