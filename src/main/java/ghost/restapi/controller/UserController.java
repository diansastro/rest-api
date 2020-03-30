package ghost.restapi.controller;

import ghost.restapi.domain.User;
import ghost.restapi.exception.ResourceNotFoundException;
import ghost.restapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/user")
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/create-user")
    public User createUser(@Valid @RequestBody User user) {
        Date date = new Date();

        user.setCreateAt(date);
        user.setUpdatedAt(date);
        return userRepository.save(user);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody User userDetails) throws ResourceNotFoundException {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        user.setEmail(userDetails.getEmail());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());
        user.setUpdatedAt(new Date());
        final User updateUser =  userRepository.save(user);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/user/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws Exception {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        userRepository.delete(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/user/delete-all")
    public Map<String, Boolean> deleteAllUser() {
        List<User> user = userRepository.findAll();
        userRepository.deleteAll(user);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
