package com.meijer.demo.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.meijer.demo.security.handler.AccessDeniedHandlerImpl;
import com.meijer.demo.security.handler.AuthenticationEntryPointImpl;
import com.meijer.demo.security.handler.AuthenticationFailureHandlerImpl;
import com.meijer.demo.security.handler.AuthenticationSuccessHandlerImpl;
import com.meijer.demo.security.handler.LogoutSuccessHandlerImpl;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	AuthenticationEntryPointImpl authenticationEntryPointImpl;
	
	@Autowired
	AccessDeniedHandlerImpl accessDeniedHandlerImpl;
	
	@Autowired
	AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
	
	@Autowired
	AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
	
	@Autowired
	LogoutSuccessHandlerImpl logoutSuccessHandlerImpl;
	
	@Autowired
	UserDetailsService userDetailsService;

	@Override
    protected void configure(HttpSecurity http) throws Exception {
//		.csrf()
//    	.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//    	.and()
        http.csrf().disable()
        		.cors() // cors config.
        			.and()
            .authorizeRequests()
            	.antMatchers("/index.html", "/products", "/products/*", "/prescriptions").permitAll()
//                .antMatchers("/index.html", "/products", "/products/*").permitAll()
//                .antMatchers("/orders", "/orders/*").hasAnyRole("USER", "ADMIN")
//                .antMatchers("/users", "/users/*").hasAnyRole("ADMIN")
                .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPointImpl)
                .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl)
                .and()
            .formLogin()
				.permitAll()
				.loginProcessingUrl("/login")
				.successHandler(authenticationSuccessHandlerImpl)
			    .failureHandler(authenticationFailureHandlerImpl)
				.usernameParameter("username").passwordParameter("password")
				.and()
			.logout()
				.permitAll()
				.logoutUrl("/logout")
				.logoutSuccessHandler(logoutSuccessHandlerImpl)
				.and()
			.rememberMe();
    }
	
	/*
	 * cors support
	 */
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.addAllowedOrigin("*"); // You should only set trusted site here. e.g. http://localhost:4200 means only this site can access.
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","HEAD","OPTIONS"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder(11);
		return encoder;
	}
	
	/*
	 * in memory user
	 */
//	@Bean
//	public UserDetailsService userDetailsService() {
//	    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//	    manager.createUser(User.withUsername("rshi").password("123456").roles("USER").build());
//	    manager.createUser(User.withUsername("admin").password("123456").roles("USER","ADMIN").build());
//	    return manager;
//	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

}