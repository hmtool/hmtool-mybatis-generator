package ${servicePackage}.impl;

import ${entityClass}.${entityClassName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.petecat.interchan.core.service.impl.BaseServiceImpl;
import ${servicePackage}.I${entityClassName}Service;
import ${mapperPackage}.${entityClassName}Mapper;

/**
* ${description}服务实现类
* @author: ${author}
* @date: ${date}
*/
@Service
public class ${entityClassName}ServiceImpl extends BaseServiceImpl<${entityClassName},${idType}> implements I${entityClassName}Service{

    @Autowired
    private  ${entityClassName}Mapper  ${entityClassName?uncap_first}Mapper;
}