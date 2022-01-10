package market.assets.marketIndex;

import java.util.ArrayList;
import market.traders.Company;
import market.observers.CompanyObserver;

public class DynamicMarketIndex extends MarketIndex implements CompanyObserver{
    private CompaniesFilter companiesFilter;    
    private int maxNumOfCompanies;
    private volatile ArrayList<Company> companiesToUpdate = new ArrayList<Company>();

    public DynamicMarketIndex(String name, ArrayList<Company> companies, String backingAsset, float startingRate, CompaniesFilter companiesFilter, int maxNumOfCompanies) {
        super(name, "Dynamic Market Index", companies, backingAsset, startingRate);
        this.companiesFilter = companiesFilter;
        this.maxNumOfCompanies = maxNumOfCompanies;
    }

    public String toString(){
        return super.toString() + "\n Max number of Companies:" + maxNumOfCompanies;
    }

    @Override
    public void update(Company company) {
        //System.out.println("Company " + company.getName() + "tries to be updated index");
        for (Company c : this.getCompanies()){
            if (c.getName().equals(company.getName())){
                //System.out.println(company.getName() + c.getName());
                return;
            } 
        }
            companiesToUpdate.add(company);
    }
    
    public void updateIndex(){
        companiesFilter.filterCompanies(this.getCompanies(), this.companiesToUpdate, maxNumOfCompanies);
        companiesToUpdate.clear();

        updateRate();
    }
}
