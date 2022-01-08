package market.assets.marketIndex;

import market.assets.Asset;
import market.entityCreator.SemiRandomValuesGenerator;
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

    public void updateRate(){
        this.getMainBankRates().updateRate(calculateRatio());
    }

    public float getPossibleAmount(){
        System.out.println("Market Amount possible!");
        return 1;
    }

    public float getPossibleAmount(float amount){
        if(amount < 1){
            System.out.println("User ERROR! with index: " + getName() + " " + amount);
            return 0;
        }
        return SemiRandomValuesGenerator.getRandomIntNumber((int)(amount));
    }
    @Override
    public synchronized boolean canBeBought(float amount){
        ArrayList<Company> checkedCompanies = new ArrayList<Company>(companies.size());
        for (Company company : companies) {
            if (!company.getShare().canBeBought(amount)) {
                for (Company unfreezedCompany : checkedCompanies) {
                    unfreezedCompany.getShare().unfreeze(amount);
                }
                return false;
            }
            else checkedCompanies.add(company);
        }
        System.out.println("You can't buy this Index as this would lead to Bigger amountInCirculation of Share than available");
        return true;
    }
    
    public float calculateMainToThis(float amount) {
        return (int)super.calculateMainToThis(amount);
    }
    
}
