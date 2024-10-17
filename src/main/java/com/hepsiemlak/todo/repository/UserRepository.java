package com.hepsiemlak.todo.repository;

import com.hepsiemlak.todo.model.User;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@N1qlPrimaryIndexed
@ViewIndexed(designDoc = "user",viewName = "all")
public interface UserRepository extends CouchbaseRepository<User, String> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
