package com.aljjabaegi.api.domain.menu;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.entity.Menu;
import org.springframework.stereotype.Repository;

/**
 * Menu Repository
 *
 * @author GEONLEE
 * @since 2024-04-22<br />
 */
@Repository
public interface MenuRepository extends JpaDynamicRepository<Menu, String> {

}
