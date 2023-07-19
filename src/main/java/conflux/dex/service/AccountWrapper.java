package conflux.dex.service;

import conflux.dex.model.Account;
import conflux.web3j.Cfx;
import conflux.web3j.types.Address;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;

public class AccountWrapper {
    private final String address;
    private final Cfx cfx;
    private final Address objAddress;
    private BigInteger nonce;

    public AccountWrapper(Cfx cfx, String privateKey) {
        Credentials credentials = Credentials.create(privateKey);
        this.address = "0x"+credentials.getAddress();
        this.cfx = cfx;
        this.objAddress = new Address(this.address, cfx.getIntNetworkId());
        this.nonce = cfx.getNonce(this.objAddress).sendAndGet();
    }

    public String getAddress() {
        return address;
    }

    public Cfx getCfx() {
        return cfx;
    }

    public Address getObjAddress() {
        return objAddress;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }
}
