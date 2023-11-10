package com.danielme.springdatajpa.repository.auditing;

import com.danielme.springdatajpa.auditing.CustomAuditorAware;
import com.danielme.springdatajpa.model.entity.audit.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testAuditInsertion() {
        User user = new User();
        user.setName("Jane Doe");
        user = userRepository.save(user);

        assertThat(user.getCreatedBy()).isEqualTo(CustomAuditorAware.MOCK_USER);
        assertThat(user.getLastModifiedBy()).isEqualTo(user.getCreatedBy());
        assertThat(user.getLastModifiedDate()).isEqualTo(user.getCreatedDate());
    }

}
