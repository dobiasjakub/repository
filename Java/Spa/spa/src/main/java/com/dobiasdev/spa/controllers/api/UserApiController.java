package com.dobiasdev.spa.controllers.api;

import com.dobiasdev.spa.converters.ConverterManager;
import com.dobiasdev.spa.converters.ConvertingContext;
import com.dobiasdev.spa.converters.NavigationFields;
import com.dobiasdev.spa.persistance.entities.User;
import com.dobiasdev.spa.model.dtos.UserDto;
import com.dobiasdev.spa.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserApiController {

    private final UserService userService;
    private final ConverterManager converterManager;

    protected UserApiController(UserService userService, ConverterManager converterManager) {
        this.userService = userService;
        this.converterManager = converterManager;
    }

    @PostMapping("")
    public long createAccount(@RequestBody UserDto user) {
        return userService.createUser(user);
    }

    @GetMapping("")
    @ResponseBody
    public List<UserDto> getUsers() {
        var users = userService.getUsers();
        var navigation = NavigationFields.create(new String[]{"accounts.*", "accounts.transactions.*"});

        return users.stream().map(u -> converterManager.user().convertToDto(u, navigation, new ConvertingContext())).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    @ResponseBody
    public UserDto getSingleUser(@PathVariable(name = "id") Long id) {
        var user = userService.getUserById(id);
        var navigation = NavigationFields.create(new String[]{"accounts"});
        var context = new ConvertingContext();
        context.setSystem(true);

        return converterManager.user().convertToDto(user, navigation, context);
    }

    @PutMapping
    public long updateUser(@RequestBody UserDto user) {
        return userService.updateUser(user);
    }

    @PostMapping("delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}
