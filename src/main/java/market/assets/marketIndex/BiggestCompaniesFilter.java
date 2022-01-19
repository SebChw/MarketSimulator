package market.assets.marketIndex;

import market.traders.Company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorts companies based on their current price
 */
public class BiggestCompaniesFilter implements CompaniesFilter, Serializable {

    /**
     * User Must ensure that companiesByNow intersection updatedCompanies is Empty!
     * 
     * @param companiesByNow    companies already in the filter.
     * @param updatedCompanies  new companies that may be added to the filter.
     * @param maxNumOfCompanies if more companies are given then only such number is
     *                          left
     */
    @Override
    public ArrayList<Company> filterCompanies(ArrayList<Company> companies, int maxNumOfCompanies) {

        // for (Company company : companies) {
        // System.out.println(company.getName() + " " + company.getCurrentPrice());
        // }
        Collections.sort(companies, new Comparator<Company>() {
            public int compare(Company c1, Company c2) {
                Float a = -c1.getCurrentPrice();
                Float b = -c2.getCurrentPrice();
                return a.compareTo(b);
            }
        });

        int companiesNumber = companies.size();
        if (companiesNumber > maxNumOfCompanies) {
            companies.subList(maxNumOfCompanies, companiesNumber).clear();
        }

        return companies;
    }

    @Override
    public String getFilterType() {
        return "Biggest";
    }

}
