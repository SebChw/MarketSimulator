package market.assets;

import market.traders.Company;
import market.exchangeRates.ExchangeRatesProvider;
public class Share extends AssetBackedByCurrency{
    //! Generally commodities and Currencies rather doesn't have limits. However Company issue only some shares and only issued can be
    //! Bough no more
    private Company company;
    private float availableShares;
    

    public Share(String name, float amountInCirulation, Company company, Float startingRate) {
        super(name, "Share", amountInCirulation, company.getRegisteredCurrency(), startingRate);
        this.company = company;
        this.availableShares = amountInCirulation;
        
    }
    
    public void increaseShares(float amount){
        this.availableShares += amount;
        this.changeAmountInCirculation(amount);
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued by: " + company.getName();
    }
}
