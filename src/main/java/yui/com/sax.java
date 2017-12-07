package yui.com;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;


/**
 * Created by yui on 2017/11/25.
 */
public class sax {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        //构建SAXParser；
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

        //实例化  DefaultHandler对象；
        SaxParseXml parseXml = new SaxParseXml();
        //加载资源文件 转化为一个输入流；
        InputStream stream = new FileInputStream("ipo.xml");

        //调用parse()方法；
        parser.parse(stream, parseXml);

        //程序运行结束；
        System.out.print("sax_done");
    }
}


class SaxParseXml extends DefaultHandler {

    private int flag = 0;
    FileWriter abcp = null;
    FileWriter ibmp = null;

    {
        try {
            this.abcp = new FileWriter("ABC_COMP_SAX.xml", false);
            this.ibmp = new FileWriter("IBM_COMP_SAX.xml", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 通用的写入方法，根据传入的writerID，选用不同xml文件的writer，以便写入不同的内容；
    * */
    private void startElementWrite(String writerID,String localName, String qName, Attributes attributes){
        FileWriter writer =null;

        //根据writerID，选取相应xml文件的writer；
        if(writerID.equals("ABC"))writer= this.abcp;
        else writer=ibmp;

        try {
            writer.write("<" + qName + ">");

            //在purchaseOrders标签下加入<ABC_COMP>或<IBM_COMP>子标签的开始部分；
            if(qName.equals("purchaseOrders")) {
                if (writerID.equals("ABC")) writer.write("<ABC_COMP>");
                else writer.write("<IBM_COMP>");
            }

            //将节点的元素转化为其子节点；
            if(attributes!=null){
                int length = attributes.getLength();
                for(int i=0;i<length;i++){
                    String qName1 = attributes.getQName(i);
                    if(qName1 == "comp_name") continue;
                    String value = attributes.getValue(i);
                    writer.write("<" + qName1 +">" + value + "</" + qName1 +">" );
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void startDocument() throws SAXException {
        try {
            this.abcp.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            this.ibmp.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
            this.abcp.flush();
            this.ibmp.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        //输出节点的属性；
        if(attributes!=null){
            int length = attributes.getLength();
            for(int i=0;i<length;i++){
                String qName1 = attributes.getQName(i);
                String value = attributes.getValue(i);
                System.out.println(qName1+ ":" + value);
            }

            String comp_name = attributes.getValue("", "comp_name");

            if(comp_name!=null && comp_name.equals("IBM")){
                flag = 1;
            }else if (comp_name!=null && comp_name.equals("ABC")){
                flag = 2;
            }

        }

        //flag==0，表示该标签需要写入两个文件；
        if(flag == 0) {
            startElementWrite("ABC",localName,qName,attributes);
            startElementWrite("IBM",localName,qName,attributes);
        }

        //flag==1，表示该标签是IBM的内容；
        if(flag==1){
            startElementWrite("IBM",localName,qName,attributes);

        }

        //flage==2，表示该标签是ABC的内容；
        if(flag==2){
            startElementWrite("ABC",localName,qName,attributes);

        }
    }



    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        //如果该结束标签是purchaseOrders，则需要写入两个文件，并且在其之前加上</ABC_COMP>或<IBM_COMP>;
        //否则，根据flage，判断该标签属于ABC或IBM内容，并将其写入相应文件；
        if(qName.equals("purchaseOrders")){

            try {
                this.abcp.write( ("</ABC_COMP></"+qName+">") );
                this.ibmp.write( ("</IBM_COMP></"+qName+">") );
                this.abcp.flush();
                this.ibmp.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

            if (flag == 1) {

                try {
                    this.ibmp.write(("</" + qName + ">"));
                    this.ibmp.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //

            }

            if (flag == 2) {

                try {
                    this.abcp.write("</" + qName + ">");
                    this.abcp.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //

            }
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
