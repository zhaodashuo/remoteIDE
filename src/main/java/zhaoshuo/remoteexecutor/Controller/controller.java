package zhaoshuo.remoteexecutor.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import zhaoshuo.remoteexecutor.Service.CodeService;


/**
 * @Description
 * @Author zhaoshuo
 * @Date 2020-05-03 21:01
 */
@Controller
public class controller {
    @Autowired
    private CodeService codeService;

    private static final String defaultSource = "public class Run {\n"
            + "    public static void main(String[] args) {\n"
            + "        \n"
            + "    }\n"
            + "}";

    @RequestMapping(value = "run", method = RequestMethod.GET)
    public String entry(Model model) {
        model.addAttribute("lastSource", defaultSource);
        return "ide";
    }

    @RequestMapping(value = "run",method = RequestMethod.POST)
    public String runCode(@RequestParam("source") String source,
                          @RequestParam("systemIn") String systemIn,
                          Model model){
        String res = codeService.execute(source, systemIn);
        res = res.replaceAll(System.lineSeparator(), "<br/>"); // 处理html中换行的问题
        model.addAttribute("lastSource", source);
        model.addAttribute("lastSystemIn", systemIn);
        model.addAttribute("runResult", res);
        return "ide";

    }



}
