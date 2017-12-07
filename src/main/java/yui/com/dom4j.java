package yui.com;


import org.dom4j.*;

import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class dom4j {

    public static void main(String[] args) throws TransformerConfigurationException, ParserConfigurationException, IOException, SAXException, DocumentException {

        String XML_SINK_ABC= "ABC_COMP_DOM4J.xml";
        String XML_SINK_IBM = "IBM_COMP_DOM4J.xml";
        String XML_SOURCE = "ipo.xml";


        //将XML_SOURCE中comp_name=filterString的订单写入到XML_SINK文件中；
        xmlProcessByDom4j(XML_SINK_IBM, XML_SOURCE,"IBM");
        xmlProcessByDom4j(XML_SINK_ABC, XML_SOURCE,"ABC");

        System.out.print("dom4j_done!");
    }



    /*
    * 读取XML_SOURCE文件，构建dom树；
    * 将comp_name=filterString的订单写入XML_SINK文件里；
    * */
    private static boolean xmlProcessByDom4j(String XML_SINK, String XML_SOURCE, String filterString) throws DocumentException, IOException {

        //用SAXReader读取XML_SOURCE，并构建dom树；
        SAXReader reader = new SAXReader();
        Document document = reader.read( XML_SOURCE );



        //删掉属性comp_name不为filterString的purchaseOrder节点；
        List<Element> elementList = document.selectNodes("//purchaseOrder");
        int sizeOfIBM = elementList.size();

        for(int i=0;i<sizeOfIBM;i++){
            Element element = elementList.get(i);
            Element parent = element.getParent();

            Attribute comp_name = element.attribute("comp_name");
            if(!comp_name.getValue().equals(filterString))parent.remove(element);
            element.remove(comp_name);
        }



        //将节点中的属性全转变为其前排子节点；
        List<Element> list = document.selectNodes("//*[@*]");
        Iterator iterator = list.iterator();
        while (iterator.hasNext()){

            Element next = (Element)iterator.next();
            List<Element> content = next.content();

            List<Element> attributes = next.selectNodes("@*");
            int size = attributes.size();
            for (int i=0;i<size;i++){
                Node node = attributes.get(i);
                //Entity entity = DocumentHelper.createEntity(node.getName(), node.getText());
                //next.addElement(node.getName(), node.getText());
                Element element = DocumentHelper.createElement(node.getName());
                element.addText(node.getText());
                content.add(i,element);
                node.getParent().remove(node);
            }

        }



        //创建COMP节点；
        //将purchaseOrders下的子节点全转移到COMP节点下；
        //再将COMP节点添加到purchaseOrders节点下；
        List<Element> list1 = document.selectNodes("/purchaseOrders");
        Element purchaseOrders = list1.get(0);

        Element insertedNode = DocumentHelper.createElement(filterString + "_COMP");

        List<Node> purchaseOrdersChildNodes_2 = document.selectNodes("//purchaseOrder");
        int len = purchaseOrdersChildNodes_2.size();
        Node item = null;
        for (int i=0; i< len;i++){
            Node node = purchaseOrdersChildNodes_2.get(i);
            item = (Node) node.clone();
            if(item!=null){
                insertedNode.add(item);
            }
            purchaseOrders.remove(node);
        }

        list1.get(0).add(insertedNode);


        //将dom树写入到XML_SINK文件中；
        String asXML = document.asXML();
        FileWriter fileWriter = new FileWriter(XML_SINK);
        fileWriter.write(asXML);
        fileWriter.flush();
        fileWriter.close();
        return true;

    }


}
