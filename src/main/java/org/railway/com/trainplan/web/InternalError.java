package org.railway.com.trainplan.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by star on 5/7/14.
 */
@Controller
@RequestMapping(value = "/500")
public class InternalError {

    @RequestMapping(method = RequestMethod.GET)
    public String internalError() {
        return "trainplan/500";
    }
}
