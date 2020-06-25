package com.zxl.util;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author: zxl
 * @Time: 2020/06/20 10:25:17
 * @system: ASUS
 **/
public class Util {
    public static ArrayList<Class<?>> getClasses(){
        String path=ClassLoader.getSystemResource("").getPath();
        ArrayList<Class<?>> classes=new ArrayList<>();
        File file[]=new File(path).listFiles();

        for (File f :
                file) {
            if(!f.isDirectory()){
                if(f.getName().split("\\.").length<=1){
                    continue;
                }else if(f.getName().split("\\.")[1].equals("class")){
                    try {
                        classes.add(Class.forName(f.getName().split("\\.")[0]));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }else {
                //递归遍历
                directoryTraversal(path,f.getName(),classes);
            }
        }

        return classes;
    }
    public static void directoryTraversal(String filePath,String path, ArrayList<Class<?>> className){

        File file[]=new File(filePath+"\\"+path).listFiles();
        for (File f:
                file){
            if(f.isDirectory()){
                directoryTraversal(filePath,path+"\\"+f.getName(),className);
            }else {
                if(f.getName().split("\\.").length<=1){
                    continue;
                }else if(f.getName().split("\\.")[1].equals("class")){
                    try {
                        className.add(Class.forName(path.replaceAll("(\\\\)",".")+"."+f.getName().split("\\.")[0]));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
