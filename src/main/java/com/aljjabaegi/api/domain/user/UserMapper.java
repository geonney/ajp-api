package com.aljjabaegi.api.domain.user;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.user.record.*;
import com.aljjabaegi.api.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * User mapper
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * entity to search response
     *
     * @param entity user entity
     * @return UserSearchResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    UserSearchResponse toSearchResponse(User entity);

    /**
     * entity list to search response list
     *
     * @param list user entity list
     * @return UserSearchResponse list
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    List<UserSearchResponse> toSearchResponseList(List<User> list);

    /**
     * entity to create response
     *
     * @param entity user entity
     * @return UserCreateResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    UserCreateResponse toCreateResponse(User entity);

    /**
     * entity to modify response
     *
     * @param entity user entity
     * @return UserModifyResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    UserModifyResponse toModifyResponse(User entity);

    /**
     * createRequest to entity
     *
     * @param userCreateRequest user create request
     * @return User
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    User toEntity(UserCreateRequest userCreateRequest);

    /**
     * update from record
     *
     * @param userModifyRequest update request record
     * @param entity            update request entity
     * @return User
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    User updateFromRequest(UserModifyRequest userModifyRequest, @MappingTarget User entity);

}
