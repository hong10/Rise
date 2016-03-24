package com.hong.rise.utils;

import com.hong.rise.lottery.ConstantValue;
import com.hong.rise.lottery.GlobalParams;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/3/24.
 */
public class HttpClientUtil {

    private HttpClient client;
    private HttpPost post;


    public HttpClientUtil() {
        client = new DefaultHttpClient();
        // 判断是否需要设置代理信息
        if (StringUtils.isNotBlank(GlobalParams.PROXY)) {
            // 设置代理信息
            HttpHost host = new HttpHost(GlobalParams.PROXY, GlobalParams.PORT);
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);

        }

    }


    /**
     * 向指定的链接发送xml文件
     *
     * @param uri
     * @param xml
     * @return
     */
    public InputStream sendXml(String uri, String xml) {
        post = new HttpPost(uri);

        StringEntity entity = null;
        try {
            entity = new StringEntity(uri, ConstantValue.ENCONDING);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 200) {
                return response.getEntity().getContent();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
