package com.hepsiemlak.todo.repository;

import com.hepsiemlak.todo.model.Role;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends CouchbaseRepository<Role, String> {
    List<Role> findByRoleName(String roleName);
}
