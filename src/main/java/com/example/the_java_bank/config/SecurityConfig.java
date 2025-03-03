
package com.example.the_java_bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.the_java_bank.service.impl.UserService;

@Configuration
public class SecurityConfig {

    private final UserService userService;
    private final PreFilter preFilter;

    public SecurityConfig(UserService userService, PreFilter preFilter) {
        this.userService = userService;
        this.preFilter = preFilter;
    }

    private String[] WHITE_LIST = { "api/auth/**", "api/user/**" };

    /*
     *
     * thiet lap cors cho cac domail ma muon xu dungj api backen
     *
     * @Bean
     * public WebMvcConfigurer webMvcConfigurer() {
     *
     * return new WebMvcConfigurer() {
     *
     * @Override
     * public void addCorsMappings(CorsRegistry registry) {
     * registry.addMapping("**")
     * .allowedOrigins("http://localhost:")
     * .allowedMethods("GET", "POST", "PUT", "DELETE")
     * .allowedHeaders("*")
     * .allowCredentials(false)
     * .maxAge(3600);
     * }
     * };
     * }
     *
     */

    // ma hoa password
    @Bean
    public PasswordEncoder getPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // thiet lap STATELESS(khong luu token o phia sever)
    // thiet lap WHITE_LIST cho phep user request ma khong can token
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
            throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests.requestMatchers(WHITE_LIST).permitAll().anyRequest()
                                .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // thiet lap cho phep bo qua request cac web chi dinh ben duoi
    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    // return weSecurity -> weSecurity.ignoring()
    // .requestMatchers("/actuator/**", "/v3/**", "/webjars/**",
    // "/swagger-ui*/*swagger-initializer.js",
    // "/swagger-ui*/**");
    // }

    // quan ly cac user truy cap
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    // Cho phep truy van vao database thong qua UserDeatil
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userService.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());

        return daoAuthenticationProvider;
    }

}
