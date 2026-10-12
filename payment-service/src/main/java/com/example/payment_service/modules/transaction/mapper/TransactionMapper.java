package com.example.payment_service.modules.transaction.mapper;

import org.mapstruct.Mapper;

import com.example.payment_service.common.mapper.MapperConfiguration;
import com.example.payment_service.modules.transaction.dto.response.TransactionResponse;
import com.example.payment_service.modules.transaction.entity.Transaction;
import com.example.payment_service.modules.user.mapper.UserMapper;

@Mapper(config = MapperConfiguration.class, uses = { UserMapper.class })
public interface TransactionMapper {

    TransactionResponse toTransactionResponse(Transaction transaction);

}