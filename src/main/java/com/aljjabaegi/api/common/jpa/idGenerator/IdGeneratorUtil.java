package kr.co.neighbor21.neighborApi.common.util;

import kr.co.neighbor21.neighborApi.common.contextHolder.ApplicationContextHolder;
import kr.co.neighbor21.neighborApi.domain.routeManagement.busStop.edit.EditBusStopRepository;
import kr.co.neighbor21.neighborApi.entity.key.M_TP_BSTP_KEY;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.util.Properties;

/**
 * IdentifierGenerator 구현체<br />
 * save 시점에 키를 생성하여 저장<br />
 *
 * @author GEONLEE
 * @since 2024-03-26<br />
 */
public class KeyGeneratorUtil implements IdentifierGenerator {
    public static final String GENERATOR_PARAM_KEY = "GENERATOR_PARAM";
    private String tableName;

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) {
        this.tableName = ConfigurationHelper.getString(GENERATOR_PARAM_KEY, parameters);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        if ("busStop".equals(this.tableName)) {
            return generateBusStopId();
        }
//        else if ("bit".equals(this.tableName)) {
//            return generateBitId();
//        }
        return null;
    }

    /**
     * 정류장 키 생성 (max + 1)
     *
     * @author GEONLEE
     * @since 2024-03-26
     */
    private M_TP_BSTP_KEY generateBusStopId() {
        EditBusStopRepository editBusStopRepository = ApplicationContextHolder.getContext().getBean(EditBusStopRepository.class);
        return new M_TP_BSTP_KEY(editBusStopRepository.findByMaxBusStopId());
    }

    /**
     * BIT 키 생성 (max + 1)
     *
     * @author GEONLEE
     * @since 2024-03-26
     */
//    private M_FA_BIT_KEY generateBitId() {
//        OperationBitRepository operationBitRepository = ApplicationContextHolder.getContext().getBean(OperationBitRepository.class);
//        return new M_FA_BIT_KEY(operationBitRepository.findByMaxBitId());
//    }
}
