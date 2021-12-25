package market.observers;

public interface AssetObserver {
    public void update(String assetName, int hypeLevel, int amountOfOwners, float amountInCirulation);
}
