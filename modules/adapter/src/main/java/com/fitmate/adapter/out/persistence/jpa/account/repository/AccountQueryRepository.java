package com.fitmate.adapter.out.persistence.jpa.account.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

import static com.fitmate.adapter.out.persistence.jpa.account.entity.QAccountJpaEntity.accountJpaEntity;


@Repository
@RequiredArgsConstructor
public class AccountQueryRepository {
    private final JPAQueryFactory queryFactory;

    public boolean checkDuplicated(Long accountId, String nickName, String name, String email, String phone) {
        Long count = queryFactory
                    .select(accountJpaEntity.count())
                    .from(accountJpaEntity)
                    .where(getCheckBooleanBuilder(accountId, nickName, name, email, phone))
                    .fetchOne();
        return !(count == null | (count != null && count <= 0));
    }

    private BooleanBuilder getCheckBooleanBuilder(Long accountId, String nickName, String name, String email, String phone) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.or((nullSafeBuilder(accountJpaEntity.nickName::eq, nickName)));
        booleanBuilder.or(nullSafeBuilder(accountJpaEntity.name::eq, name));
        booleanBuilder.or(nullSafeBuilder(accountJpaEntity.email::eq, email));
        booleanBuilder.or(nullSafeBuilder(accountJpaEntity.phone::eq, phone));
        booleanBuilder.and(nullSafeBuilder(accountJpaEntity.id::ne, accountId));
        return booleanBuilder;
    }

    private <T> Predicate nullSafeBuilder(Function<T, Predicate> builder, T value) {
        return value != null ? builder.apply(value) : Expressions.booleanTemplate("1 = 1");
    }
}
