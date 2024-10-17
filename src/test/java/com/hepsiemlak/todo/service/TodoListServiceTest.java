package com.hepsiemlak.todo.service;

import com.hepsiemlak.todo.dto.request.TodoListRequest;
import com.hepsiemlak.todo.model.TodoList;
import com.hepsiemlak.todo.model.User;
import com.hepsiemlak.todo.repository.TodoListRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.*;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class TodoListServiceTest {

    @Mock
    private TodoListRepository todoListRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TodoListService todoListService;

    @Test
    public void saveTodoList_should_save_todo_list_for_authenticated_user() {
        // given
        String username = "testUser";
        TodoListRequest todoListRequest = new TodoListRequest();
        todoListRequest.setName("My Todo List");

        User user = new User();
        user.setId("123");
        user.setUsername(username);

        BDDMockito.given(userService.getOneUserByUsername(username)).willReturn(user);

        // when
        todoListService.saveTodoList(todoListRequest, username);

        // then
        ArgumentCaptor<TodoList> savedCaptor = ArgumentCaptor.forClass(TodoList.class);
        Mockito.verify(todoListRepository, times(1)).save(savedCaptor.capture());
        assertThat(savedCaptor.getValue().getName()).isEqualTo(todoListRequest.getName());
        assertThat(savedCaptor.getValue().getUserId()).isEqualTo(user.getId());
        assertThat(savedCaptor.getValue().getCreateDate()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    public void updateTodoList_should_update_todo_list_for_authenticated_user() {
        // given
        String username = "testUser";
        String todoListId = "1";
        TodoListRequest todoListRequest = new TodoListRequest();
        todoListRequest.setName("Updated Todo List");

        User user = new User();
        user.setId("123");
        user.setUsername(username);

        TodoList todoList = new TodoList();
        todoList.setId(todoListId);
        todoList.setUserId(user.getId());
        todoList.setName("Old Todo List"); // Eski isim ile birlikte

        // Mocking userService and repository
        BDDMockito.given(userService.getOneUserByUsername(username)).willReturn(user);
        BDDMockito.given(todoListRepository.findByIdAndUserId(todoListId, user.getId()))
                .willReturn(Optional.of(todoList));

        // Mock save method to return the updated to do list
        BDDMockito.given(todoListRepository.save(Mockito.any(TodoList.class))).willAnswer(invocation -> {
            TodoList savedTodoList = invocation.getArgument(0);
            savedTodoList.setId(todoListId); // ID'yi ayarlayın
            return savedTodoList; // Güncellenmiş listeyi döndür
        });

        // when
        TodoList updatedList = todoListService.updateTodoList(todoListId, todoListRequest, username);

        // then
        assertThat(updatedList).isNotNull(); // updatedList'in null olmadığını kontrol et
        assertThat(updatedList.getName()).isEqualTo(todoListRequest.getName());
        assertThat(updatedList.getUpdateDate()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    public void deleteTodoList_should_delete_todo_list_for_authenticated_user() {
        // given
        String username = "testUser";
        String todoListId = "1";

        User user = new User();
        user.setId("123");
        user.setUsername(username);

        TodoList todoList = new TodoList();
        todoList.setId(todoListId);
        todoList.setUserId(user.getId());

        BDDMockito.given(userService.getOneUserByUsername(username)).willReturn(user);
        BDDMockito.given(todoListRepository.findByIdAndUserId(todoListId, user.getId()))
                .willReturn(Optional.of(todoList));

        // when
        todoListService.deleteTodoList(todoListId, username);

        // then
        Mockito.verify(todoListRepository, times(1)).delete(todoList);
    }
}
