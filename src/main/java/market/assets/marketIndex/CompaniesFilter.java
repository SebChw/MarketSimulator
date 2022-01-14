package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;

/**
 * Interface which allows for filtering companies based on some rule
 */
public interface CompaniesFilter {
    public void filterCompanies(ArrayList<Company> companiesByNow, ArrayList<Company> updatedCompanies,
            int maxNumOfCompanies);

    public String getFilterType();
}
