package dev.mini.minibox.mappers;

import dev.mini.minibox.entities.PaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentMapper {
    int insertPayment(PaymentEntity payment);

    int deletePayment(@Param(value = "userEmail") String userEmail,
                      @Param(value = "paymentId") int paymentId);
}
