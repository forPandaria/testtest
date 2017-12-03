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

import org.w3c.dom.*;
import org.xml.sax.SAXException;
/**
 * Created by yui on 2017/11/24.
 */
public class dom {
    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {


        String XML_SINK_ABC= "ABC_COMP_DOM.xml";
        String XML_SINK_IBM = "IBM_COMP_DOM.xml";
        String XML_SOURCE = "ipo.xml";

        xmlProcessByDOM(XML_SINK_IBM, XML_SOURCE,"comp_name","IBM");
        xmlProcessByDOM(XML_SINK_ABC, XML_SOURCE,"comp_name","ABC");

        System.out.print("done");

    }



    public static boolean xmlProcessByDOM(String XML_SINK, String XML_SOURCE, String attr,String attr_value) throws TransformerException, ParserConfigurationException, IOException, SAXException {

        //获transformer，用来写入新的XML文件;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        //XML解析器db;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        //get DOMTree;
        Document document = db.parse(XML_SOURCE);

        Node purchaseOrders = document.getLastChild();
        NodeList purchaseOrdersChildNodes = purchaseOrders.getChildNodes();
        int length = purchaseOrdersChildNodes.getLength();


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
            if(comp_name==null) {
                continue;
            }

            //String nodeValue = comp_name.getNodeValue();
            //boolean b = nodeValue == attr_value;

            //i
            if(!comp_name.getNodeValue().equals(attr_value)){
                purchaseOrders.removeChild(purchaseOrder);

            }else {
                attributes.removeNamedItem(comp_name.getNodeName());
            }
        }

        attributes2Nodes(purchaseOrders);

        //将删之后得到的dom写入到IBM_COMP.XML
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(XML_SINK));
        transformer.transform(domSource, streamResult);
        return true;
    }

    private static void attributes2Nodes(Node purchaseOrders) {
        //if has childnodes, then apply
        NodeList childNodes = purchaseOrders.getChildNodes();
        int nodesLength = childNodes.getLength();
        for(int i=0;i<nodesLength;i++){
            attributes2Nodes(childNodes.item(i));
        }


        //change attributes as childnodes;
        NamedNodeMap attributes = purchaseOrders.getAttributes();
        if(attributes!=null){
            Node firstChild = purchaseOrders.getFirstChild();
            int length = attributes.getLength();
            Document document = purchaseOrders.getOwnerDocument();

            for(int i=0;i<length;i++){
                Node item = attributes.item(0);
                Node textNode = document.createElement(item.getNodeName());
                Text textNode1 = document.createTextNode(item.getNodeValue());
                textNode.appendChild(textNode1);

                purchaseOrders.insertBefore(textNode,firstChild);

                attributes.removeNamedItem(item.getNodeName());


            }



        }


    }


}
