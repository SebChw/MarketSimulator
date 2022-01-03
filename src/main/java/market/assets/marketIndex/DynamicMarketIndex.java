package market.assets.marketIndex;

import java.util.ArrayList;
import market.traders.Company;
import market.observers.CompanyObserver;

public class DynamicMarketIndex extends MarketIndex implements CompanyObserver{
    private CompaniesFilter companiesFilter;    
    private int maxNumOfCompanies;
    private ArrayList<Company> companiesToUpdate = new ArrayList<Company>();

    public DynamicMarketIndex(String name, ArrayList<Company> companies, String backingAsset, float startingRate, CompaniesFilter companiesFilter, int maxNumOfCompanies) {
        super(name, "Dynamic Market Index", companies, backingAsset, startingRate);
        this.companiesFilter = companiesFilter;
        this.maxNumOfCompanies = maxNumOfCompanies;
    }

    @Override
    public void update(Company company) {
        if (!this.getCompanies().contains(company)){
            companiesToUpdate.add(company);
        }
    }
    
    public void updateIndex(){
        companiesFilter.filterCompanies(this.getCompanies(), this.companiesToUpdate, maxNumOfCompanies);

        this.getMainBankRates().updateRate(calculateRatio());
    }
}
