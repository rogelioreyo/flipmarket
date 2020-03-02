package mx.com.agurno.flipmarket.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import mx.com.agurno.flipmarket.security.JwtTokenFilterConfigurer;
import mx.com.agurno.flipmarket.security.JwtTokenProvider;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	/** The jwt token provider. */
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Configure global.
	 *
	 * @param auth
	 *            the auth
	 * @throws Exception
	 *             the exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(jwtTokenProvider);
	}

	/**
	 * Configure.
	 *
	 * @param http
	 *            the http
	 * @throws Exception
	 *             the exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests()
				.antMatchers("/**/recover").permitAll()
				.antMatchers("/DomainVerification.html").permitAll()
			    .antMatchers("/v1/its/items/my").hasAnyAuthority("ROLE_DEVELOPER", "ROLE_ADMIN", "ROLE_CONFIGURATIONMANAGER")
				.anyRequest().authenticated();
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
	}

	/**
	 * Configure.
	 *
	 * @param web
	 *            the web
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs")
				.antMatchers("/swagger-resources/**")
				.antMatchers("/swagger-ui.html")
				.antMatchers("/DomainVerification.html")
				.antMatchers("/**");
	}

	/**
	 * Password encoder.
	 *
	 * @return the password encoder
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	/**
	 * Authentication manager bean.
	 *
	 * @return the authentication manager
	 * @throws Exception
	 *             the exception
	 */
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
