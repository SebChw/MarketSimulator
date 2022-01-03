package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;
public interface CompaniesFilter {
    public void filterCompanies(ArrayList<Company> companiesByNow, ArrayList<Company> updatedCompanies, int maxNumOfCompanies);
}
