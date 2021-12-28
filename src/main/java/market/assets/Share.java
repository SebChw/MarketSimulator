package market.assets;

import market.traders.Company;
import market.exchangeRates.ExchangeRatesProvider;
public class Share extends Asset{
    private Company company;

    public Share(String name, Company company, ExchangeRatesProvider mainBankRates) {
        super(name, "Share", mainBankRates);
        this.company = company;
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued by: " + company.getName();
    }
}
