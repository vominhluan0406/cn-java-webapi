package com.shop.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shop.security.jwt.JwtAuthenticationFilter;
import com.shop.service.imp.CustomUserDetailServiceImp;

@EnableWebSecurity
public class WebSecutiryConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomUserDetailServiceImp userDetailService;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// Get AuthenticationManager bean
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeRequests().antMatchers(HttpMethod.GET, "/product/**", "/order/**", "/cart/**")
				.permitAll().antMatchers(HttpMethod.POST, "/user/verify").permitAll()
				.antMatchers(HttpMethod.GET, "/user/**").authenticated().antMatchers(HttpMethod.POST, "/commnet/add")
				.authenticated().antMatchers(HttpMethod.POST, "/user/add").permitAll()
				.antMatchers(HttpMethod.POST, "/user/forget","/user/send-fp").permitAll()
				.antMatchers(HttpMethod.PUT,"/user/pw").permitAll()
				.antMatchers(HttpMethod.POST, "/product/**", "/img/add").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/order/**", "/user/**", "/cart/**").authenticated()
				.antMatchers(HttpMethod.PUT, "/user/**").authenticated()
				.antMatchers(HttpMethod.PUT, "/product/**", "/order/**", "/cart/**").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/statistic").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/product/**", "/order/**", "/user/**", "/cart/**")
				.hasAuthority("ADMIN").antMatchers("/login").permitAll().anyRequest().permitAll();

		// Xác thực JWT
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		// Thông báo lỗi 403
		http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.getWriter().write("{\"status_code\":403,\"message\":\"Truy cập bị từ chối\"}");
		});

	}

}
