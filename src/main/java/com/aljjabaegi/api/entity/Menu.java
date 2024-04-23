package com.aljjabaegi.api.entity;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import static com.aljjabaegi.api.common.jpa.idGenerator.IdGeneratorUtil.GENERATOR_PARAM_KEY;

/**
 * Menu entity
 *
 * @author GEONLEE
 * @since 2024-04-22
 */
@Entity(name = "menu")
@Getter
@Setter
public class Menu extends BaseEntity {
    @Id
    @Column(name = "menu_id")
    @GenericGenerator(name = "MenuIdGenerator", type = IdGeneratorUtil.class
            , parameters = @org.hibernate.annotations.Parameter(name = GENERATOR_PARAM_KEY, value = "menu"))
    @GeneratedValue(generator = "MenuIdGenerator")
    private String menuId;
    @Column(name = "menu_nm")
    private String menuName;
    @Column(name = "upper_menu_id")
    private String upperMenuId;
    @Column(name = "menu_path")
    private String menuPath;
}
