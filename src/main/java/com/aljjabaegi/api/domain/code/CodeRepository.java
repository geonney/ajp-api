package com.aljjabaegi.api.domain.code;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.entity.Code;
import com.aljjabaegi.api.entity.key.CodeKey;

/**
 * @author GEONLEE
 * @since 2024-05-29
 */
public interface CodeRepository extends JpaDynamicRepository<Code, CodeKey> {
}
