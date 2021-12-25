package market.observers;


public interface AssetSubject {
    public void notifyObservers();
    public void registerObserver(AssetObserver observer);
    public void removeObserver(AssetObserver observer);
}

