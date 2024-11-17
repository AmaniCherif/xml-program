package com.adelean.elasticsearch.indexer.xml;

import com.adelean.elasticsearch.config.ConfigLoader;
import com.adelean.elasticsearch.config.ElasticsearchConfigKeys;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Getter
@Setter
public class ElasticsearchClient {

    private RestHighLevelClient client;

    public ElasticsearchClient(ConfigLoader configLoader) {
        String hostname = configLoader.getProperty(ElasticsearchConfigKeys.ELASTICSEARCH_HOSTNAME);
        int port = Integer.parseInt(configLoader.getProperty(ElasticsearchConfigKeys.ELASTICSEARCH_PORT));
        String scheme = configLoader.getProperty(ElasticsearchConfigKeys.ELASTICSEARCH_SCHEME);

        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme));
        this.client = new RestHighLevelClient(builder);
    }

    public void indexDocument(String index, String id, Map<String, String> document) {
        IndexRequest request = new IndexRequest(index).id(id).source(document, XContentType.JSON);
        try {
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info("Document indexed with ID: {}", response.getId());
        } catch (IOException e) {
            log.error("Error indexing document", e);
        }
    }

    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            log.error("Error closing Elasticsearch client", e);
        }
    }
}