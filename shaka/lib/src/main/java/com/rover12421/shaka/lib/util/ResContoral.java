package com.rover12421.shaka.lib.util;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResContoral {
    public static HashMap<Integer, String> mOrgMap;
    public static HashMap<Integer, PublicXmlParamer> publicXmlParamerHashMap;
    public static HashMap<String, List<PublicXmlParamer>> publicTypeXmlParamerHashMap;
    public static FileWriter writer;
    public static int index = 0;
    static String fileName = "C:\\Users\\guzhu\\Desktop\\qqmail\\log.txt";
    public static final String outFile = "C:\\Users\\guzhu\\Desktop\\qqmail\\Apk\\outDir";

    public static void init() throws Exception {
        if (mOrgMap == null) {
            mOrgMap = new HashMap<>();
        }
        if (publicXmlParamerHashMap == null) {
            publicXmlParamerHashMap = new HashMap<>();
        }
        if (publicTypeXmlParamerHashMap == null) {
            publicTypeXmlParamerHashMap = new HashMap<>();
        }
        if (writer == null) {

            writer = new FileWriter(fileName, true);
        }
        mOrgMap.clear();
        Object bject = new WXRR();
        Class<?>[] classs = bject.getClass().getClasses();
        int classsLength = classs.length;
        for (int i = 0; i < classsLength; i++) {
            Class clz = classs[i];
            injectValue(clz);
        }
//        File publicXml = new File("C:\\Users\\guzhu\\Desktop\\weixin\\out\\res\\values\\public.xml");
//        InputStream in = new FileInputStream(publicXml);
//        XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
//        //2.设置XmlPullParser的参数
//        parser.setInput(in, "utf-8");
//        //3.获取时间类型
//        int node = parser.getEventType();
//        while (node != XmlPullParser.END_DOCUMENT) {
//            switch (node) {
//                //4.具体判断下解析到了哪个开始标签
//                case XmlPullParser.START_TAG://开始解析标签
//                    if ("public".equals(parser.getName())) {
//                        parser.getAttributeCount();
//                        String type = parser.getAttributeValue(0);
//                        String name = parser.getAttributeValue(1);
//                        String id = parser.getAttributeValue(2);
//                        PublicXmlParamer publicXmlParamer = new PublicXmlParamer();
//                        Integer x = Integer.parseInt(id.substring(2), 16);
//                        publicXmlParamer.id = x;
//                        publicXmlParamer.old_name = name;
//                        publicXmlParamer.type = type;
//                        String path = "";
//                        if ("drawable".equals(type)) {
//                            path = "res/drawable/";
//                        }
//                        if ("anim".equals(type)) {
//                            path = "res/anim/";
//                        }
//                        if ("animator".equals(type)) {
//                            path = "res/animator/";
//                        }
//                        if ("layout".equals(type)) {
//                            path = "res/layout/";
//                        }
//                        if ("xml".equals(type)) {
//                            path = "res/xml/";
//                        }
//                        publicXmlParamer.path = path;
//                        publicXmlParamerHashMap.put(publicXmlParamer.id, publicXmlParamer);
//
//                        List<PublicXmlParamer> publicXmlParamers;
//                        if(!publicTypeXmlParamerHashMap.containsKey(type)){
//                            publicXmlParamers = new ArrayList<>();
//                            publicTypeXmlParamerHashMap.put(type,publicXmlParamers);
//                        }
//                        publicXmlParamers = publicTypeXmlParamerHashMap.get(type);
//                        publicXmlParamers.add(publicXmlParamer);
//                    }
//                    break;
//                case XmlPullParser.END_TAG:  //解析结束标志
//                    //判断要解析的结束标签
//                    if ("channel".equals(parser.getName())) {
//                        //把javabean对象存到集合中
////                        publicXmlParamerHashMap.put(channel);
//                    }
//
//            }
//            //不停的向下解析
//            node = parser.next();
//        }

        for (Map.Entry<Integer, PublicXmlParamer> entry : publicXmlParamerHashMap.entrySet()) {
            int id = entry.getKey();
            PublicXmlParamer paramer = entry.getValue();
            if (mOrgMap.containsKey(id)) {
                paramer.new_name = mOrgMap.get(id);
            }
//            writeLOG(paramer.toString());
        }

    }


    private static void injectValue(Class classz) throws IllegalAccessException {
        Field[] mFields = classz.getFields();
        if (null != mFields) {
            int length = mFields.length;
            for (int i = 0; i < length; i++) {
                Field field = mFields[i];
                String type = field.getType().getSimpleName();
                if ("int".equals(type)) {
                    String name = field.getName();
                    int value = (int) field.get(classz);
                    mOrgMap.put(value, name);
                } else if ("int[]".equals(type)) {

                } else {
//                    writeLOG(type);
                }
            }

        }

    }

    /**
     * 使用FileWriter类写文本文件
     */
    public synchronized static void writeLOG(String msg) {
        try {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则先把这个文件给删除掉，然后创建新的kuka.txt
            if (index >= 500) {
                writer.close();
                writer = new FileWriter(fileName, true);
                index = 0;
            }
            writer.write(msg + "\n");
            index++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PEnd() {
        writeLOGClose();
        System.exit(0);
    }

    public static void writeLOGClose() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getName(int value) {
        if (mOrgMap.containsKey(value)) {
            return mOrgMap.get(value);
        }
        return null;
    }
}
