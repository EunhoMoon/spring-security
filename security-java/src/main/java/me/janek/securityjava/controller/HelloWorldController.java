package me.janek.securityjava.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class HelloWorldController {

    private static final List<Todo> TODOS_LIST = List.of(
        new Todo("Janek", "Learn AWS"),
        new Todo("Janek", "Get AWS SAA"),
        new Todo("Eunho", "Learn Docker")
    );

    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/todos")
    public List<Todo> getAllTodos() {
        return TODOS_LIST;
    }

    @GetMapping("/{username}/todos")
    public List<Todo> getUsersTodos(@PathVariable String username) {
        return TODOS_LIST.stream().filter(todo -> todo.username().equals(username)).toList();
    }

    @PostMapping("/{username}/todos")
    public void saveTodo(
        @PathVariable String username,
        @RequestBody Todo newTodo
    ) {
        log.info("Create {} for {}", newTodo, username);
//        TODOS_LIST.add(newTodo);
    }

}

record Todo(String username, String description) {
}
