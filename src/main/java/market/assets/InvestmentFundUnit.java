package market.assets;

import market.traders.InvestmentFund;
import market.exchangeRates.ExchangeRatesProvider;
public class InvestmentFundUnit extends Asset {
    private InvestmentFund issuedBy;

    public InvestmentFundUnit(String name, InvestmentFund issuedBy, ExchangeRatesProvider mainBankRates) {
        super(name, "InvestmentFundUnit", mainBankRates);
        this.issuedBy = issuedBy;
    }

    @Override
    public String toString(){
        return super.toString() + "\nIssued By: " + issuedBy.getName();
    }

    
}
