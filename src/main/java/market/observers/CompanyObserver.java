package market.observers;

import market.traders.Company;

public interface CompanyObserver {
    public void update(Company company);
}
