import edu.restaurant.app.dao.operations.DishCrudOperations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DishOperation {
    private final DishCrudOperations subject = new DishCrudOperations();

    @Test
    public void get_hot_dog_ingredients_price() {
        var expectedPrice = 5500.0;
        var actualHotDogPrice = subject.findById(1L).getTotalIngredientsCost();

        Assertions.assertEquals(expectedPrice, actualHotDogPrice);
    }
    @Test
    public void get_hot_dog_ingredients_price_at_defined_date() {
        var expectedPrice = 5500.0;
        var actualHotDogPrice = subject.findById(1L).getTotalIngredientsCostAt(LocalDate.parse("2025-03-15"));

        Assertions.assertEquals(expectedPrice, actualHotDogPrice);
    }
    @Test
    public void get_hot_dog_gross_margin() {
        var expectedPrice = 9500.0;
        var actualMargin = subject.findById(1L).getGrossMargin();

        Assertions.assertEquals(expectedPrice, actualMargin);
    }
}
