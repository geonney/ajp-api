package com.aljjabaegi.api.domain.user;

import com.aljjabaegi.api.domain.user.record.*;
import com.aljjabaegi.api.entity.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 Service
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    /**
     * 전체 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public List<UserSearchResponse> getUserList() {
        return userMapper.toSearchResponseList(userRepository.findAll());
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public UserSearchResponse getUser(String userId) {
        User entity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId));
        return userMapper.toSearchResponse(entity);
    }

    /**
     * 사용자 ID 중복 확인
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public boolean checkUserId(String userId) {
        return userRepository.existsById(userId);
    }

    /**
     * 사용자 추가
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public UserCreateResponse createUser(UserCreateRequest parameter) {
        if (userRepository.existsById(parameter.userId())) {
            throw new EntityExistsException(parameter.userId());
        }
        User createRequestEntity = userMapper.toEntity(parameter);
        User createdEntity = userRepository.save(createRequestEntity);
        return userMapper.toCreateResponse(createdEntity);
    }

    /**
     * 사용자 수정
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public UserModifyResponse modifyUser(UserModifyRequest parameter) {
        User entity = userRepository.findById(parameter.userId())
                .orElseThrow(() -> new EntityNotFoundException(parameter.userId()));
        User modifiedEntity = userMapper.updateFromRequest(parameter, entity);
        modifiedEntity = userRepository.saveAndFlush(modifiedEntity);
        return userMapper.toModifyResponse(modifiedEntity);
    }

    /**
     * 사용자 삭제
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public Long deleteUser(String userId) {
        User entity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId));
        userRepository.delete(entity);
        return 1L;
    }
}
