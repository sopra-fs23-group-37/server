package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.service.LogoutService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class LogoutController {

  private final LogoutService logoutService;

  LogoutController(LogoutService logoutService) {
    this.logoutService = logoutService;
  }

  @PutMapping("/logout/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void createLogout(@PathVariable("userId") Long userId) {
    logoutService.createLogout(userId);

  }

}
