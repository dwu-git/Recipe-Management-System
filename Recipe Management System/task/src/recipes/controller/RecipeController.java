package recipes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.ID;
import recipes.model.Recipe;
import recipes.service.RecipeService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService service) {
        this.recipeService = service;
    }

    @GetMapping ("recipe/{id}")
    public Recipe getRecipe(@PathVariable @Min(1) Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping("recipe/new")
    public ID saveRecipe(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Recipe recipe) {
        return new ID(recipeService.saveRecipe(userDetails, recipe).getId());
    }

    @PutMapping("recipe/{id}")
    public ResponseEntity<Recipe> updateRecipe(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody Recipe recipe, @PathVariable @Min(1) Long id) {
        if (!userDetails.getUsername().equals(recipeService.getRecipe(id).getUser().getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        recipeService.updateRecipe(recipe, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping ("recipe/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@AuthenticationPrincipal UserDetails userDetails, @PathVariable @Min(1) Long id) {
        if (!userDetails.getUsername().equals(recipeService.getRecipe(id).getUser().getEmail()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("recipe/search")
    public List<Recipe> findRecipe(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if (category != null && name != null || category == null && name == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        else if (category != null)
            return recipeService.findRecipeByCategory(category);
        else
            return recipeService.findRecipeByName(name);
    }
}
