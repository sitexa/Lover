package com.sitexa.lover.blockchain.chain;

import com.google.gson.annotations.Expose;
import com.sitexa.lover.blockchain.cypto.digest.Sha256;
import com.sitexa.lover.blockchain.cypto.ec.EcDsa;
import com.sitexa.lover.blockchain.cypto.ec.EcSignature;
import com.sitexa.lover.blockchain.cypto.ec.EosPrivateKey;
import com.sitexa.lover.blockchain.types.EosByteWriter;
import com.sitexa.lover.blockchain.types.TypeChainId;
import com.sitexa.lover.blockchain.cypto.digest.Sha256;
import com.sitexa.lover.blockchain.cypto.ec.EcDsa;
import com.sitexa.lover.blockchain.cypto.ec.EcSignature;
import com.sitexa.lover.blockchain.cypto.ec.EosPrivateKey;
import com.sitexa.lover.blockchain.types.EosByteWriter;
import com.sitexa.lover.blockchain.types.TypeChainId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pocketEos on 2018/4/26.
 */

public class SignedTransaction extends Transaction {

    @Expose
    private List<String> signatures = null;

    @Expose
    private List<String> context_free_data = new ArrayList<>();


    public SignedTransaction(){
        super();
    }

    public SignedTransaction( SignedTransaction anotherTxn){
        super(anotherTxn);
        this.signatures = deepCopyOnlyContainer( anotherTxn.signatures );
        this.context_free_data = context_free_data;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void putSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public int getCtxFreeDataCount() {
        return ( context_free_data == null ) ? 0 : context_free_data.size();
    }

    public List<String> getCtxFreeData() {
        return context_free_data;
    }


    private Sha256 getDigestForSignature(TypeChainId chainId) {
        EosByteWriter writer = new EosByteWriter(255);

        // data layout to sign :
        // [ {chainId}, {Transaction( parent class )}, {hash of context_free_data only when exists ]

        writer.putBytes(chainId.getBytes());
        pack( writer);
        if (context_free_data.size() > 0 ) {
        }
        else {
            writer.putBytes( Sha256.ZERO_HASH.getBytes());
        }

        return Sha256.from(writer.toBytes());
    }

    public void sign(EosPrivateKey privateKey, TypeChainId chainId) {
        if ( null == this.signatures){
            this.signatures = new ArrayList<>();
        }

        EcSignature signature = EcDsa.sign(getDigestForSignature( chainId ), privateKey);
        this.signatures.add( signature.toString());
    }
}

