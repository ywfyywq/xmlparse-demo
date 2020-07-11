package com.xyz0751.demo.xmlparse;

import com.xyz0751.demo.xmlparse.constants.FileName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

public class SAXParserDemo extends DefaultHandler {
    private static Logger log = LogManager.getLogger(SAXParserDemo.class);

    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FileName.MENU);) {
                if(is != null) {
                    parser.parse(is, new SAXParserDemo());
                } else {
                    log.error("打不开文件！");
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        log.info("startElement: uri[{}], localName[{}], qName[{}]", uri, localName, qName);
        for(int i=0; i<attributes.getLength(); i++) {
            log.info("attributes: uri[{}], localName[{}], qName[{}], type[{}]",
                    attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        log.info("endElement: uri[{}], localName[{}], qName[{}]", uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        if (!"".equals(str.trim())) {
            log.info(str);
        }
    }

}
