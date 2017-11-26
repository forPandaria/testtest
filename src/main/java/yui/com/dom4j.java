package yui.com;


import org.dom4j.Document;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
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

        List<Element> elementList = document.selectNodes(filterString);
        int sizeOfIBM = elementList.size();

        for(int i=0;i<sizeOfIBM;i++){
            Element element = elementList.get(i);
            Element parent = element.getParent();
            parent.remove(element);
        }

        String asXML = document.asXML();
        FileWriter fileWriter = new FileWriter(XML_SINK);
        fileWriter.write(asXML);
        fileWriter.flush();
        fileWriter.close();
        return true;
    }
}
