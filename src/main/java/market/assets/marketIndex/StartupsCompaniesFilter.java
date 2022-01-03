package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StartupsCompaniesFilter implements CompaniesFilter {
    //Sort companies by their Date of Origin
    @Override
    public void filterCompanies(ArrayList<Company> companiesByNow, ArrayList<Company> updatedCompanies, int maxNumOfCompanies) {
        companiesByNow.addAll(updatedCompanies); //!I must ensure that there are no dupliccated in updatedCompanies
        
        Collections.sort(companiesByNow, new Comparator<Company>() {
            public int compare(Company c1, Company c2)
            {
                return c1.getIpoDate().compareTo(c2.getIpoDate());
            }
        });

        int companiesNumber = companiesByNow.size();
        if (companiesNumber > maxNumOfCompanies){
            companiesByNow.subList(maxNumOfCompanies-1, companiesNumber-1).clear();
        }
        
    }
    
    
}
