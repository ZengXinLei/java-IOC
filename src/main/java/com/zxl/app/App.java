package com.zxl.app;


import com.zxl.zxlframework.xmlfactory.context.support.XmlReader;

/**
 * Hello world!
 *
 */
public class App extends AppFather
{

    public static void main(String[] args) {
//                AtxMarkdownToc.newInstance().charset("UTF-8").genTocFile("F:\\all\\github\\Java-IOC\\README.md");
        B b =new XmlReader("classpath:application.xml").getAttribute("b");

        System.out.println(b.toString());
    }
}
