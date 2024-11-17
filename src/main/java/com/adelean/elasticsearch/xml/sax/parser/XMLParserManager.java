package com.adelean.elasticsearch.xml.sax.parser;

import com.adelean.elasticsearch.config.ConfigLoader;
import com.adelean.elasticsearch.indexer.xml.ElasticsearchClient;
import com.adelean.elasticsearch.xml.sax.handler.ProductHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FilenameFilter;

@Slf4j
@Getter
public class XMLParserManager {
    protected SAXParser saxParser;
    protected ElasticsearchClient elasticsearchClient;

    public XMLParserManager() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            this.saxParser = factory.newSAXParser();

            ConfigLoader configLoader = new ConfigLoader("config/config.properties");
            this.elasticsearchClient = new ElasticsearchClient(configLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        File[] xmlFiles = getXmlFiles(directory);

        if (xmlFiles != null) {
            for (File xmlFile : xmlFiles) {
                log.info("Parsing file: {}", xmlFile.getName());
                parseXmlFile(xmlFile);
            }
        } else {
            log.info("No XML files found in the directory: : {}", directoryPath);
        }
    }

    private File[] getXmlFiles(File directory) {
        FilenameFilter xmlFilter = (dir, name) -> name.toLowerCase().endsWith(".xml");
        return directory.listFiles(xmlFilter);
    }

    private void parseXmlFile(File xmlFile) {
        try {
            ProductHandler handler = new ProductHandler(elasticsearchClient);
            saxParser.parse(xmlFile, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}