package com.hepsiemlak.todo.controller;

import com.hepsiemlak.todo.dto.request.TodoRequest;
import com.hepsiemlak.todo.model.Todo;
import com.hepsiemlak.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo-list")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // Authenticated kullanıcıya bir yapılacak öğesi ekle
    @PostMapping("/{todoListId}/todo")
    public void save(@PathVariable String todoListId, @RequestBody TodoRequest todoRequest, Authentication authentication){
        String username = authentication.getName();
        todoService.saveTodo(todoListId, todoRequest, username);
    }

    // Authenticated kullanıcıya ait bir yapılacak öğesini işaretle (check)
    @PostMapping("/todo/{id}/check")
    public void check(@PathVariable String id, Authentication authentication){
        String username = authentication.getName();
        todoService.checkTodo(id, username);
    }

    // Authenticated kullanıcının belirli bir listeye ait yapılacak öğelerini getir
    @GetMapping("/{todoListId}/todo")
    public List<Todo> getAll(@PathVariable String todoListId, Authentication authentication){
        String username = authentication.getName();
        return todoService.getTodos(todoListId, username);
    }

    // Authenticated kullanıcının belirli bir yapılacak öğesini güncelle
    @PutMapping("/todo/{id}")
    public Todo update(@PathVariable String id, @RequestBody TodoRequest todoRequest, Authentication authentication){
        String username = authentication.getName();
        return todoService.updateTodo(id, todoRequest, username);
    }

    // Authenticated kullanıcıya ait bir yapılacak öğesini sil
    @DeleteMapping("/todo/{id}")
    public void delete(@PathVariable String id, Authentication authentication){
        String username = authentication.getName();
        todoService.deleteTodoById(id, username);
    }
}
