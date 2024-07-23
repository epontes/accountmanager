package br.com.paymanager.infra.persistence.repository;

import br.com.paymanager.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCustomDetailsRepository extends JpaRepository<User, Long> {
        Optional<User> findUserByAppName(String appName);
}
