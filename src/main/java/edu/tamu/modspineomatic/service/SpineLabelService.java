package edu.tamu.modspineomatic.service;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpineLabelService implements SpineLabelPrinter {

    @Autowired
    private OkapiService okapiService;

    @Autowired
    private TemplatingService templatingService;

    public String getSpineLabel(String tenant, String barcode) {
        String token = okapiService.getToken(tenant);
        JsonNode itemNode = okapiService.fetchItem(tenant, token, barcode);

        return templatingService.templateResponse(itemNode);
    }

}
