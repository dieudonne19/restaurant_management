import edu.restaurant.app.dao.operations.IngredientCrudOperations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

public class IngredientOperation {
    IngredientCrudOperations subject = new IngredientCrudOperations();

    @Test
    void get_eggs_available_quantity() {
        var expectedEggs = 80.0;
        var actualEggs = subject.findById(3L).getAvailableQuantity();

        Assertions.assertEquals(expectedEggs, actualEggs);
    }
}
