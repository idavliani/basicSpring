package dln.ioanna.basicspring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class Controller {


    @GetMapping("/test")
    public ResponseEntity<String> getTest() {

        return ResponseEntity.ok().build();
    }
}
