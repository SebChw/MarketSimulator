package market.assets.marketIndex;

import market.traders.Company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorts companies according to their origin Date
 */
public class StartupsCompaniesFilter implements CompaniesFilter, Serializable {

    /**
     * 
     * @param companiesByNow    companies to be filtered
     * @param maxNumOfCompanies if more companies are given then only such number is
     *                          left
     */
    @Override
    public ArrayList<Company> filterCompanies(ArrayList<Company> companies, int maxNumOfCompanies) {
        // for (Company company : companies) {
        // System.out.println(company.getName() + " " + company.getIpoDate());
        // }

        Collections.sort(companies, new Comparator<Company>() {
            public int compare(Company c1, Company c2) {
                return c1.getIpoDate().compareTo(c2.getIpoDate());
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
        return "Startups";
    }

}
