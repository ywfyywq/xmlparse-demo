package com.xyz0751.demo.xmlparse;

import com.xyz0751.demo.xmlparse.constants.FileName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Dom4j是目前最流行、最好用的XML解析工具，解析XML的速度最快。
 * @author YY
 */
public class Dom4jParserDemo {
    private static Logger log = LogManager.getLogger(Dom4jParserDemo.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        SAXReader reader = new SAXReader();

        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(FileName.MYBATIS_CONFIGURATION)) {
            reader.setFeature("http://xml.org/sax/features/external-general-entities",false);
            reader.setFeature("http://xml.org/sax/features/external-parameter-entities",false);
            if(is == null) {
                log.error("打开文件失败！");
                return;
            }

            Document dom = reader.read(is);
            Element root = dom.getRootElement();

            log.info("{} : {}", root.getName(), root.getStringValue());
            logAttrs(root.attributes());
            logNode(root.elements());
        } catch (IOException | DocumentException | SAXException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void logAttrs(List<Attribute> attrs) {
        if(attrs == null || attrs.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("attributes: ");
        sb.append("\r\n");

        attrs.forEach(item -> {
            sb.append("    ");
            sb.append(item.getName());
            sb.append(" : ");
            sb.append(item.getValue());
        });

        sb.append("\r\n");
        if(log.isInfoEnabled()) {
            log.info(sb.toString());
        }
    }


    @SuppressWarnings({"unchecked"})
    private static void logNode(List<Element> elementList) {
        if(elementList == null || elementList.isEmpty()) {
            return;
        }

        for(Element item : elementList) {
            log.info("type:{}, name:{}, value:{}", item.getNodeType(), item.getName(), item.getStringValue());

            logAttrs(item.attributes());
            logNode(item.elements());
        }
    }
}
