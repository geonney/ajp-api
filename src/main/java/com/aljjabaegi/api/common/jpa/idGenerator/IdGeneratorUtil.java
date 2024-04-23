package com.aljjabaegi.api.common.jpa.idGenerator;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.domain.project.ProjectRepository;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.UUID;

/**
 * IdentifierGenerator 구현체<br />
 * save 시점에 특정 형태의 키를 생성하여 저장<br />
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
public class IdGeneratorUtil implements IdentifierGenerator {

    public static final String GENERATOR_PARAM_KEY = "GENERATOR_PARAM";
    private static final Logger LOGGER = LoggerFactory.getLogger(IdGeneratorUtil.class);
    private String parameter; //파라미터에 따른 처리가 필요할 때 사용

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) {
        this.parameter = ConfigurationHelper.getString(GENERATOR_PARAM_KEY, parameters);
        LOGGER.info(this.parameter + " ID generate.");
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        if ("project".equals(this.parameter)) {
            return generateProjectId();
        } else {
            return UUID.randomUUID().toString();
        }
    }

    /**
     * Project id 생성<br />
     * "PJ" + 일련번호 3자리
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    private String generateProjectId() {
        ProjectRepository projectRepository = ApplicationContextHolder.getContext().getBean(ProjectRepository.class);
        return projectRepository.findByMaxProjectId();
    }
}
