package com.aurx.core.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
@Model(adaptables = Resource.class)
public class SlingModelOfProducts {
    @Inject
    String products;

    private List<Products> properties;
    @SlingObject
    private Resource resource;



    @PostConstruct
    protected  void init(){

getJSONData();

    }

    public JsonArray getJsonObject()throws IOException {
        String baseURL = "https://dummyjson.com/products/";
    URL url = new URL(baseURL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("accept", "application/json");
    InputStream responseStream = connection.getInputStream();
    String response = IOUtils.toString(responseStream, StandardCharsets.UTF_8);
    JsonParser parser = new JsonParser();
//Creating JSONObject from String using parser
    return parser.parse(response).getAsJsonObject().get("products").getAsJsonArray();


    }

    public void getJSONData(){
        int numberOfProducts = Integer.parseInt(products);
        properties=new ArrayList<>();

        try{
            JsonArray productsArray=getJsonObject();
            for(int i=0; i< numberOfProducts; i++) {

                JsonObject jsonObject = productsArray.get(i).getAsJsonObject();
                properties.add(new Products(jsonObject.get("id").toString(),jsonObject.get("price").toString(),jsonObject.get("title").getAsString(), jsonObject.get("description").getAsString(), jsonObject.get("images").getAsJsonArray().get(0).getAsString()));
            }

        }catch (IOException e){

        }
    }

    public List<Products> getProperties() {
        return properties;
    }
}
