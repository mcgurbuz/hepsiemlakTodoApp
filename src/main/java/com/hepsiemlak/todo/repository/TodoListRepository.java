package com.hepsiemlak.todo.repository;

import com.hepsiemlak.todo.model.TodoList;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoListRepository extends CouchbaseRepository<TodoList, String> {

    // Belirli bir kullanıcıya ait yapılacaklar listelerini getir
    List<TodoList> findByUserId(String userId);

    // Belirli bir kullanıcıya ait spesifik bir yapılacaklar listesini bul
    Optional<TodoList> findByIdAndUserId(String id, String userId);
}
