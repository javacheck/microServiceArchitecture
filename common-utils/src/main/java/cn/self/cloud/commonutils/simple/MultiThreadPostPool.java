package cn.self.cloud.commonutils.simple;

import cn.self.cloud.commonutils.password.MurmurHash2;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Chen Ping on 14-9-19.
 */
public class MultiThreadPostPool {
    public static final int Max_URL_Connection_Count = 10;
    public static final String Success_Value = "[配载成功]";
    public static List<String> cwsServerUrls = new ArrayList<String>();
    private static PoolingHttpClientConnectionManager cm = null;
    private static CloseableHttpClient httpclient = null;

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(30);
        httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        String urls = ConfigHelper.getInstance().get("cwsServerUrl");
        String[] urlArr = urls.split(",");
        for (String url : urlArr) {
            if (StringUtils.isNotEmpty(url)) {
                cwsServerUrls.add(url);
            }
        }
    }

    private static Logger logger = LoggerFactory.getLogger(MultiThreadPostPool.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static Random random = new Random();
    private static AtomicInteger splitingCount = new AtomicInteger(0);
    private static List<MyUrlConnection> myUrlConnectionList = new ArrayList<MyUrlConnection>();

    private static void freeHttpUrlConnection(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null)
            return;
        for (MyUrlConnection myUrlConnection : myUrlConnectionList) {
            synchronized (myUrlConnection) {
                if (myUrlConnection.getHttpURLConnection() == httpURLConnection) {
                    myUrlConnection.setFree(true);
                    httpURLConnection = null;
                }
            }
        }
    }

    private static void freeBrokenHttpUrlConnection(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null)
            return;
        synchronized (myUrlConnectionList) {
            MyUrlConnection foundUrlConnection = null;
            for (MyUrlConnection myUrlConnection : myUrlConnectionList) {
                if (myUrlConnection.getHttpURLConnection() == httpURLConnection) {
                    foundUrlConnection = myUrlConnection;
                    break;
                }
            }
            if (foundUrlConnection != null) {
                myUrlConnectionList.remove(foundUrlConnection);
            }
        }
        httpURLConnection = null;
    }

    private static HttpURLConnection getHttpUrlConnection() {
        HttpURLConnection httpURLConnection = null;
        while (httpURLConnection == null) {
            synchronized (myUrlConnectionList) {
                if (myUrlConnectionList.size() < Max_URL_Connection_Count) {
                    MyUrlConnection myUrlConnection = new MyUrlConnection();
                    myUrlConnection.setFree(false);
                    httpURLConnection = produceHttpUrlConnection();
                    myUrlConnection.setHttpURLConnection(httpURLConnection);
                    myUrlConnectionList.add(myUrlConnection);
                    return httpURLConnection;
                }
            }
            for (MyUrlConnection myUrlConnection : myUrlConnectionList) {
                synchronized (myUrlConnection) {
                    if (myUrlConnection.isFree() && myUrlConnection.getHttpURLConnection() != null) {
                        myUrlConnection.setFree(false);
                        return myUrlConnection.getHttpURLConnection();
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static HttpURLConnection produceHttpUrlConnection() {
        HttpURLConnection myConn = null;
        try {
            int urlCount = cwsServerUrls.size();
            int index = random.nextInt(urlCount);
            URL _url = new URL(cwsServerUrls.get(index));
            myConn = (HttpURLConnection) _url.openConnection();
            myConn.setUseCaches(false);
            myConn.setRequestMethod("POST");
            //  myConn.setRequestProperty("Connection", "close");
//            myConn.setConnectTimeout(5000);
//            myConn.setReadTimeout(5000);
            myConn.setDoOutput(true);
            myConn.setDoInput(true);
            myConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            myConn.setRequestProperty("Connection", "keep-alive");
            myConn.setConnectTimeout(20 * 1000);
            myConn.setReadTimeout(12 * 1000);
            myConn.connect();
            return myConn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myConn;
    }

    private static String trySplit1(String sentence) throws Exception {
        HttpURLConnection myConn = getHttpUrlConnection();
        try {
            InputStream inputStream = myConn.getInputStream();
            OutputStream outputStream = myConn.getOutputStream();
            String pmStr = "w=";
            try {
                pmStr += URLEncoder.encode(sentence, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            outputStream.write(pmStr.getBytes());
            outputStream.flush();
            outputStream.close();
            if (myConn.getResponseCode() == 200) {
                ByteArrayOutputStream fOutputStream = new ByteArrayOutputStream();
                byte[] bs = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bs)) > 0) {
                    outputStream.write(bs, 0, len);
                }
                String ret = new String(fOutputStream.toByteArray());
                inputStream.close();
                outputStream.close();
                return ret;
            }
        } finally {
            freeHttpUrlConnection(myConn);
        }
        return null;
    }

    private static String trySplit(String sentence) throws Exception {
        HttpURLConnection myConn = null;
        try {
            //System.setProperty("http.keepAlive", "false");
            int urlCount = cwsServerUrls.size();
            int index = random.nextInt(urlCount);
            String pmStr = "?w=";
            try {
                pmStr += URLEncoder.encode(sentence, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            URL _url = new URL(cwsServerUrls.get(index) + pmStr);
            myConn = (HttpURLConnection) _url.openConnection();
            myConn.setUseCaches(false);
            myConn.setRequestMethod("GET");
//            myConn.setRequestProperty("Connection", "close");
            myConn.setConnectTimeout(5000);
            myConn.setReadTimeout(5000);
            myConn.connect();
            if (myConn.getResponseCode() == 200) {
                InputStream inputStream = myConn.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] bs = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(bs)) > 0) {
                    outputStream.write(bs, 0, len);
                }
                String ret = new String(outputStream.toByteArray());
                inputStream.close();
                outputStream.close();
                return ret;
            }
        } finally {
          /*  if (myConn != null) {
                myConn.disconnect();
                myConn = null;
            }*/
        }
        return null;
    }

    public static String splitSentence1(String sentence) {
        if (StringUtils.isEmpty(sentence))
            return "";
        int waitCount = 0;
        while (splitingCount.get() > 1000) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitCount++;
            if (waitCount > 10)
                logger.info("目前等待分词的数量已经超过50000了");
        }
        splitingCount.getAndIncrement();
        try {
            return trySplit(sentence);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("分词出现异常，进行分词的内容：" + sentence);
            try {
                return trySplit(sentence);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } finally {
            splitingCount.getAndDecrement();
        }
        return null;
    }

    public static String splitSentenceSet(Set<String> stringSet) {
        if (stringSet == null || stringSet.isEmpty()) {
            return null;
        }
        List<Callable<String>> callableList = new ArrayList<Callable<String>>();
        int urlCount = cwsServerUrls.size();

        Set<String> splited = new HashSet<String>();
        for (String str : stringSet) {
            int index = random.nextInt(urlCount);
            Map<String, String> data = new HashMap<String, String>();
            data.put("w", str);
            PostThread postThread = new PostThread(httpclient, cwsServerUrls.get(index), data);
            callableList.add(postThread);
        }
        try {
            int size = stringSet.size();
            List<Future<String>> futureList = executorService.invokeAll(callableList);
            for (int i = 0; i < size; i++) {
                Future<String> future = futureList.get(i);
                String ret = future.get();
                if (StringUtils.isNotEmpty(ret))
                    splited.add(ret);
            }
        } catch (ExecutionException eu) {
            eu.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : splited) {
            stringBuilder.append(s);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    public static String splitSentence(String sentence) {
        if (StringUtils.isEmpty(sentence)) {
            return "";
        }
        Map<String, String> reqData = new HashMap<String, String>();
        reqData.put("w", sentence);
        int urlCount = cwsServerUrls.size();
        int index = random.nextInt(urlCount);
        PostThread postThread = new PostThread(httpclient, cwsServerUrls.get(index), reqData);
        try {
            Future<String> future = executorService.submit(postThread);
            String ret = future.get();
//            logger.info("对句子" + sentence + "分词结果｛" + ret + "}");
            return ret;
        } catch (ExecutionException eu) {
            eu.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static final int  Hash_Seed=83789426;

    public static void post(Map<String, String>[] dataArray, List<HyInfo> hyInfoList) {
        if (dataArray == null || dataArray.length == 0) {
            return;
        }
        List<Callable<String>> callableList = new ArrayList<Callable<String>>();
        int urlCount = cwsServerUrls.size();

        for (Map<String, String> data : dataArray) {
            int index = random.nextInt(urlCount);
            PostThread postThread = new PostThread(httpclient, cwsServerUrls.get(index), data);
            callableList.add(postThread);
        }
        try {
            int size = hyInfoList.size();
            List<Future<String>> futureList = executorService.invokeAll(callableList);
            for (int i = 0; i < size; i++) {
                Future<String> future = futureList.get(i);
                String ret = future.get();
//                logger.info(hyInfoList.get(i).getInfotxt()+"\r\n分词结果："+ret);
                HyInfo hyInfo = hyInfoList.get(i);
                hyInfo.setDevided(ret);
                String content = hyInfo.getInfotxt();
                if (content.startsWith(Success_Value)) {
                    content = content.substring(Success_Value.length());
                }
                String md5 = hyInfo.getMd5();
                hyInfo.setMd5(md5);
                hyInfo.setHash(MurmurHash2.hash(content.getBytes(), Hash_Seed));
            }
        } catch (ExecutionException eu) {
            eu.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        } finally {
//            try {
//                httpclient.close();
//                cm.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public static void closePool() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    private static class MyUrlConnection {
        private HttpURLConnection httpURLConnection;
        private boolean isFree;

        public HttpURLConnection getHttpURLConnection() {
            return httpURLConnection;
        }

        public void setHttpURLConnection(HttpURLConnection httpURLConnection) {
            this.httpURLConnection = httpURLConnection;
        }

        public boolean isFree() {
            return isFree;
        }

        public void setFree(boolean isFree) {
            this.isFree = isFree;
        }
    }

    private static class PostThread implements Callable<String> {
        private final HttpContext context;
        private String url;
        private Map<String, String> data;
        private HttpClient httpClient;

        public PostThread(HttpClient httpClient, String url, Map<String, String> data) {
            this.httpClient = httpClient;
            this.url = url;
            this.data = data;
            this.context = new BasicHttpContext();
        }

        public String call() {
            try {
                HttpPost httpPost = new HttpPost(url);
                // logger.info("url:" + url + "\r\n" + "dataMap:" + data);
                if (data != null) {
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, String> entry : data.entrySet()) {
                        NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
                        list.add(pair);
                    }
                    StringEntity reqEntity = new UrlEncodedFormEntity(list, "UTF-8");
                    httpPost.setEntity(reqEntity);
                }
//                logger.info("Executing request: " + httpPost.getRequestLine());
                CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpPost, context);
                HttpEntity httpEntity = null;
                try {
                    StatusLine statusLine = response.getStatusLine();
//                    logger.info("发送请求：" + url + ",返回:" + statusLine);
                    httpEntity = response.getEntity();
                    String str = "";
                    if (httpEntity != null) {
                        str = EntityUtils.toString(httpEntity, "UTF-8");
                    }
                    if (statusLine.getStatusCode() == 200) {
                        return str;
                    }
                } finally {
                    if (httpEntity != null) {
                        EntityUtils.consume(httpEntity);
                    }
                    // httpPost.releaseConnection();
                    // if (response != null) {
                    //     response.close();
                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            return null;
        }
    }
}
