package edu.tamu.modspineomatic.service;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpineLabelService implements SpineLabelPrinter {

    private static final String ITEM_HRID_PREFIX = "it";
    private static final String BARCODE_PREFIX = "A";

    @Autowired
    private OkapiService okapiService;

    @Autowired
    private TemplatingService templatingService;

    public String getSpineLabel(String tenant, String identifier) {

        String token = okapiService.getToken(tenant);
        JsonNode itemNode = null;

        if (identifier.startsWith(BARCODE_PREFIX)) {
            itemNode = okapiService.fetchItemByBarcode(tenant, token, identifier);
        } else if (identifier.startsWith(ITEM_HRID_PREFIX)) {
            itemNode = okapiService.fetchItemByItemHRID(tenant, token, identifier);
        } else {
            throw new RuntimeException(String.format("Unable to determine identifer type for {}", identifier));
        }

        return templatingService.templateResponse(itemNode);
    }

}
