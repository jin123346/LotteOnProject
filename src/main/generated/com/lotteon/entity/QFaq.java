package com.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFaq is a Querydsl query type for Faq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaq extends EntityPathBase<Faq> {

    private static final long serialVersionUID = 596479269L;

    public static final QFaq faq = new QFaq("faq");

    public final DateTimePath<java.time.LocalDateTime> date = createDateTime("date", java.time.LocalDateTime.class);

    public final StringPath faqcontent = createString("faqcontent");

    public final NumberPath<Integer> faqhit = createNumber("faqhit", Integer.class);

    public final NumberPath<Integer> faqNo = createNumber("faqNo", Integer.class);

    public final StringPath faqtitle = createString("faqtitle");

    public final StringPath faqtype1 = createString("faqtype1");

    public final StringPath faqtype2 = createString("faqtype2");

    public QFaq(String variable) {
        super(Faq.class, forVariable(variable));
    }

    public QFaq(Path<? extends Faq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFaq(PathMetadata metadata) {
        super(Faq.class, metadata);
    }

}

