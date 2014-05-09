package org.railway.com.trainplan.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by star on 5/9/14.
 */
@Controller
@RequestMapping(value = "/config")
public class ConfigController {

    @Value("#{restConfig['rest.url']}")
    private String restUrl;

    @Value("#{rabbitmqConfig['rabbitmq.ip']}")
    private String rabbitMQ_ip;

    @Value("#{rabbitmqConfig['rabbitmq.username']}")
    private String rabbitMQ_username;

    @Value("#{rabbitmqConfig['rabbitmq.password']}")
    private String rabbitMQ_password;

    @Value("#{rabbitmqConfig['rabbitmq.vhost']}")
    private String rabbitMQ_vhost;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String getRestUrl() {
        return restUrl;
    }

    @RequestMapping(value = "ip", method = RequestMethod.GET)
    @ResponseBody
    public String getRabbitMQ_ip() {
        return rabbitMQ_ip;
    }

    @RequestMapping(value = "username", method = RequestMethod.GET)
    @ResponseBody
    public String getRabbitMQ_username() {
        return rabbitMQ_username;
    }

    @RequestMapping(value = "password", method = RequestMethod.GET)
    @ResponseBody
    public String getRabbitMQ_password() {
        return rabbitMQ_password;
    }

    @RequestMapping(value = "vhost", method = RequestMethod.GET)
    @ResponseBody
    public String getRabbitMQ_vhost() {
        return rabbitMQ_vhost;
    }
}
