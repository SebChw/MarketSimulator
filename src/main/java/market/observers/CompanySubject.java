package market.observers;

public interface CompanySubject {
    public void notifyObservers();
    public void registerObserver(CompanyObserver observer);
    public void removeObserver(CompanyObserver observer);
}
