package com.fitmate.adapter.out.persistence.jpa.mate.entity;

import com.fitmate.adapter.out.converter.SetConverter;
import com.fitmate.adapter.out.persistence.jpa.common.BaseJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "mate")
@SQLDelete(sql = "UPDATE MATE SET DELETED_AT = CURRENT_TIMESTAMP WHERE ID = ? ")
@Where(clause = "DELETED_AT IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MateJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String fitCategory;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "CLOB")
    private String introduction;

    @Column
    @Convert(converter = SetConverter.class)
    private Set<Long> introImageIds = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime mateAt;

    @Column(nullable = false)
    private String fitPlaceName;

    @Column(nullable = false)
    private String fitPlaceAddress;

    @Column(nullable = false)
    private String gatherType;

    @Column(nullable = false)
    private String permitGender;

    @Column(nullable = false)
    private Integer permitMaxAge;

    @Column(nullable = false)
    private Integer permitMinAge;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer permitPeopleCnt = 1;

    @Column(nullable = false)
    private Long writerId;

    @Column(nullable = false)
    private String applyQuestion;

    @Column(nullable = false)
    private Integer totalFee;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column
    @Convert(converter = SetConverter.class)
    private Set<Long> waitingAccountIds = new HashSet<>();

    @Column
    @Convert(converter = SetConverter.class)
    private Set<Long> approvedAccountIds = new HashSet<>();

    public MateJpaEntity(Long id, String fitCategory, String title, String introduction, Set<Long> introImageIds, LocalDateTime mateAt, String fitPlaceName, String fitPlaceAddress, String gatherType, String permitGender, Integer permitMaxAge, Integer permitMinAge, Integer permitPeopleCnt, Long writerId, String applyQuestion, Integer totalFee, Set<Long> waitingAccountIds, Set<Long> approvedAccountIds, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fitCategory = fitCategory;
        this.title = title;
        this.introduction = introduction;
        this.introImageIds = introImageIds;
        this.mateAt = mateAt;
        this.fitPlaceName = fitPlaceName;
        this.fitPlaceAddress = fitPlaceAddress;
        this.gatherType = gatherType;
        this.permitGender = permitGender;
        this.permitMaxAge = permitMaxAge;
        this.permitMinAge = permitMinAge;
        this.permitPeopleCnt = permitPeopleCnt;
        this.writerId = writerId;
        this.applyQuestion = applyQuestion;
        this.totalFee = totalFee;
        this.waitingAccountIds = waitingAccountIds;
        this.approvedAccountIds = approvedAccountIds;
        super.createdAt = createdAt;
        super.updatedAt = updatedAt;
    }
}
