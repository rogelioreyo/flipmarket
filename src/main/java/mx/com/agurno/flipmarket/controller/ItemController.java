package mx.com.agurno.flipmarket.controller;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.com.agurno.flipmarket.entity.Item;
import mx.com.agurno.flipmarket.service.ItemService;



/**
 * ItemController - ItemController.java
 *
 * @author Rogelio Reyo
 * @version 1.0.0
 * @since 10/11/2019
 */
@RestController
@RequestMapping(value = "/v1/its/items", produces = "application/json")
public class ItemController extends ServiceBasedRestController<Item, Long, ItemService> {
	
	/** The Constant LOG. */
	private static final Logger LOG  = Logger.getLogger(ItemController.class);
	
    @Inject
    @Named("itemService")
    @Override
    public void setService(ItemService itemService) {
        this.service = itemService;
    }
    
    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
   	public Page<Item> myItems(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
               @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
       	LOG.info("my");
   		return this.service.myItems(PageRequest.of(page - 1, size), null);
   	}
    
}
