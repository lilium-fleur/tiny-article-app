package com.example.demo.user;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{username}/articles")
@RequiredArgsConstructor
public class UserController {

}
