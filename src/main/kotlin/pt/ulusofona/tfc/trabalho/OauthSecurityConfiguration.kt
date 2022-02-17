package pt.ulusofona.tfc.trabalho

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
//import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority
//import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import java.util.*

// uncomment this for orcid authentication
// @Configuration
class OauthSecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
        //                .antMatchers("/**").authenticated() // Block this
            //                .antMatchers("/**", "/Intranet**").permitAll() // Allow this for all
            .anyRequest().authenticated()
            .and().logout().logoutSuccessUrl("/").permitAll()
            .and()
            .oauth2Login()
                .userInfoEndpoint()
                    .userAuthoritiesMapper(userAuthoritiesMapper())
    }

    private fun userAuthoritiesMapper(): GrantedAuthoritiesMapper {
        return GrantedAuthoritiesMapper { authorities: Collection<GrantedAuthority?> ->
            val mappedAuthorities: MutableSet<GrantedAuthority> = HashSet()
            // uncomment this for orcid authentication
//            authorities.forEach { authority ->
//                if (authority is OidcUserAuthority) {
//                    val oidcUserAuthority = authority
//                    throw RuntimeException("Shouldn't be here")
//
//                } else if (authority is OAuth2UserAuthority) {
//
//                    val oauth2UserAuthority = authority
//                    val userAttributes = oauth2UserAuthority.attributes
//
//                    // TODO: Ir buscar a um ficheiro os id's dos admins
//                    if (userAttributes["id"] == "https://sandbox.orcid.org/0000-0003-2187-5116") {
//                        mappedAuthorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
//                    }
//                    mappedAuthorities.add(SimpleGrantedAuthority("ROLE_USER"))
//                }
//            }
            mappedAuthorities
        }
    }
}