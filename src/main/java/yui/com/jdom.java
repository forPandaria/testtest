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

        xmlProcessByJdom(XML_SINK_IBM, XML_SOURCE, "//purchaseOrder[@comp_name=\"ABC\"]");
        xmlProcessByJdom(XML_SINK_ABC, XML_SOURCE, "//purchaseOrder[@comp_name=\"IBM\"]");

        System.out.print("dom4j_done!");
    }


    private static boolean xmlProcessByJdom(String XML_SINK, String XML_SOURCE, String filterString) throws JDOMException, IOException {

        SAXBuilder sax = new SAXBuilder();

        Document document = sax.build(XML_SOURCE);



        Element rootElement = document.getRootElement();
        List<Element> elementList = XPath.selectNodes(rootElement,filterString);
        int sizeOfIBM = elementList.size();

        for(int i=0;i<sizeOfIBM;i++){
            Element element = elementList.get(i);
            Element parent = (Element) element.getParent();
            parent.removeContent(element);
        }

        //attributes2Nodes(rootElement);
        List<Element> list = XPath.selectNodes(rootElement,"//*[@*]");
        Iterator<Element> iterator = list.iterator();
        while (iterator.hasNext()){

            Element next = (Element)iterator.next();
           // List<Element> content = next.content();


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


        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(document,new FileWriter(XML_SINK));
        return true;
    }
}
