package recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.User;
import recipes.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return new UserAdapter(user);
    }

    public void addUser(User user) {
        Optional<User> foundUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (foundUser.isEmpty()) {
            encodeUserPassword(user);
            userRepository.save(user);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void setUserForNewRecipe(UserDetails userDetails, Recipe recipe) {
        var user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        recipe.setUser(user.get());
    }

    private void encodeUserPassword(User user) {
        user.setPassword(new BCryptPasswordEncoder(9).encode(user.getPassword()));
    }
}
