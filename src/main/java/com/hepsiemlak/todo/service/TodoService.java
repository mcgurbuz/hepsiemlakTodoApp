package com.hepsiemlak.todo.service;

import com.hepsiemlak.todo.dto.request.TodoRequest;
import com.hepsiemlak.todo.exception.ResourceNotFoundException;
import com.hepsiemlak.todo.model.Todo;
import com.hepsiemlak.todo.model.TodoList;
import com.hepsiemlak.todo.model.User;
import com.hepsiemlak.todo.repository.TodoListRepository;
import com.hepsiemlak.todo.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoListRepository todoListRepository;
    private final UserService userService;

    // Authenticated kullanıcının belirli bir listeye ait yapılacak öğelerini getir
    public List<Todo> getTodos(String todoListId, String username) {
        User user = userService.getOneUserByUsername(username);
        return todoRepository.findByTodoListIdAndUserId(todoListId, user.getId());
    }

    // Authenticated kullanıcıya bir yapılacak öğesi ekle
    public void saveTodo(String todoListId, TodoRequest todoRequest, String username) {
        User user = userService.getOneUserByUsername(username);
        TodoList todoList = todoListRepository.findByIdAndUserId(todoListId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("TodoList not found for this user."));

        Todo todo = new Todo();
        todo.setId(UUID.randomUUID().toString());
        todo.setTodoListId(todoList.getId());
        todo.setUserId(user.getId());
        todo.setTodo(todoRequest.getTodo());
        todo.setCheck(false);
        todo.setCreateDate(Instant.now());
        todoRepository.save(todo);
    }

    // Authenticated kullanıcıya ait bir yapılacak öğesini güncelle
    public Todo updateTodo(String id, TodoRequest todoRequest, String username) {
        User user = userService.getOneUserByUsername(username);
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found for this user."));

        todo.setTodo(todoRequest.getTodo());
        todo.setUpdateDate(Instant.now());
        return todoRepository.save(todo);
    }

    // Authenticated kullanıcıya ait bir yapılacak öğesini sil
    public void deleteTodoById(String id, String username) {
        User user = userService.getOneUserByUsername(username);
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found for this user."));

        todoRepository.delete(todo);
    }

    // Authenticated kullanıcıya ait bir yapılacak öğesini işaretle (check)
    public void checkTodo(String id, String username) {
        User user = userService.getOneUserByUsername(username);
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found for this user."));

        todo.setCheck(!todo.getCheck());
        todoRepository.save(todo);
    }
}
