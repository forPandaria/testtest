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
 *
 * Created by yui on 2017/11/24.
 */
public class dom {
    public static void main(String[] args) throws TransformerException, ParserConfigurationException, IOException, SAXException {


        String XML_SINK_ABC= "ABC_COMP_DOM.xml";
        String XML_SINK_IBM = "IBM_COMP_DOM.xml";
        String XML_SOURCE = "ipo.xml";

        //调用两次xmlProcessByDOM方法，分别生成两个目标xml文件；
        xmlProcessByDOM(XML_SINK_IBM, XML_SOURCE,"comp_name","IBM");
        xmlProcessByDOM(XML_SINK_ABC, XML_SOURCE,"comp_name","ABC");

        System.out.print("DOM done");

    }



    /*
    * 输入参数：
    * String  XML_SINK : 生成的目标xml文件名；
    * String  XML_SOURCE : 源xml文件名；
    * String  attr : order分类所依据的属性名；
    * String attr_value : 目标order的属性值；
    *
    * */
    public static boolean xmlProcessByDOM(String XML_SINK, String XML_SOURCE, String attr,String attr_value) throws TransformerException, ParserConfigurationException, IOException, SAXException {



        //获取XML解析器db;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        //解析源xml文件，构建DOM树；
        Document document = db.parse(XML_SOURCE);
        //获取xml根节点；
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

            if(!comp_name.getNodeValue().equals(attr_value)){
                purchaseOrders.removeChild(purchaseOrder);

            }else {
                attributes.removeNamedItem(comp_name.getNodeName());
            }
        }



        //调用方法，将所有节点的属性都转化为其前排子节点；
        attributes2Nodes(purchaseOrders);



        //创建新节点insertNode；
        //将purchaseOrders下面的purchaseOrder节点转成insertNode的子节点；
        //将insertNode节点添加为purchaseOrders的子节点；
        Node insertNode = document.createElement(attr_value + "_COMP");

        NodeList purchaseOrdersChildNodes_2 = document.getElementsByTagName("purchaseOrder");
        int len = purchaseOrdersChildNodes_2.getLength();
        Node item = null;
        for (int i=0; i< len;i++){
             item = purchaseOrdersChildNodes_2.item(0);
            if(item!=null){
                insertNode.appendChild(item);
            }
        }

        purchaseOrders.appendChild(insertNode);



        //获transformer，用来写入新的XML文件;
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();


        //将删之后得到的dom写入到IBM_COMP.XML
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(XML_SINK));
        transformer.transform(domSource, streamResult);
        return true;
    }


    /*
    * 将以输入节点为根的树的所有属性转变为其子节点，并插入其原先子节点的前面；
    * */
    private static void attributes2Nodes(Node targetNode) {
        //if has childnodes, then apply
        NodeList childNodes = targetNode.getChildNodes();
        int nodesLength = childNodes.getLength();
        for(int i=0;i<nodesLength;i++){
            attributes2Nodes(childNodes.item(i));
        }


        //尝试获取当前节点的属性，如果该节点有属性，则将其全部转换为该节点的子节点，并插在其原子节点之前；
        NamedNodeMap attributes = targetNode.getAttributes();
        if(attributes!=null){
            Node firstChild = targetNode.getFirstChild();
            int length = attributes.getLength();
            Document document = targetNode.getOwnerDocument();

            for(int i=0;i<length;i++){
                Node item = attributes.item(0);
                Node textNode = document.createElement(item.getNodeName());
                Text textNode1 = document.createTextNode(item.getNodeValue());
                textNode.appendChild(textNode1);

                targetNode.insertBefore(textNode,firstChild);

                attributes.removeNamedItem(item.getNodeName());


            }



        }


    }


}
