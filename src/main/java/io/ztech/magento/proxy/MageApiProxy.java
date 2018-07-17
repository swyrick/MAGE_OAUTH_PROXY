package io.ztech.magento.proxy;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.gson.Gson;
import com.squareup.okhttp.*;
import io.ztech.magento.proxy.configs.Constants;
import io.ztech.magento.proxy.model.MageRequest;
import io.ztech.magento.proxy.services.OAuthInterceptor;




@WebServlet(name = "MageApiProxy")
public class MageApiProxy extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Gson gson = new Gson();
        MageRequest apiRequest = null;

        try {
            StringBuilder sb = new StringBuilder();
            String sRequest;
            while ((sRequest = request.getReader().readLine()) != null) {
                sb.append(sRequest);
            }

            apiRequest = gson.fromJson(sb.toString(), MageRequest.class);

        } catch (Exception ex) {
            ex.printStackTrace();

        }



        String sURI = apiRequest.getsHost() + apiRequest.getsUri();

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new OAuthInterceptor(getConsumer()));

        Request.Builder okrequestbuilder = new Request.Builder()
                .header(Constants.CONTENT_TYPE, Constants.MEDIA_TYPE_JSON)
                .url(sURI);

        if(!apiRequest.getsRequestType().toUpperCase().equals(Constants.GET)){
            byte[] json = apiRequest.getsBody().getBytes();
            okrequestbuilder.post(RequestBody.create(MediaType.parse(Constants.MEDIA_TYPE_JSON), json));
        }


        Request okrequest = okrequestbuilder.build();

        Response okresponse = client.newCall(okrequest).execute();


        response.setContentType(Constants.MEDIA_TYPE_JSON);
        response.getOutputStream().println(okresponse.body().string());

    }


    protected OAuthParameters getConsumer() {
        OAuthHmacSigner signer = new OAuthHmacSigner();
        signer.clientSharedSecret = Constants.CONSUMER_SECRET;
        signer.tokenSharedSecret = Constants.APP_TOKEN_SECRET;
        OAuthParameters authorizer = new OAuthParameters();
        authorizer.consumerKey = Constants.CONSUMER_KEY;
        authorizer.signer = signer;
        authorizer.token = Constants.APP_TOKEN;

        return authorizer;
    }


}
