package org.railway.com.trainplan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by star on 5/7/14.
 */
@Controller
@RequestMapping(value = "/template")
public class Template {

    @RequestMapping(method = RequestMethod.GET)
    public String template() {
        return "trainplan/template";
    }
}
