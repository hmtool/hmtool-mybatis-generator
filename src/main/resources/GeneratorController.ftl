package ${controllerPackage};

import ${entityClass}.${entityClassName};
import ${servicePackage}.I${entityClassName}Service;
import com.petecat.interchan.core.controller.BaseController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.petecat.interchan.protocol.Result;

/**
* ${description}控制层
* @author: ${author}
* @date:   ${date}
*/
@RestController
@RequestMapping("请填写你的请求路径。比如/sys/user")
public class ${entityClassName}Controller extends BaseController{

    @Autowired
    private I${entityClassName}Service ${entityClassName?uncap_first}Service;

    @GetMapping(value = "/page")
    public Result page(){
        return null;
    }

    @PostMapping(value = "/insert")
    public Result insert(){
        return null;
    }

    @PutMapping(value = "/update")
    public Result update(){
        return null;
    }

    @DeleteMapping(value = "/delete")
    public Result delete(){
        return null;
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable ${idType} id){
        return null;
    }
}