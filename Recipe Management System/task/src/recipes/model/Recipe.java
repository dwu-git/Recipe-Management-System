package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recipes")
public class Recipe {
        @JsonIgnore
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private String category;

        @NotBlank
        private String name;

        @NotBlank
        private String description;

        @ElementCollection
        @NotNull
        @Size(min = 1)
        private List<String> ingredients;

        @ElementCollection
        @NotNull
        @Size(min = 1)
        private List<String> directions;

        @UpdateTimestamp
        private LocalDateTime date;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
}
