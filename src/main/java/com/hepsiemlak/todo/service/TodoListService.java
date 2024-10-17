package com.hepsiemlak.todo.service;

import com.hepsiemlak.todo.dto.request.TodoListRequest;
import com.hepsiemlak.todo.exception.ResourceNotFoundException;
import com.hepsiemlak.todo.model.TodoList;
import com.hepsiemlak.todo.model.User;
import com.hepsiemlak.todo.repository.TodoListRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserService userService;

    // Authenticated kullanıcıya bir yapılacaklar listesi ekle
    public void saveTodoList(TodoListRequest todoListRequest, String username) {
        User user = userService.getOneUserByUsername(username);
        TodoList todoList = new TodoList();
        todoList.setId(UUID.randomUUID().toString());
        todoList.setUserId(user.getId());
        todoList.setName(todoListRequest.getName());
        todoList.setCreateDate(Instant.now());
        todoListRepository.save(todoList);
    }

    // Authenticated kullanıcıya ait bir yapılacaklar listesini güncelle
    public TodoList updateTodoList(String id, TodoListRequest todoListRequest, String username) {
        User user = userService.getOneUserByUsername(username);
        TodoList todoList = todoListRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(" TodoList not found for this user."));

        todoList.setName(todoListRequest.getName());
        todoList.setUpdateDate(Instant.now());
        return todoListRepository.save(todoList);
    }

    // Authenticated kullanıcının tüm yapılacaklar listesini getir
    public List<TodoList> getTodoListsByUser(String username) {
        User user = userService.getOneUserByUsername(username);
        return todoListRepository.findByUserId(user.getId());
    }

    // Authenticated kullanıcının spesifik bir yapılacaklar listesini getir
    public TodoList getTodoListByIdAndUser(String id, String username) {
        User user = userService.getOneUserByUsername(username);
        return todoListRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("TodoList not found for this user."));
    }

    // Authenticated kullanıcıya ait bir yapılacaklar listesini sil
    public void deleteTodoList(String id, String username) {
        User user = userService.getOneUserByUsername(username);
        TodoList todoList = todoListRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("TodoList not found for this user."));

        todoListRepository.delete(todoList);
    }
}
