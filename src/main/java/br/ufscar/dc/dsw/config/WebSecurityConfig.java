package br.ufscar.dc.dsw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Permit static resources without authentication
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/image/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/home").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/materiais/listar").permitAll() // R5: Listagem de materiais disponíveis (não requer login)
                .requestMatchers("/estudantes/**").hasRole("ADMIN") // R1: CRUD de estudantes (requer login de administrador)
                .requestMatchers("/materiais/cadastrar").hasRole("USER") // R4: Cadastro de materiais (requer login de estudante)
                .requestMatchers("/materiais/salvar").hasRole("USER")
                .requestMatchers("/materiais/editar/**").hasRole("USER")
                .requestMatchers("/materiais/excluir/**").hasRole("USER")
                .requestMatchers("/materiais/meus-materiais").hasRole("USER") // R7: Listagem dos materiais que o aluno colocou para empréstimo
                .requestMatchers("/emprestimos/solicitar/**").hasRole("USER") // R6: Solicitação de empréstimo (requer login de estudante)
                .requestMatchers("/emprestimos/salvarSolicitacao").hasRole("USER")
                .requestMatchers("/emprestimos/meus-emprestimos").hasRole("USER") // R8: Listagem dos empréstimos de um estudante (requer login do estudante)
                .requestMatchers("/emprestimos/solicitacoes-para-meus-materiais").hasRole("USER") // R9: Gerenciamento de solicitações (requer login do estudante doador)
                .requestMatchers("/emprestimos/aprovar/**").hasRole("USER") // R9: Aprovar/Recusar (requer login do estudante doador)
                .requestMatchers("/emprestimos/recusar/**").hasRole("USER")
                .requestMatchers("/emprestimos/iniciar/**").hasRole("USER")
                .requestMatchers("/emprestimos/concluir/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();
        UserDetails user = User.withUsername("lorena@gmail.com")
            .password(passwordEncoder().encode("123abc"))
            .roles("USER")
            .build();
        UserDetails user2 = User.withUsername("luis@gmail.com")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(admin, user, user2);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}