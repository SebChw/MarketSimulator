package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class BiggestCompaniesFilter implements CompaniesFilter{
    //Sort companies by their CurrentPrice
    //!I must ensure that there are no dupliccated in updatedCompanies
    @Override
    public void filterCompanies(ArrayList<Company> companiesByNow, ArrayList<Company> updatedCompanies, int maxNumOfCompanies) {
        //We refilter here
        companiesByNow.addAll(updatedCompanies); //!I must ensure that there are no dupliccated in updatedCompanies
        
        Collections.sort(companiesByNow, new Comparator<Company>() {
            public int compare(Company c1, Company c2)
            {
                Float a = -c1.getCurrentPrice();
                Float b = -c2.getCurrentPrice();
                return a.compareTo(b);
            }
        });

        int companiesNumber = companiesByNow.size();
        if (companiesNumber > maxNumOfCompanies){
            companiesByNow.subList(maxNumOfCompanies-1, companiesNumber-1).clear();
        }

    }

    
    
    
}
