package conflux.dex.tool.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class ERCContract extends org.web3j.tx.Contract{


    public ERCContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function("transferFrom", Arrays.asList(new Address(_from), new Address(_to), new Uint256(_value)), Collections.emptyList());
        return this.executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function("transfer", Arrays.asList(new Address(_to), new Uint256(_value)), Collections.emptyList());
        return this.executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> send(String _to, BigInteger _value, byte[] data) {
        Function function = new Function("send", Arrays.asList(new Address(_to), new Uint256(_value), new DynamicBytes(data)), Collections.emptyList());
        return this.executeRemoteCallTransaction(function);
    }
}