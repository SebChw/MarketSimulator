package market.assets.marketIndex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import market.interfaces.CompanyObserver;
import market.traders.Company;
import market.world.Constants;
import market.world.World;

/**
 * Extension of simple market index. This is dynamic. It means if some company
 * changes it's value the index may change also
 */
public class DynamicMarketIndex extends MarketIndex implements CompanyObserver {
    private CompaniesFilter companiesFilter;
    private int maxNumOfCompanies;
    private volatile HashSet<String> companiesToFilter = new HashSet<String>();
    private World world;

    public DynamicMarketIndex(String name, ArrayList<Company> companies, String backingAsset,
            CompaniesFilter companiesFilter, int maxNumOfCompanies, World world) {
        super(name, Constants.dynamicIndexType, companies, backingAsset);
        this.companiesFilter = companiesFilter;
        this.maxNumOfCompanies = maxNumOfCompanies;
        this.world = world;

        updateRate();
    }

    /**
     * @return String
     */
    public String toString() {
        return super.toString() + "\n Max number of Companies:" + maxNumOfCompanies + "\n Filter type: "
                + companiesFilter.getFilterType();
    }

    /**
     * add company to be updated if it is not already in the index
     * 
     * @param company
     */
    @Override
    public void update(Company company) {
        for (Company c : this.getCompanies()) {
            if (c.getName().equals(company.getName())) {
                // System.out.println(company.getName() + c.getName());
                return;
            }
        }
        companiesToFilter.add(company.getName());
    }

    public void updateRate() {
        if (!Objects.isNull(companiesFilter)) {
            this.setCompanies(
                    companiesFilter.filterCompanies(new ArrayList<Company>(world.getWorldContainer().getCompanies()),
                            maxNumOfCompanies));
            // companiesToUpdate.clear();
        }
        super.updateRate();
    }
}
