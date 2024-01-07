package config;


import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSecurity
public class SecurityConfig //extends WebSecurityConfigurerAdapter
{
//
//  @Value("${eureka.username}")
//  private String username;
//  @Value("${eureka.password}")
//  private String password;

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.inMemoryAuthentication()
//      .passwordEncoder(NoOpPasswordEncoder.getInstance())
//      .withUser(username)
//      .password(password).authorities("USER");
//  }
//
//  @Override
//  public void configure(HttpSecurity httpSecurity) throws Exception {
//    httpSecurity.csrf()
//      .disable()
//      .authorizeRequests()
//      .anyRequest()
//      .authenticated()
//      .and()
//      .httpBasic();
//  }
}
