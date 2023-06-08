package conflux.dex.tool;

import conflux.dex.tool.contract.ERCContract;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class EvmTxTool {
    public Web3j web3j;
    private ContractGasProvider contractGasProvider;
    private TransactionManager readOnlyTransactionManager;
    private Credentials credentials;

    public void setup(String rpc, String privateKey) {

        credentials = Credentials.create(privateKey);

        HttpService httpService = new HttpService(rpc);
        this.web3j = Web3j.build(httpService);
        String zero = Constants.ZERO;
        readOnlyTransactionManager = new ReadonlyTransactionManager(web3j, zero);
        contractGasProvider = new StaticGasProvider(
                BigInteger.valueOf(40_000_000_000L), // price
                BigInteger.valueOf(9_000_000) // limit
        );
    }

    public ERCContract buildERCContract(String contractAddress) {
        return new ERCContract(
                Contract.BIN_NOT_PROVIDED,
                contractAddress,
                this.web3j,
                this.credentials,
                this.contractGasProvider
        );
    }
}

