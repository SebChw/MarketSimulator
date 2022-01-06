package market.gui;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import market.markets.Market;
import market.assets.Asset;
import market.traders.Trader;
public class TableFilter {
    
    public void marketFilter(TextField filterField, FilteredList<Market> filteredData){
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(market -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (market.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getAssetType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getCountry().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (market.getCurrencyName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });
    }
    
    public void assetFilter(TextField filterField, FilteredList<Asset> filteredData){
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(asset -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (asset.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (asset.getType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });
    }

    public void traderFilter(TextField filterField, FilteredList<Trader> filteredData){
        filterField.textProperty().addListener((observable, oldValue, newValue) ->{
            filteredData.setPredicate(trader -> {
                
				//If filter text is empty, display all people
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseNewValue = newValue.toLowerCase();

                if (trader.getName().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (trader.getType().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else if (trader.getTradingIdentifier().toLowerCase().indexOf(lowerCaseNewValue) != -1){
                    return true;
                }
                else{
                    return false;
                }
            });
        });
    }
}
