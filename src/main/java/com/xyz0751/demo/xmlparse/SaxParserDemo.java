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

/**
 * 优点：
 *     a、无需将整个xml文档载入内存，因此消耗内存少
 *     b、可以继承ContentHandler创建多个执行不同查询的listener进行解析操作
 * 缺点：
 *     a、不能随机的访问xml中的节点
 *     b、不能修改文档
 *     c、查询要对XML文档从头到尾遍历一次
 *
 * @author YY
 */
public class SaxParserDemo extends DefaultHandler {
    private static Logger log = LogManager.getLogger(SaxParserDemo.class);

    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = saxParserFactory.newSAXParser();
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FileName.MENU)) {
                if(is != null) {
                    parser.parse(is, new SaxParserDemo());
                } else {
                    log.error("打不开文件！");
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        log.info("startElement: uri[{}], localName[{}], qName[{}]", uri, localName, qName);
        for(int i=0; i<attributes.getLength(); i++) {
            log.info("attributes: uri[{}], localName[{}], qName[{}], type[{}]",
                    attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        log.info("endElement: uri[{}], localName[{}], qName[{}]", uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String str = new String(ch, start, length);
        if (!"".equals(str.trim())) {
            log.info(str);
        }
    }

}
