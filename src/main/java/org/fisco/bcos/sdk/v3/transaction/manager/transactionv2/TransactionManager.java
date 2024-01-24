package org.fisco.bcos.sdk.v3.transaction.manager.transactionv2;

import java.math.BigInteger;
import org.fisco.bcos.sdk.jni.common.JniException;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.client.protocol.response.Call;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.RespCallback;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.gasProvider.ContractGasProvider;
import org.fisco.bcos.sdk.v3.transaction.gasProvider.EIP1559Struct;
import org.fisco.bcos.sdk.v3.transaction.nonce.NonceAndBlockLimitProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransactionManager {

    protected final Client client;

    protected final Logger logger = LoggerFactory.getLogger(TransactionManager.class);

    public Client getClient() {
        return client;
    }

    protected TransactionManager(Client client) {
        int negotiatedProtocol = client.getNegotiatedProtocol();
        int maxProtocol = negotiatedProtocol >> 16;
        if (maxProtocol < 2) {
            logger.error(
                    "The current version of the node does not support the transaction manager, please upgrade the node to the latest version. Max protocol version is {}",
                    maxProtocol);
        }
        this.client = client;
    }

    public abstract ContractGasProvider getGasProvider();

    public abstract void setGasProvider(ContractGasProvider gasProvider);

    public abstract NonceAndBlockLimitProvider getNonceProvider();

    public abstract void setNonceProvider(NonceAndBlockLimitProvider nonceProvider);

    /**
     * Simple send tx
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @return receipt
     */
    public TransactionReceipt sendTransaction(String to, byte[] data, BigInteger value)
            throws JniException {
        return sendTransaction(to, data, value, "", false);
    }

    /**
     * Send tx with abi field
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract TransactionReceipt sendTransaction(
            String to, byte[] data, BigInteger value, String abi, boolean constructor)
            throws JniException;

    /**
     * Send tx with gasPrice and gasLimit fields
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param gasPrice price of gas
     * @param gasLimit use limit of gas
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract TransactionReceipt sendTransaction(
            String to,
            byte[] data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String abi,
            boolean constructor)
            throws JniException;

    /**
     * Send tx with gasPrice and gasLimit fields
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param gasPrice price of gas
     * @param gasLimit use limit of gas
     * @param blockLimit block limit
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract TransactionReceipt sendTransaction(
            String to,
            byte[] data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            BigInteger blockLimit,
            String abi,
            boolean constructor)
            throws JniException;

    /**
     * This method is used to create a signed transaction.
     *
     * @param to The destination address for the transaction.
     * @param data The data to be sent with the transaction.
     * @param value The value to be transferred with the transaction.
     * @param gasPrice The price of gas for the transaction.
     * @param gasLimit The maximum amount of gas that can be used for the transaction.
     * @param blockLimit The maximum block number that can be used for the transaction, if
     *     blockLimit is zero, then get client blockLimit
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param constructor If you deploy contract, should set to be true.
     * @return A Hex string representation of the signed transaction.
     */
    public abstract String createSignedTransaction(
            String to,
            byte[] data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            BigInteger blockLimit,
            String abi,
            boolean constructor)
            throws JniException;

    /**
     * Simple send tx asynchronously
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @return receipt
     */
    public String asyncSendTransaction(
            String to, byte[] data, BigInteger value, TransactionCallback callback)
            throws JniException {
        return asyncSendTransaction(to, data, value, "", false, callback);
    }

    /**
     * Send tx with abi field asynchronously
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract String asyncSendTransaction(
            String to,
            byte[] data,
            BigInteger value,
            String abi,
            boolean constructor,
            TransactionCallback callback)
            throws JniException;

    /**
     * Send tx with gasPrice and gasLimit fields asynchronously
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param gasPrice price of gas
     * @param gasLimit use limit of gas
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract String asyncSendTransaction(
            String to,
            byte[] data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String abi,
            boolean constructor,
            TransactionCallback callback)
            throws JniException;

    /**
     * Send tx with gasPrice and gasLimit fields asynchronously
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param gasPrice price of gas
     * @param gasLimit use limit of gas
     * @param blockLimit block limit
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param constructor if you deploy contract, should set to be true
     * @param callback callback function
     * @return receipt
     */
    public abstract String asyncSendTransaction(
            String to,
            byte[] data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            BigInteger blockLimit,
            String abi,
            boolean constructor,
            TransactionCallback callback)
            throws JniException;

    /**
     * Send tx with EIP1559
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @return receipt
     */
    public TransactionReceipt sendTransactionEIP1559(
            String to, byte[] data, BigInteger value, EIP1559Struct eip1559Struct)
            throws JniException {
        return sendTransactionEIP1559(to, data, value, eip1559Struct, "", false);
    }

    /**
     * Send tx with EIP1559
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract TransactionReceipt sendTransactionEIP1559(
            String to,
            byte[] data,
            BigInteger value,
            EIP1559Struct eip1559Struct,
            String abi,
            boolean constructor)
            throws JniException;

    /**
     * Send tx with EIP1559
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @param blockLimit block limit
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     * @param constructor if you deploy contract, should set to be true
     * @return receipt
     */
    public abstract TransactionReceipt sendTransactionEIP1559(
            String to,
            byte[] data,
            BigInteger value,
            EIP1559Struct eip1559Struct,
            BigInteger blockLimit,
            String abi,
            boolean constructor)
            throws JniException;

    /**
     * Send tx with EIP1559 asynchronously
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @param callback callback function
     * @return receipt
     */
    public String asyncSendTransactionEIP1559(
            String to,
            byte[] data,
            BigInteger value,
            EIP1559Struct eip1559Struct,
            TransactionCallback callback)
            throws JniException {
        return asyncSendTransactionEIP1559(to, data, value, eip1559Struct, "", false, callback);
    }

    /**
     * Send tx with EIP1559 asynchronously
     *
     * @param to to address
     * @param data input data
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     *     contract
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @param constructor if you deploy contract, should set to be true
     * @param callback callback function
     * @return receipt
     */
    public abstract String asyncSendTransactionEIP1559(
            String to,
            byte[] data,
            BigInteger value,
            EIP1559Struct eip1559Struct,
            String abi,
            boolean constructor,
            TransactionCallback callback)
            throws JniException;

    /**
     * Send tx with EIP1559 asynchronously
     *
     * @param to to address
     * @param data input data
     * @param value transfer value
     * @param eip1559Struct EIP1559 transaction payload
     * @param blockLimit block limit
     * @param abi ABI JSON string, generated by compile contract, should fill in when you deploy
     * @param constructor if you deploy contract, should set to be true
     * @param callback callback function
     * @return receipt
     */
    public abstract String asyncSendTransactionEIP1559(
            String to,
            byte[] data,
            BigInteger value,
            EIP1559Struct eip1559Struct,
            BigInteger blockLimit,
            String abi,
            boolean constructor,
            TransactionCallback callback)
            throws JniException;

    /**
     * Send call
     *
     * @param to to address
     * @param data input data
     * @return call result
     */
    public abstract Call sendCall(String to, byte[] data);

    /**
     * Send call with signature of call data
     *
     * @param to to address
     * @param data input data
     * @param signature signature of call data
     */
    public abstract Call sendCall(String to, byte[] data, String signature);

    /**
     * Send call asynchronously
     *
     * @param to to address
     * @param data input data
     * @param callback callback function
     */
    public abstract void asyncSendCall(String to, byte[] data, RespCallback<Call> callback);

    /**
     * Send call asynchronously with signature of call data
     *
     * @param to to address
     * @param data input data
     * @param signature signature of call data
     * @param callback callback function
     */
    public abstract void asyncSendCall(
            String to, byte[] data, String signature, RespCallback<Call> callback);
}
