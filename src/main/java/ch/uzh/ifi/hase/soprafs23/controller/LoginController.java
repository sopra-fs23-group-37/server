package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Login;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LoginGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LoginPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Login Controller
 * This class is responsible for handling all REST request that are related to
 * the login.
 * The controller will receive the request and delegate the execution to the
 * LoginService and finally return the result.
 */
@RestController
public class LoginController {

  private final LoginService loginService;

  LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public LoginGetDTO createLogin(@RequestBody LoginPostDTO loginPostDTO) {
    // convert API login to internal representation
    Login loginInput = DTOMapper.INSTANCE.convertLoginPostDTOtoEntity(loginPostDTO);

    // create login
    Login createdLogin = loginService.createLogin(loginInput);

    // convert internal representation of login back to API
    return DTOMapper.INSTANCE.convertEntityToLoginGetDTO(createdLogin);
  }

}
