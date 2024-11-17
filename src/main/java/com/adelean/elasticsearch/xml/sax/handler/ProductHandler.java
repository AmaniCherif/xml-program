package com.adelean.elasticsearch.xml.sax.handler;

import com.adelean.elasticsearch.indexer.xml.ElasticsearchClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Getter
public class ProductHandler extends DefaultHandler {
    private static final String PRODUCTS_ELASTIC_INDEX = "products";
    private final Map<String, String> elementValues = new HashMap<>();
    private String currentElement = "";

    private final ElasticsearchClient elasticsearchClient;

    public ProductHandler(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentElement = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        currentElement = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (!currentElement.isEmpty()) {
            elementValues.put(currentElement, new String(ch, start, length));
        }
    }

    @Override
    public void endDocument() {
        elementValues.forEach((key, value) -> log.info(key + ": {}", value));
        indexDocument();
    }

    private void indexDocument() {
        String index = PRODUCTS_ELASTIC_INDEX;
        String id = UUID.randomUUID().toString();
        elasticsearchClient.indexDocument(index, id, elementValues);
    }
}