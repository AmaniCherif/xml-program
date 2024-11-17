package com.adelean.elasticsearch;

import com.adelean.elasticsearch.xml.sax.parser.XMLParserManager;

import java.io.IOException;

import static com.adelean.elasticsearch.zip.ZipExtractor.unzip;

public class XMLFileProcessor {
    /**
     * String sourceFilePath = "src/main/resources/input/zip/xml.zip";
     * String targetDir = "src/main/resources/output/xml";
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java XMLFileProcessor <sourceFilePath> <targetDir>");
            return;
        }

        String sourceFilePath = args[0];
        String targetDir = args[1];

        unzip(sourceFilePath, targetDir);

        XMLParserManager manager = new XMLParserManager();

        manager.parseDirectory(targetDir);

        manager.getElasticsearchClient().close();
    }
}