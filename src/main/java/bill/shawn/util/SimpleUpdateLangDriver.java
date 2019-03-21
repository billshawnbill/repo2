package bill.shawn.util;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleUpdateLangDriver extends XMLLanguageDriver implements LanguageDriver {
    //定义正则对象
    private final Pattern inPatter=Pattern.compile("\\(#\\{\\w+\\}\\)");
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        //script传入的参数
        Matcher matcher = inPatter.matcher(script);
        //判断是否含有该字段
        if(matcher.find()){
            //创建StringBuilder拼接字段
            StringBuilder sb = new StringBuilder();
            //利用反射遍历该字段
            for (Field field : parameterType.getDeclaredFields()) {
                if(!(field.isAnnotationPresent(Invisible.class)||field.isAnnotationPresent(InvisibleId.class))){
                    //定义初始动态字段
                    String tem="<if test=\" _field != null\">, _column=#{ _field }  </if>";
                    //替换变量,并判断是否到最后一个
                    sb.append(tem.replaceAll("_field",field.getName()).replaceAll("_column",
                            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field.getName())));
                }
            }
            //剪切掉最后一个,
            String s = sb.toString().replaceFirst(",", "");
            //替换传入的参数
            System.out.println("s = " + s);
             script=matcher.replaceAll(s);
            script="<script>"+script+"</script>";
        }
        //返回结果
        return  super.createSqlSource(configuration,script,parameterType);
    }
}
