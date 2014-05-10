package org.railway.com.trainplan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by star on 5/7/14.
 */
@Controller
@RequestMapping(value = "/content")
public class Content {

    @RequestMapping(method = RequestMethod.GET)
    public String content() {
        return "trainplan/Console";
    }
}
