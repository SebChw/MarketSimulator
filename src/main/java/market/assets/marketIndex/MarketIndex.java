package market.assets.marketIndex;

import market.assets.Asset;
import market.exchangeRates.ExchangeRatesProvider;
import market.traders.Company;
import java.util.ArrayList;
import java.util.LinkedList;

public class MarketIndex extends Asset{
    private ArrayList<Company> companies;

    public MarketIndex(String name, String type, ArrayList<Company> companies, String backingAsset, float startingRate) {
        super(name, type, 0, backingAsset, startingRate);
        this.companies = companies;
    }

    public void addToIndex(Company company){

    }
    public void removeFromIndex(Company company){

    }
    public float getIndexValue() {

        return (float) 0.1;
    }

    public ArrayList<Company> getCompanies(){
        return this.companies;
    }
    public void setCompanies(ArrayList<Company> companies){
        this.companies = companies;
    }
    
    public float calculateRatio(){
        float ratio = 0;
        for (Company company : companies) {
            ratio += 1/company.getShareValue();
        }
        return 1/ratio;
    }
    
}
