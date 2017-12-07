package yui.com;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class jdom {

    public static void main(String[] args) throws IOException, JDOMException {

        String XML_SINK_ABC= "ABC_COMP_JDOM.xml";
        String XML_SINK_IBM = "IBM_COMP_JDOM.xml";
        String XML_SOURCE = "ipo.xml";


        //将XML_SOURCE中comp_name=filterString的订单写入到XML_SINK文件中；
        xmlProcessByJdom(XML_SINK_ABC, XML_SOURCE, "ABC");
        xmlProcessByJdom(XML_SINK_IBM, XML_SOURCE, "IBM");

        System.out.print("dom4j_done!");
    }


    /*
    * 读取XML_SOURCE文件，构建dom树；
    * 将comp_name=filterString的订单写入XML_SINK文件里；
    * */
    private static boolean xmlProcessByJdom(String XML_SINK, String XML_SOURCE, String filterString) throws JDOMException, IOException {


        //创建SAXBuilder实例，用其读取XML_SOURCE文件并构建相应dom树；
        SAXBuilder sax = new SAXBuilder();
        Document document = sax.build(XML_SOURCE);



        //删掉属性comp_name不为filterString的purchaseOrder节点；
        Element rootElement = document.getRootElement();
        List<Element> elementList = XPath.selectNodes(rootElement,"//purchaseOrder");
        int sizeOfIBM = elementList.size();

        for(int i=0;i<sizeOfIBM;i++){
            Element element = elementList.get(i);
            Element parent = (Element) element.getParent();

            Attribute comp_name = element.getAttribute("comp_name");
            if(!comp_name.getValue().equals(filterString))parent.removeContent(element);
            element.removeAttribute(comp_name);
        }




        //将节点中的属性全转变为其前排子节点；
        List<Element> list = XPath.selectNodes(rootElement,"//*[@*]");
        Iterator<Element> iterator = list.iterator();
        while (iterator.hasNext()){

            Element next = (Element)iterator.next();

            List<Attribute> attributes = XPath.selectNodes(next, "@*");
            int size = attributes.size();
            for (int i=0;i<size;i++){
                Attribute node = attributes.get(i);

                Element element = new Element(node.getName());
                element.setText(node.getValue());

                next.addContent(i,element);

                node.getParent().removeAttribute(node.getName());

            }

        }




        //创建COMP节点；
        //将purchaseOrders下的子节点全转移到COMP节点下；
        //再将COMP节点添加到purchaseOrders节点下；
        Element insertElement = new Element(filterString + "_COMP");

        List<Element> purchaseOrdersChildNodes_2 = XPath.selectNodes(document, "//purchaseOrder");
        int len = purchaseOrdersChildNodes_2.size();
        Element item = null;
        for (int i=0; i< len;i++){
            item = purchaseOrdersChildNodes_2.get(i);

            if(item!=null){
                insertElement.addContent((Element)item.clone());
                //insertElement.getContent().add(item);
                item.getParent().removeContent(item);
            }
        }
        List<Element> purchaseOrdersList = XPath.selectNodes(document, "purchaseOrders");
        Element purchaseOrders = purchaseOrdersList.get(0);
        purchaseOrders.getContent().add(insertElement);



        //创建XMLOutputter实例，用其将dom树写入到XML_SINK文件中；
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(document,new FileWriter(XML_SINK));
        return true;
    }
}
