package market.assets.marketIndex;

import market.traders.Company;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorts companies according to their origin Date
 */
public class StartupsCompaniesFilter implements CompaniesFilter {

    /**
     * User Must ensure that companiesByNow intersection updatedCompanies is Empty!
     * 
     * @param companiesByNow    companies already in the filter.
     * @param updatedCompanies  new companies that may be added to the filter.
     * @param maxNumOfCompanies if more companies are given then only such number is
     *                          left
     */
    @Override
    public void filterCompanies(ArrayList<Company> companiesByNow, ArrayList<Company> updatedCompanies,
            int maxNumOfCompanies) {
        companiesByNow.addAll(updatedCompanies); // !I must ensure that there are no dupliccated in updatedCompanies

        Collections.sort(companiesByNow, new Comparator<Company>() {
            public int compare(Company c1, Company c2) {
                return c1.getIpoDate().compareTo(c2.getIpoDate());
            }
        });

        int companiesNumber = companiesByNow.size();
        if (companiesNumber > maxNumOfCompanies) {
            companiesByNow.subList(maxNumOfCompanies - 1, companiesNumber - 1).clear();
        }

    }

    @Override
    public String getFilterType() {
        return "Startups";
    }

}
