package com.x04e.springsecurityldap;

import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private DefaultSpringSecurityContextSource contextSource = contextSource();
	private String userSearchBase = "OU=Users,OU=Root";
	private String userSearchFilter = "(&(sAMAccountName={0})(memberOf=CN=TestAppUserGroup,OU=Groups,OU=Root,DC=hlab,DC=local))";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.anyRequest().fullyAuthenticated()
			.and()
				.formLogin()
			.and()
				.csrf()
				.disable();
	}

	private static DefaultSpringSecurityContextSource contextSource (){
		DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource("ldap://192.168.93.153:389/dc=hlab,dc=local");
		source.setUserDn("cn=Administrator,cn=Users,dc=hlab,dc=local");
		source.setPassword("Password1");
		source.afterPropertiesSet();
		return source;
	}

	public LdapAuthoritiesPopulator authoritiesPopulator() {
		DefaultLdapAuthoritiesPopulator populator = new DefaultLdapAuthoritiesPopulator(contextSource, userSearchBase);
		populator.setGroupSearchFilter(userSearchFilter);
		populator.setIgnorePartialResultException(true);

		return populator;
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
   //  ActiveDirectoryLdapAuthenticationProvider adProvider = new ActiveDirectoryLdapAuthenticationProvider("hlab.local", "ldap://192.168.93.153:389");
   //  adProvider.setConvertSubErrorCodesToExceptions(true);
   //  adProvider.setUseAuthenticationRequestCredentials(true);
   //  auth.authenticationProvider(adProvider);
		auth
			.ldapAuthentication()
			.ldapAuthoritiesPopulator(authoritiesPopulator())
			.userSearchBase(userSearchBase)
			.userSearchFilter(userSearchFilter)
			.contextSource(contextSource);
				//.url("ldap://192.168.93.153:389/dc=hlab,dc=local")
				//.managerDn("cn=Administrator,cn=Users,dc=hlab,dc=local")
				//.managerPassword("Password1");
	}
}
