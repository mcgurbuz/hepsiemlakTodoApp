package com.hepsiemlak.todo.repository;

import com.hepsiemlak.todo.model.Todo;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository  extends CouchbaseRepository<Todo, String> {
    // Belirli bir to do listesine ve kullanıcıya ait todo'ları getir
    List<Todo> findByTodoListIdAndUserId(String todoListId, String userId);

    // Belirli bir kullanıcıya ait belirli bir to do'yu bul
    Optional<Todo> findByIdAndUserId(String id, String userId);

    // Belirli bir kullanıcıya ait tüm yapılacakları getir
    List<Todo> findAllByUserId(String userId);
}
