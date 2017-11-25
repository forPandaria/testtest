package yui.com;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
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

        //获取转换器transformer，用来写入新的XML文件
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //获取XML解析器db
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();


        /*
        * 解析ipo.xml, 得到IBM_COMP.xml
        *
        *
        *
        * */

        Document IBM_document = db.parse("ipo.xml");

        Node purchaseOrders = IBM_document.getLastChild();
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
            Node comp_name = attributes.getNamedItem("comp_name");
            String nodeValue = comp_name.getNodeValue();
            boolean b = nodeValue == "ABC";

            if(comp_name.getNodeValue().equals("ABC")){
                purchaseOrders.removeChild(purchaseOrder);
            }
        }

        //将删除ABC之后得到的dom写入到IBM_COMP.XML
        DOMSource IBM_domSource = new DOMSource(IBM_document);
        StreamResult IBM_streamResult = new StreamResult(new File("IBM_COMP.xml"));
        transformer.transform(IBM_domSource, IBM_streamResult);


        /*
        * 解析ipo.xml，得到ABC_COMP.xml
        *
        *
        *
        * */


        Document ABC_document2 = db.parse("ipo.xml");

        Node ABC_purchaseOrders = ABC_document2.getLastChild();
        NodeList ABC_purchaseOrdersChildNodes = ABC_purchaseOrders.getChildNodes();
        int ABC_length = ABC_purchaseOrdersChildNodes.getLength();

        //遍历purchaseOrders的子节点，删除comp_name="IBM"的
        for(int i=0; i < length;i++){

            Node purchaseOrder = ABC_purchaseOrdersChildNodes.item(i);
            if(purchaseOrder==null) {
                continue;
            }

            NamedNodeMap attributes = purchaseOrder.getAttributes();
            if(attributes==null) {
                continue;
            }
            Node comp_name = attributes.getNamedItem("comp_name");
            String nodeValue = comp_name.getNodeValue();
            boolean b = nodeValue == "IBM";

            if(comp_name.getNodeValue().equals("IBM")){
                ABC_purchaseOrders.removeChild(purchaseOrder);
            }
        }

        //将删除IBM之后得到的dom写入到ABC_COMP.XML
        DOMSource ABC_domSource = new DOMSource(ABC_document2);
        StreamResult ABC_streamResult = new StreamResult(new File("ABC_COMP.xml"));
        transformer.transform(ABC_domSource, ABC_streamResult);


        System.out.print("done");


    }
}
