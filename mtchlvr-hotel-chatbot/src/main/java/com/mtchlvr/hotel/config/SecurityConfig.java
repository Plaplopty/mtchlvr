package com.mtchlvr.hotel.config;
import com.mtchlvr.hotel.repo.UserRepository;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*; import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.web.SecurityFilterChain;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder(12);}
 @Bean UserDetailsService userDetailsService(UserRepository users){return email->users.findByEmailIgnoreCase(email).map(u->{if(!u.isVerified())throw new DisabledException("Verify your email first.");return User.withUsername(u.getEmail()).password(u.getPasswordHash()).roles(u.getRole()).build();}).orElseThrow(()->new UsernameNotFoundException("Account not found"));}
 @Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
  http.authorizeHttpRequests(a->a.requestMatchers("/","/rooms","/register","/verify","/login","/css/**","/js/**","/error").permitAll().requestMatchers("/manager/**").hasRole("MANAGER").requestMatchers("/customer/**","/api/chat/**").hasRole("CUSTOMER").anyRequest().authenticated())
   .formLogin(f->f.loginPage("/login").defaultSuccessUrl("/",true).permitAll()).logout(l->l.logoutSuccessUrl("/").permitAll()).csrf(c->c.ignoringRequestMatchers("/api/chat"));
  return http.build();
 }
}