package com.zxl.app;


import com.zxl.zxlframework.xmlFactory.context.support.XMLReader;

/**
 * Hello world!
 *
 */
public class App extends AppFather
{

    public static void main(String[] args) {
//                AtxMarkdownToc.newInstance().charset("UTF-8").genTocFile("F:\\all\\github\\Java-IOC\\README.md");
        B b =new XMLReader("classpath:example.xml").getBean("b");

        System.out.println(b.toString());
    }
}
