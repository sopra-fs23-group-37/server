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

  @PostMapping("/logout/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void createLogout(@PathVariable Long id) {
    logoutService.createLogout(id);

  }

}
