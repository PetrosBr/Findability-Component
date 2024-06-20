package com.mobispaces.findabilitycomponent.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .httpBasic()
        .and()
            .authorizeRequests()
                .antMatchers("/api/dataset").permitAll()
                .antMatchers("/api/codelist/**").permitAll()
                .antMatchers("/api/datasets").permitAll()
                .antMatchers("/api/metadata").permitAll()
				.antMatchers("/api/upload-ttl").permitAll()	
        .and()
            .csrf().disable();
    }
    
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true); // Allow double slashes
        return firewall;
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        super.configure(web);
        web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }
}
