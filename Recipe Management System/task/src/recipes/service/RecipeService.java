package recipes.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;

import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserService userService;

    public RecipeService(RecipeRepository recipeRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Recipe saveRecipe(UserDetails userDetails, Recipe recipe) {
        userService.setUserForNewRecipe(userDetails, recipe);
        return recipeRepository.save(recipe);
    }

    public void updateRecipe(Recipe recipe, Long id) {
        var updatedRecipe = setRecipe(recipe, id);
        recipeRepository.save(updatedRecipe);
    }

    public List<Recipe> findRecipeByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> findRecipeByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.delete(getRecipe(id));
    }

    private Recipe setRecipe(Recipe newRecipe, Long id) {
        var existedRecipe = getRecipe(id);
        existedRecipe.setCategory(newRecipe.getCategory());
        existedRecipe.setName(newRecipe.getName());
        existedRecipe.setDescription(newRecipe.getDescription());
        existedRecipe.setIngredients(newRecipe.getIngredients());
        existedRecipe.setDirections(newRecipe.getDirections());

        return existedRecipe;
    }
}
