package edu.tamu.modspineomatic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.modspineomatic.service.OkapiService;
import edu.tamu.modspineomatic.service.TemplatingService;

@RestController
@RequestMapping("/call-number")
public class CallNumberController {

    @Value("${spring.datasource.tenant}")
    private String tenant;

    @Autowired
    private OkapiService okapiService;

    @Autowired
    private TemplatingService templatingService;

    @GetMapping
    public String get(@RequestParam(required = true) String barcode) {
        String token = okapiService.getToken(tenant);
        return templatingService.templateResponse(okapiService.fetchItem(tenant, token, barcode));
    }

}
