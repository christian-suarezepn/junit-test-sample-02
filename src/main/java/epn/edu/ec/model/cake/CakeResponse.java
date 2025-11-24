package epn.edu.ec.model.cake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeResponse {

    private long id;
    private String title;
    private String description;   

}
