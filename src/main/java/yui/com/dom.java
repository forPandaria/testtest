package yui.com;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * Created by yui on 2017/11/24.
 */
public class dom {
    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {


        String XML_SINK_ABC= "ABC_COMP_DOM.xml";
        String XML_SINK_IBM = "IBM_COMP_DOM.xml";
        String XML_SOURCE = "ipo.xml";

        xmlProcessByDOM(XML_SINK_IBM, XML_SOURCE,"comp_name","ABC");
        xmlProcessByDOM(XML_SINK_ABC, XML_SOURCE,"comp_name","IBM");

        System.out.print("done");

    }



    public static boolean xmlProcessByDOM(String XML_SINK, String XML_SOURCE, String attr,String attr_value) throws TransformerException, ParserConfigurationException, IOException, SAXException {

        //获transformer，用来写入新的XML文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //XML解析器db
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document document = db.parse(XML_SOURCE);

        Node purchaseOrders = document.getLastChild();
        NodeList purchaseOrdersChildNodes = purchaseOrders.getChildNodes();
        int length = purchaseOrdersChildNodes.getLength();

        //遍历purchaseOrders的子节点，删除comp_name="ABC"的
        for(int i=0; i < length;i++){

            Node purchaseOrder = purchaseOrdersChildNodes.item(i);
            if(purchaseOrder==null) {
                continue;
            }

            NamedNodeMap attributes = purchaseOrder.getAttributes();
            if(attributes==null) {
                continue;
            }
            Node comp_name = attributes.getNamedItem(attr);
            String nodeValue = comp_name.getNodeValue();
            boolean b = nodeValue == attr_value;

            if(comp_name.getNodeValue().equals(attr_value)){
                purchaseOrders.removeChild(purchaseOrder);
            }
        }

        //将删之后得到的dom写入到IBM_COMP.XML
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(XML_SINK));
        transformer.transform(domSource, streamResult);
        return true;
    }
}
