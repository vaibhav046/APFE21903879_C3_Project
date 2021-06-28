import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestaurantTest {
    @InjectMocks
    Restaurant res;
    Restaurant restaurant;

    List<Item> items=new ArrayList<>();
    //REFACTOR ALL THE REPEATED LINES OF CODE

    @BeforeAll
    void setup() {

        items.add(new Item("Sweet corn soup", 119));
        items.add(new Item("Vegetable lasagne", 269));

        restaurant=new Restaurant("Amelie's cafe","Chennai",LocalTime.parse("10:30:00"), LocalTime.parse("22:00:00"));
        res=spy(restaurant);
        res.addToMenu("Sweet corn soup", 119);
        res.addToMenu("Vegetable lasagne", 269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        //WRITE UNIT TEST CASE HERE
        //given
        LocalTime currentTime = LocalTime.parse("11:30:00");

        //act
        when(res.getCurrentTime()).thenReturn(currentTime);
        boolean isOpen = res.isRestaurantOpen();

        //assert

        assertEquals(isOpen, true);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        //WRITE UNIT TEST CASE HERE
        //given
        LocalTime currentTime = LocalTime.parse("23:30:00");

        //act
        when(res.getCurrentTime()).thenReturn(currentTime);
        boolean isOpen = res.isRestaurantOpen();

        //assert
        assertEquals(isOpen, false);

    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {

        int initialMenuSize = res.getMenu().size();
        res.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, res.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {

        int initialMenuSize = res.getMenu().size();
        res.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, res.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {

        assertThrows(itemNotFoundException.class,
                () -> res.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //<<<<<<<<<<<<<<<<<<<<<<<ITEMS>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Test
    public void get_total_price_of_all_selected_items_from_menu() {

        //act
        int total = res.orderTotal(res.getMenu());
        int expectedTotal = expectedTotal(res.getMenu());

        //assert
        assertTrue(res.getMenu().size()>0);
        verify(res).orderTotal(argThat(x->{
            assertNotNull(x);
            assertTrue(x.size()>0);
            assertEquals(res.getMenu().size(),x.size());
            return true;
        }));
        assertTrue(total > 0);
        assertEquals(expectedTotal, total);

    }

    private int expectedTotal(List<Item> items) {
        int sum = 0;
        for (Item item : items) {
            sum = sum + item.getPrice();
        }
        return sum;
    }
}