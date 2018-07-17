package io.ztech.magento.proxy.services;


import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.ztech.magento.proxy.configs.Constants;

import java.io.IOException;
import java.security.GeneralSecurityException;


    public class OAuthInterceptor implements Interceptor {

        private final OAuthParameters oAuthParams;


        public OAuthInterceptor(OAuthParameters oAuthParams) {
            this.oAuthParams = oAuthParams;

        }


        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            GenericUrl requestUrl = new GenericUrl(originalRequest.urlString());
            oAuthParams.computeNonce();
            oAuthParams.computeTimestamp();
            try {
                oAuthParams.computeSignature(originalRequest.method(), requestUrl);
                Request compressedRequest = originalRequest.newBuilder()
                        .header(Constants.AUTHORIZATION, oAuthParams.getAuthorizationHeader())
                        .header(Constants.ACCEPT,Constants.MEDIA_TYPE_JSON)
                        .method(originalRequest.method(),originalRequest.body())
                        .build();
                return chain.proceed(compressedRequest);
            } catch (GeneralSecurityException e) {
            }


            return chain.proceed(originalRequest);
        }
    }

