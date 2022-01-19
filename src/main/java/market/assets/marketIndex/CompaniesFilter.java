package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;

/**
 * Interface which allows for filtering companies based on some rule
 */
public interface CompaniesFilter {
    public ArrayList<Company> filterCompanies(ArrayList<Company> companies, int maxNumOfCompanies);

    public String getFilterType();
}
