/****************************************************************************
 * 
 * Copyright (C) CEMEX S.A.B de C.V 2018, Inc - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * 
 * Proprietary and confidential.
 * 
 * Written by Rogelio Reyo Cachu, 9/06/2018
 * 
 * We keep our License Statement under regular review and reserve the right 
 * to modify this License Statement from time to time.
 * 
 * Should you have any questions or comments about any of the above, 
 * please contact ethos@cemex.com for assistance or visit www.cemex.com 
 * if you need additional information or have any questions.
 * 
 ****************************************************************************/
package mx.com.agurno.flipmarket.service.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.com.agurno.flipmarket.entity.Item;
import mx.com.agurno.flipmarket.repository.ItemRepository;
import mx.com.agurno.flipmarket.service.ItemService;


/**
 * PropertyServiceImpl - PropertyServiceImpl.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@Named("itemService")
public class ItemServiceImpl extends CrudServiceImpl<Item, Long, ItemRepository> implements ItemService {
	
	/** The Constant LOG. */
	@SuppressWarnings("unused")
	private static final Logger LOG  = Logger.getLogger(ItemServiceImpl.class);
    
    @Override
    @Inject
    public void setRepository(ItemRepository itemRepository) {
        super.setRepository(itemRepository);
    }

    /**
	 * My items.
	 *
	 * @param pageable
	 *            the pageable
	 * @param userName
	 *            the user name
	 * @return the page
	 */
//    @Cacheable(value = "myitemshcache", key="#pageable.toString() + #userName")
	public Page<Item> myItems(Pageable pageable, String userName) {
		return this.repository.findAll(pageable);
	}

}