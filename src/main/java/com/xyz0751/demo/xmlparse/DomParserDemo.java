package com.xyz0751.demo.xmlparse;

import com.alibaba.fastjson.JSON;
import com.xyz0751.demo.xmlparse.constants.FileName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用DOM进行解析
 * OM是html和xml的应用程序接口(API)，以层次结构（类似于树型）来组织节点和信息片段，映射XML文档的结构，允许获取
 * 和操作文档的任意部分，是W3C的官方标准
 * 	【优点】
 * 		①允许应用程序对数据和结构做出更改。
 * 		②访问是双向的，可以在任何时候在树中上下导航，获取和操作任意部分的数据。
 * 	【缺点】
 * 		①通常需要加载整个XML文档来构造层次结构，消耗资源大。
 * @author YY
 */
public class DomParserDemo {
    private static Logger log = LogManager.getLogger(DomParserDemo.class);

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        dbf.setValidating(true);
        dbf.setIgnoringComments(true);

        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException exception) throws SAXException {
                log.warn(exception.toString());
                throw new SAXException(exception.toString());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                log.error(exception.toString());
                throw new SAXException(exception.toString());
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                log.fatal(exception.toString());
                throw new SAXException(exception.toString());
            }
        });

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FileName.MYBATIS_CONFIGURATION)) {
            if (is == null) {
                log.error("加载文件失败！");
                return;
            }

            Document doc = db.parse(is);
            DocumentType doctype = doc.getDoctype();
            log.info("version: {}", doc.getXmlVersion());
            log.info("encoding: {}", doc.getXmlEncoding());

            log.info("doctype.getName(): {}" , doctype.getName());
            log.info("doctype.getEntities(): {}", doctype.getEntities());
            log.info("doctype.getInternalSubset(): {}", doctype.getInternalSubset());
            log.info("doctype.getNotations(): {}", doctype.getNotations());
            log.info("doctype.getPublicId(): {}", doctype.getPublicId());
            log.info("doctype.getSystemId(): {}", doctype.getSystemId());
            if(log.isInfoEnabled()) {
                log.info("doctype.getAttributes(): {}", JSON.toJSONString(doctype.getAttributes()));
            }

            log.info("doctype.getNodeName : {}", doctype.getNodeName());
            log.info("doctype.getNodeValue(): {}", doctype.getNodeValue());
            log.info("doctype.getChildNodes(): {}",doctype.getChildNodes());

            Element rootElement = doc.getDocumentElement();
            log.info("{} : {}", rootElement.getNodeName(), rootElement.getNodeValue());
            logAttrs(rootElement.getAttributes());
            logNode(rootElement.getChildNodes());
        }

    }

    private static void logAttrs(NamedNodeMap attrs) {
        if(attrs == null || attrs.getLength() <= 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("attributes: ");
        sb.append("\r\n");
        for(int i=0; i<attrs.getLength(); i++) {
            Node item = attrs.item(i);
            sb.append("    ");
            sb.append(item.getNodeName());
            sb.append(" : ");
            sb.append(item.getNodeValue());
        }
        sb.append("\r\n");
        if(log.isInfoEnabled()) {
            log.info(sb.toString());
        }
    }

    private static void logNode(NodeList nodeList) {
        if(nodeList == null || nodeList.getLength() <= 0) {
            return;
        }
        for(int i=0; i<nodeList.getLength(); i++) {
            Node item = nodeList.item(i);
            if(item.getNodeType() == Node.TEXT_NODE) {
                if(!"".equals(item.getNodeValue().trim())) {
                    log.info(item.getNodeValue());
                }
            } else if(item.getNodeType() == Node.ELEMENT_NODE) {
                log.info("{} :", item.getNodeName());
            } else {
                log.info("type:{}, name:{}, value:{}", item.getNodeType(), item.getNodeName(), item.getNodeValue());
            }

            logAttrs(item.getAttributes());

            if(item.hasChildNodes()) {
                logNode(item.getChildNodes());
            }
        }
    }
}
