package br.com.paymanager.domain.service;

import br.com.paymanager.application.security.UserAuthenticated;
import br.com.paymanager.infra.persistence.repository.UserCustomDetailsRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCustomDetailsService implements UserDetailsService {

    private final UserCustomDetailsRepository userCustomDetailsRepository;


    public UserCustomDetailsService(UserCustomDetailsRepository userCustomDetailsRepository) {
        this.userCustomDetailsRepository = userCustomDetailsRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCustomDetailsRepository.findUserByAppName(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário inválido"));
    }
}
