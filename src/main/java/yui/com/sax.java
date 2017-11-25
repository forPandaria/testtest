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
    FileWriter abcp = null;
    FileWriter ibmp = null;

    {


        try {
            this.abcp = new FileWriter("ABC_COMP_SAX.xml", true);
            this.ibmp = new FileWriter("IBM_COMP_SAX.xml", true);
            //abcp = new PrintStream(new File("ABC_COMP_SAX.xml"));
            //ibmp = new PrintStream(new File("IBM_COMP_SAX.xml"));

            //ibmp = new PrintWriter(ibm);
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

        if(flag == 0) {
            try {
                this.abcp.write("<" + qName + ">");
                this.ibmp.write("<" + qName + ">");
                this.abcp.flush();
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==1){
            try {
                this.ibmp.write( "<"+qName+">");
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(flag==2){
            //abc.write("give me five\n");
            //this.abc.write( ("<"+qName+">\r\n"));
            try {
                this.abcp.write("<"+qName+">");
                this.abcp.flush();
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
                this.abcp.write( ("</"+qName+">") );
                this.ibmp.write( ("</"+qName+">") );
                this.abcp.flush();
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }

        if(flag==1){

            try {
                this.ibmp.write( ("</"+qName+">"));
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //

        }

        if(flag==2){

            try {
                this.abcp.write("</"+qName+">");
                this.abcp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //

        }


    }


    //只调用一次
    @Override
    public void endDocument() throws SAXException {
        try {
            abcp.close();
            ibmp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //调用多次
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String s = new String(ch, start, length);

        if(flag==1){
            try {
                //this.ibmp.write( new String(ch) );

                this.ibmp.write(s);
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        if(flag==2){
            try {

                //this.ibmp.write(s);
                this.abcp.write(s);
                this.abcp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
