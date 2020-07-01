package com.example.esstudy.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.esstudy.demo.beans.StudentPO;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StrpingDataElasticsearchApplicationTests {
    String index = "test";
    @Autowired
    private RestHighLevelClient highLevelClient;

    @Test
    public void createIndex() {
        CreateIndexResponse indexResponse = null;
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject()
                    .field("properties")
                    .startObject()
                    .field("name").startObject().field("index", "true").field("type", "text").field("analyzer", "ik_smart").endObject()
                    .field("age").startObject().field("index", "true").field("type", "integer").endObject()
                    .field("time").startObject().field("index", "true").field("type", "date").field("format", "strict_date_optional_time||epoch_millis").endObject()
                    .endObject().endObject();
            createIndexRequest.mapping(builder);
            indexResponse = highLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (indexResponse != null) {
            System.out.println("response:" + indexResponse.index());
        } else {
            System.out.println("response 返回为空");
        }
    }

    @Test
    public void testExistIndex() {
        GetIndexRequest request = new GetIndexRequest(index);
        boolean exist = false;
        try {
            exist = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("索引是否存在： " + exist);
    }

    @Test
    public void deleteIndex() {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = null;
        try {
            response = highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            System.out.println("删除index的结果： " + response.isAcknowledged());
        } else {
            System.out.println("删除index返回结果为空");
        }

    }

    @Test
    public void addDocument() {
        StudentPO studentPO = new StudentPO();
        studentPO.setAge(15);
        studentPO.setTime(new Date());
        studentPO.setName("张三 9");
        IndexRequest request = new IndexRequest(index);
        IndexResponse response = null;
        try {
            request.source(JSONObject.toJSONString(studentPO), XContentType.JSON);
            response = highLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    @Test
    public void testDocumentExist() {
        GetRequest request = new GetRequest(index, "gJKxCXMBrzoScuKACHSV");
        try {
            System.out.println("是否存在： " + highLevelClient.exists(request, RequestOptions.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDocument() {
        GetRequest request = new GetRequest(index, "gJKxCXMBrzoScuKACHSV");
        try {
            System.out.println(highLevelClient.get(request, RequestOptions.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void search() {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        MatchAllQueryBuilder allQueryBuilder = QueryBuilders.matchAllQuery();
//        searchSourceBuilder.query(allQueryBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "张三");
        boolQueryBuilder.must(termQueryBuilder);


        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.sort("time", SortOrder.DESC);
        request.source(searchSourceBuilder);
        try {
            System.out.println(highLevelClient.search(request, RequestOptions.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
