package io.ztech.magento.proxy.model;

public class MageRequest {

    protected String sUri;
    protected String sHost;
    protected String sRequestType;

    public String getsUri() {
        return sUri;
    }

    public void setsUri(String sUri) {
        this.sUri = sUri;
    }

    public String getsHost() {
        return sHost;
    }

    public void setsHost(String sHost) {
        this.sHost = sHost;
    }

    public String getsRequestType() {
        return sRequestType;
    }

    public void setsRequestType(String sRequestType) {
        this.sRequestType = sRequestType;
    }

    public String getsBody() {
        return sBody;
    }

    public void setsBody(String sBody) {
        this.sBody = sBody;
    }

    protected String sBody;
}
