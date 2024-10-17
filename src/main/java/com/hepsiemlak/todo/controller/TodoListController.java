package com.hepsiemlak.todo.controller;

import com.hepsiemlak.todo.dto.request.TodoListRequest;
import com.hepsiemlak.todo.model.TodoList;
import com.hepsiemlak.todo.service.TodoListService;
import com.hepsiemlak.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todo-list")
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    // Authenticated kullanıcıya bir yapılacaklar listesi ekle
    @PostMapping
    public void save(@RequestBody TodoListRequest todoListRequest, Authentication authentication){
        String username = authentication.getName();
        todoListService.saveTodoList(todoListRequest, username);
    }

    // Authenticated kullanıcıya ait bir yapılacaklar listesini güncelle
    @PutMapping("/{id}")
    public TodoList update(@PathVariable String id, @RequestBody TodoListRequest todoListRequest, Authentication authentication){
        String username = authentication.getName();
        return todoListService.updateTodoList(id, todoListRequest, username);
    }

    // Authenticated kullanıcının tüm yapılacaklar listesini getir
    @GetMapping
    public List<TodoList> getAll(Authentication authentication){
        String username = authentication.getName(); // JWT'den alınan kullanıcı adı
        return todoListService.getTodoListsByUser(username);
    }

    // Authenticated kullanıcının bir yapılacaklar listesini getir
    @GetMapping("/{id}")
    public TodoList get(@PathVariable String id, Authentication authentication){
        String username = authentication.getName();
        return todoListService.getTodoListByIdAndUser(id, username);
    }

    // Authenticated kullanıcıya ait bir yapılacaklar listesini sil
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id, Authentication authentication){
        String username = authentication.getName();
        todoListService.deleteTodoList(id, username);
    }
}
