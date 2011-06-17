package net.opgenorth.prdc11.scotchy;

public class ScotchBrand {
    private String _brand;
    private int _age;
    private String _url;

    public ScotchBrand(String _brand, int _age, String _url) {
        this._brand = _brand;
        this._age = _age;
        this._url = _url;
    }

    public String getBrand() {
        return _brand;
    }
    public int getAge() {
        return _age;
    }
    public String getUrl() {
        return _url;
    }
}
