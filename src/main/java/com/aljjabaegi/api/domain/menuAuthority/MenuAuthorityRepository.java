package com.aljjabaegi.api.domain.menuAuthority;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.entity.MenuAuthority;
import com.aljjabaegi.api.entity.key.MenuAuthorityKey;
import org.springframework.stereotype.Repository;

/**
 * Menu Authority Repository
 *
 * @author GEONLEE
 * @since 2024-04-22<br />
 */
@Repository
public interface MenuAuthorityRepository extends JpaDynamicRepository<MenuAuthority, MenuAuthorityKey> {

}
