package market.gui;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import market.interfaces.Searchable;

//Class implementing SearchBox in the table Views
public class TableFilter {

    /**
     * Add Listener to the TextField that can filter any objects that implements
     * Searchable interface
     * 
     * @param filterField  The text we use to filter
     * @param filteredData The data we are to filter
     */
    public <T extends Searchable> void searchableObjectsFilter(TextField filterField, FilteredList<T> filteredData) {
        // FilteredList is a cool Collection when we can set predicates to all elements
        // to show them or not.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(object -> {

                // If filter text is empty every predicate is set to True -> Everything is
                // displayed
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // If not we take the string from the TextField and just compare will element in
                // the row
                String lowerCaseNewValue = newValue.toLowerCase();
                if (object.getSearchString().toLowerCase().indexOf(lowerCaseNewValue) != -1) {
                    return true;
                } else {
                    return false;
                }
            });
        });
    }
}
