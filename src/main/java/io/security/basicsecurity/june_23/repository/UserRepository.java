package io.security.basicsecurity.june_23.repository;

import io.security.basicsecurity.june_23.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

    ;

}
