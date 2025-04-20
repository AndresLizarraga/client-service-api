package com.pinapp.clientservice.testdummy;

import com.pinapp.clientservice.util.ControllerUtils;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Hidden
@RestController
@RequestMapping("/test")
public class DummyTestController {
    @GetMapping
    public ResponseEntity<String> test() {
        URI uri = ControllerUtils.buildResourceLocation(123L);
        return ResponseEntity.created(uri).body("ok");
    }
}
