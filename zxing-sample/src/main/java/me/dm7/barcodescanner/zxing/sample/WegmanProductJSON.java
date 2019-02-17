package me.dm7.barcodescanner.zxing.sample;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class WegmanProductJSON {

    private static int getSKU(String barcodeUPC, String key){
        int sku = 0;
        try{
            // first make the url we need
            String barcodeUrl = "https://api.wegmans.io/products/barcodes/" + barcodeUPC + "?api-version=2018-10-18";

            HttpClient barcodeHttpClient = HttpClients.createDefault();

            // send a request to wegmans
            URIBuilder barcodeBuilder = new URIBuilder(barcodeUrl);
            URI barcodeUri = barcodeBuilder.build();
            HttpGet barcodeRequest = new HttpGet(barcodeUri);
            barcodeRequest.setHeader("Subscription-Key", key);


            // get the response back
            HttpResponse barcodeResponse = barcodeHttpClient.execute(barcodeRequest);
            HttpEntity barcodeEntity = barcodeResponse.getEntity();

            // validate our request was good
            if (barcodeEntity == null){
                System.err.println("SOMETHING WENT WRONG GETTING SKU");
                System.exit(1);
            }

            String barcodeJsonResult = EntityUtils.toString(barcodeEntity);

            // make a GSON parser for us
            Gson gson = new Gson();

            // get the sku data

            SKU skuClass = gson.fromJson(barcodeJsonResult, SKU.class);
            sku = skuClass.getSku();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sku;
    }

    private static String getProductData(int sku, String key){
        String productInfo = "";
        try{
            // make the main http client
            HttpClient httpClient = HttpClients.createDefault();

            // build the uri
            String url = "https://api.wegmans.io/products/" + sku + "?api-version=2018-10-18";
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Subscription-Key", key);

            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if(entity == null){
                System.err.println("SOMETHING WENT WRONG GETTING PRODUCT DATA");
                System.exit(1);
            }
            productInfo = EntityUtils.toString(entity);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return productInfo;
    }

    public static String upcToProductData(String upc){
        String key = "5036d8214f9e47a5ae6bfa2204cb0903";
//        int sku = getSKU(upc, key);
        int sku = 37979;
        return getProductData(sku, key);
    }
}
