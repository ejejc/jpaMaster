package com.example.jpamaster.accommodations.repository.review;

import com.example.jpamaster.accommodations.domain.entity.QReview;
import com.example.jpamaster.accommodations.domain.entity.QRoom;
import com.example.jpamaster.accommodations.domain.entity.Review;
import com.example.jpamaster.accommodations.dto.ReviewDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public ReviewCustomRepositoryImpl(EntityManager em) {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Review> findAllReviewByRoomSeq(Long seq) {
        return jpaQueryFactory.selectFrom(QReview.review)
                .where(QReview.review.room.roomSeq.eq(seq))
                .fetch();
    }

    @Override
    public Page<Review> findAllReviewByRoomList(List<Long> roomseqList, Pageable pageable) {
        List<Review> fetch = jpaQueryFactory.selectFrom(QReview.review)
                .join(QReview.review.room, QRoom.room).fetchJoin()
                .where(QReview.review.room.roomSeq.in(roomseqList))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory.select(QReview.review.count())
                .from(QReview.review)
                //.join(QReview.review.room, QRoom.room).fetchJoin()
                .where(QReview.review.room.roomSeq.in(roomseqList));


        return PageableExecutionUtils.getPage(fetch, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ReviewDto.ReviewSum> findAvgEachScore() {
        /**
         * sum() ?????? ???????????? ?????? ?????? ??????, null??? ?????? 0?????? ???????????? ?????? coalesce() ??????
         * count()??? ???????????? 0?????? ????????????.
         */
        return jpaQueryFactory.from(QReview.review)
                .groupBy(QReview.review.room.roomSeq)
                .select(
                        Projections.bean(ReviewDto.ReviewSum.class,
                                QReview.review.room.roomSeq.as("roomSeq"),
                                QReview.review.cleanlinessStarScore.sum().as("cleanlinessSum"),
                                QReview.review.convenienceStarScore.sum().as("convenienceSum"),
                                QReview.review.kindnessStarScore.sum().as("kindnessSum"),
                                QReview.review.locationStarScore.sum().as("locationSum"),
                                QReview.review.room.roomSeq.count().as("reviewCnt"))
                ).fetch();
    }
}
