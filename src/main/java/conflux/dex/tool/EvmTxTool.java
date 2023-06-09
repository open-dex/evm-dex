package conflux.dex.tool;

import conflux.dex.blockchain.EvmCompatibleLog;
import conflux.dex.controller.EvmAddress;
import conflux.dex.tool.contract.ERCContract;
import conflux.web3j.response.Log;
import conflux.web3j.response.Receipt;
import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.stream.Collectors;

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
    @NotNull
    public static Log convertEvmLog(EvmCompatibleLog log) {
        Log log1 = new Log();
        log1.setAddress(log.getAddress());
        log1.setTopics(log.getTopics());
        log1.setData(log.getData());
        log1.setTransactionHash(log.getTransactionHash().get());
        return log1;
    }
    @NotNull
    public static Log convertEvmLog(org.web3j.protocol.core.methods.response.Log log) {
        Log log1 = new Log();
        log1.setAddress(new EvmAddress(log.getAddress(), 71));
        log1.setTopics(log.getTopics());
        log1.setData(log.getData());
        log1.setTransactionHash(log.getTransactionHash());
        return log1;
    }
    public static Receipt convertReceipt(TransactionReceipt web3receipt) {
        Receipt receipt = new Receipt();
        receipt.setBlockHash(web3receipt.getBlockHash());
        receipt.setTransactionHash(web3receipt.getTransactionHash());
        receipt.setLogs(web3receipt.getLogs().stream().map(EvmTxTool::convertEvmLog).collect(Collectors.toList()));
        receipt.setTo(new EvmAddress(web3receipt.getTo(), 71));
        receipt.setOutcomeStatus(web3receipt.getStatus());
        return receipt;
    }
}

