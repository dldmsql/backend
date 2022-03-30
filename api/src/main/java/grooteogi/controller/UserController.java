package grooteogi.controller;

import grooteogi.domain.User;
import grooteogi.domain.dto.LoginDto;
import grooteogi.domain.dto.TokenDto;
import grooteogi.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/user")
  public List<User> getAllUser() {
    return userService.getAllUser();
  }

  @PostMapping("/test")
  public String testAdmin(@RequestBody LoginDto loginDto){
    userService.testAdmin(loginDto);
    return "어드민 회원 가입 완료";
  }

  @PostMapping( "/login" )
  public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
    return ResponseEntity.ok(userService.login(loginDto));
  }
}
