package com.example.payment_service.modules.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.payment_service.common.mapper.MapperConfiguration;
import com.example.payment_service.modules.transaction.dto.response.TransactionResponse;
import com.example.payment_service.modules.transaction.entity.Transaction;

@Mapper(config = MapperConfiguration.class)
public interface TransactionMapper {

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "user.name", source = "userName")
    @Mapping(target = "user.email", source = "userEmail")
    TransactionResponse toTransactionResponse(Transaction transaction);

}