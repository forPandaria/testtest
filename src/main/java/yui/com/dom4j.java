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


        xmlProcessByDom4j(XML_SINK_IBM, XML_SOURCE,"//purchaseOrder[@comp_name=\"ABC\"]");
        xmlProcessByDom4j(XML_SINK_ABC, XML_SOURCE,"//purchaseOrder[@comp_name=\"IBM\"]");

        System.out.print("dom4j_done!");


    }

    private static boolean xmlProcessByDom4j(String XML_SINK, String XML_SOURCE, String filterString) throws DocumentException, IOException {

        SAXReader reader = new SAXReader();
        Document document = null;

        document = reader.read( XML_SOURCE );
        Element rootElement = document.getRootElement();

        List<Element> elementList = document.selectNodes(filterString);
        int sizeOfIBM = elementList.size();

        for(int i=0;i<sizeOfIBM;i++){
            Element element = elementList.get(i);
            Element parent = element.getParent();
            parent.remove(element);
        }

        //attributes2Nodes(rootElement);
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


        String asXML = document.asXML();
        FileWriter fileWriter = new FileWriter(XML_SINK);
        fileWriter.write(asXML);
        fileWriter.flush();
        fileWriter.close();
        return true;

    }


}
