package com.x04e.springsecurityldap;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private DefaultSpringSecurityContextSource contextSource = contextSource();
	//private String userSearchBase = "OU=Users,OU=Root";
	private String userSearchBase = "OU=people";
	/* Recursive search for children of a parent OU */
	//private String userSearchFilter = "(&(sAMAccountName={0})(memberOf:1.2.840.113556.1.4.1941:=CN=TestAppUserGroup,OU=Groups,OU=Root,DC=hlab,DC=local))";
	/* Direct children of an OU */
	//private String userSearchFilter = "(&(sAMAccountName={0})(memberOf=CN=TestAppUserGroup,OU=Groups,OU=Root,DC=hlab,DC=local))";
	private String userSearchFilter = "(uid={0})";

	@Autowired
	private PersonDao personDao;
	
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/h2-console").permitAll()
				.anyRequest().fullyAuthenticated()
			.and()
			.formLogin()
			.successHandler(loginSuccessHandler())
			.failureHandler(loginFailureHandler())
			// allow h2 console to show without browser errors
			.and().headers().frameOptions().disable()
			.and()
				.csrf()
				.disable();
	}

	private void persistPerson(Person person){
		personDao.save(person);
	}

	private AuthenticationSuccessHandler loginSuccessHandler(){
		return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
			String username = ((LdapUserDetails)authentication.getPrincipal()).getUsername();

			Person p = new Person();
			p.setName(username + new Date());
			System.out.println("Person to be saved: "+ p);
			persistPerson(p);
			System.out.println("Person saved");
		};
	}

	private AuthenticationFailureHandler loginFailureHandler(){
		return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {

				System.out.println("failed login");
		};
	}

	private static DefaultSpringSecurityContextSource contextSource (){
	DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource("ldap://localhost:8389/dc=springframework,dc=org");
		//DefaultSpringSecurityContextSource source = new DefaultSpringSecurityContextSource("ldap://192.168.93.153:389/dc=hlab,dc=local");
		source.setUserDn("uid=ben,ou=people,dc=springframework,dc=org");
		//source.setUserDn("cn=Administrator,cn=Users,dc=hlab,dc=local");
		source.setPassword("benspassword");
		//source.setPassword("Password1");
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
