package yui.com;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.*;


/**
 * Created by yui on 2017/11/25.
 */
public class sax {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        //StreamResult streamResult = new StreamResult(new File("23.txt"));
        SAXParser parser = null;

        //构建SAXParser
        parser = SAXParserFactory.newInstance().newSAXParser();
        //实例化  DefaultHandler对象
        SaxParseXml parseXml = new SaxParseXml();
        //加载资源文件 转化为一个输入流
        InputStream stream = new FileInputStream("ipo.xml");
        //调用parse()方法
        parser.parse(stream, parseXml);
        //遍历结果
        System.out.print("sax_done");

    }
}


class SaxParseXml extends DefaultHandler {

    private int flag = 0;
    FileWriter abc = null;
    FileWriter ibm = null;
    {


        try {
            this.abc = new FileWriter("ABC_COMP_SAX.xml", true);
            this.ibm = new FileWriter("IBM_COMP_SAX.xml", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void startDocument() throws SAXException {

    }



    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if(attributes!=null){
            String comp_name = attributes.getValue("", "comp_name");
            if(comp_name!=null && comp_name.equals("IBM")){
                flag = 1;
            }else if (comp_name!=null && comp_name.equals("ABC")){
                flag = 2;
            }

        }

        if(flag == 0){
            try {

                this.abc.write( "<"+qName+">");
                this.ibm.write( "<"+qName+">");
                this.abc.flush();
                this.ibm.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==1){
            try {
                this.ibm.write( "<"+qName+">");
                this.ibm.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==2){
            try {
                this.abc.write( ("<"+qName+">\r\n"));
                this.abc.flush();
                System.out.print("<"+qName+">");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }



    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(qName.equals("purchaseOrders")) flag=0;

        if(flag == 0){
            try {
                this.abc.write( ("</"+qName+">") );
                this.ibm.write( ("</"+qName+">") );
                this.abc.flush();
                this.ibm.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==1){
            try {
                this.ibm.write( ("</"+qName+">"));
                this.ibm.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==2){
            try {
                this.abc.write( ("</"+qName+">"));
                this.abc.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    //只调用一次
    @Override
    public void endDocument() throws SAXException {
    }

    //调用多次
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if(flag==1){
            try {
                this.ibm.write( ch.toString());
                this.ibm.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==2){
            try {
                this.abc.write( ch.toString());
                this.abc.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
