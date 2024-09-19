package com.demo.api.demo_api.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.demo_api.helper.ApiResponse;

@RestController
public class NotFoundController {

  @RequestMapping(value = "*", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
      RequestMethod.DELETE })
  public ResponseEntity<ApiResponse> handleNotFound() {
    ApiResponse response = new ApiResponse("error", "Endpoint not found");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }
}
