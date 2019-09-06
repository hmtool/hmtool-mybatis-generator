package tech.mhuang.mybatis.generate.common;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * mybatis xml配置处理
 *
 * @author mhuang
 * @since 1.0.0
 */
public class XMLElementUtils {
    public XMLElementUtils() {
    }

    public static void replaceAttribute(XmlElement element, Map<String, String> params) {
        List<Attribute> list = element.getAttributes().parallelStream().filter(x -> x != null).map((attibute) -> {
            String name = attibute.getName();
            String value = params.getOrDefault(name, attibute.getValue());
            attibute = new Attribute(name, value);
            return attibute;
        }).collect(Collectors.toList());
        
        element.getAttributes().clear();
        
        list.parallelStream().filter(x -> x != null).forEach((attirube) -> {
            element.addAttribute(attirube);
        });
    }
}
