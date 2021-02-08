package br.radixeng.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.radixeng.jwt.filter.JWTAuthenticationFilter;
import br.radixeng.jwt.filter.JWTLoginFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringFoxConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
		
		
		httpSecurity.csrf().disable().authorizeRequests()
			.antMatchers("/").permitAll()
			.and().headers().frameOptions().disable()
			.and()
			.authorizeRequests()
				.antMatchers("/js/**", "/*.ico", "/distanciaMinima**", "/register", "/register/**", "/console/**", "/swagger**/**", "/webjars/**", "/v2/**", "/error**").permitAll()
				.antMatchers("/logingraph").permitAll()
			.anyRequest().authenticated()
			.and()
			
			// filtra requisições de login
			.addFilterBefore(new JWTLoginFilter("/logingraph", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
			
			// filtra outras requisições para verificar a presença do JWT no header
			.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// cria uma conta default
		auth.inMemoryAuthentication()
			.withUser("radix")
			.password("{noop}radix")
			.roles("ADMIN");
	}
}
