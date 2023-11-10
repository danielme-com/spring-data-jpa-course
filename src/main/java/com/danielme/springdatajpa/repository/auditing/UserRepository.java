package com.danielme.springdatajpa.repository.auditing;

import com.danielme.springdatajpa.model.entity.audit.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

     User save(User user);

     Optional<User> findById(Long id);

}
