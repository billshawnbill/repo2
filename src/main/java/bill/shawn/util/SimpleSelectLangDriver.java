package bill.shawn.util;

import com.google.common.base.CaseFormat;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSelectLangDriver extends XMLLanguageDriver implements LanguageDriver {
    //定义正则对象
    private final Pattern inPatter=Pattern.compile("\\(#\\{\\w+\\}\\)");
    /*
    * 重写方法,定义动态语句拼接规则
    * */
    @Override
    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
        //script为传入的sql语句
        Matcher matcher = inPatter.matcher(script);
        //判断是否有该字段
        if(matcher.find()){
            //创建StringBuilder做凭借
            StringBuilder sb = new StringBuilder();
            //拼接where标签
            sb.append("<where> ");
            //遍历字段进行凭借
            for (Field field : parameterType.getDeclaredFields()) {
                //过滤有invisible注解的变量
                if(field.isAnnotationPresent(Invisible.class)){
                    //定义初始字段
                    String tem="<if test=\"_field != null\">and  _column = #{_field} </if>";
                    //替换_column和_field,并将变量的命名规则:小驼峰规则-->下划线规则
                    sb.append(tem.replaceAll("_field",field.getName()).replaceAll("_column",
                            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,field.getName()))) ;
                }
            }
            //拼接where标签
            sb.append("</where>");
            //替换传入的参数
            script= matcher.replaceAll(sb.toString());
            script="<script>"+script+"</script>";
        }
        //返回处理的结果
          return  super.createSqlSource(configuration,script,parameterType);
    }
}
































































//    //定义Patter对象
//    private final Pattern inPatter =Pattern.compile("\\(#\\{\\w+\\}\\)");
//    @Override
//    public SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType) {
//        //查询是否有符合正则的字段
//        Matcher matcher = inPatter.matcher(script);
//        //判断
//        if(matcher.find()){
//            //表示有该类型的字段,创建StringBuilder做拼接]
//            StringBuilder sb = new StringBuilder();
//            //凭借where标签
//            sb.append("<where> ");
//            //通过反射获取所有的field
//            for (Field field : parameterType.getDeclaredFields()) {
//                //被定义可Invisible注解的属性,将被过滤查询
//                if(!field.isAnnotationPresent(Invisible.class)){
//                    //定义凭借的字段
//                    String tem="<if test =\"_field != null\"> and _column=#{_field}  </if>";
//                    //替换字段,并将变量的命名规则变换:驼峰规则(pojo命名规范)-->下划线规则(数据库命名规则)
//                    sb.append(tem.replaceAll("_field",
//                            field.getName()).replaceAll("_column", CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName())));
//                }
//            }
//            //拼接where标签
//            sb.append("</where>");
//            //将传入的参数script替换
//            script=matcher.replaceAll(sb.toString());
//            //返回sql语句
//            script="<script>"+script+"</script>";
//        }
//        return super.createSqlSource(configuration,script,parameterType);
//    }
