package conflux.dex.blockchain;

import conflux.web3j.response.Log;
import okhttp3.OkHttpClient;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static conflux.dex.tool.EvmTxTool.convertEvmLog;

public class EvmHttpProvider extends HttpService {
    public EvmHttpProvider(String url, OkHttpClient client) {
        super(url, client);
    }

    @Override
    public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        if (request.getMethod().equals("cfx_getLogs")) {
            EvmCompatibleLog.Response res = super.send(request, EvmCompatibleLog.Response.class);
            if (!res.hasError()) {
                List<Log> list = res.getValue().stream().map(log -> {
                    Log log1 = convertEvmLog(log);
                    return log1;
                }).collect(Collectors.toList());
                CfxListResponse<Log> listResp = new CfxListResponse<>();
                listResp.setResult(list);
                return (T) listResp;
            }
            return (T) res;
        }
        return super.send(request, responseType);
    }


}
