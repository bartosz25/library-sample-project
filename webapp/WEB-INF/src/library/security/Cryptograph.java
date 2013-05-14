package library.security;


public interface Cryptograph {
    public void setRandom(String random);
    public String getRandom();
    public String encrypt(String toEncrypt);
    public String decrypt(String toDecrypt);
}