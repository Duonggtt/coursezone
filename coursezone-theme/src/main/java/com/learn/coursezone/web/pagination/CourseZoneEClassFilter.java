package com.learn.coursezone.web.pagination;

import com.tvd12.ezydata.database.query.EzyQueryConditionBuilder;
import java.util.Collection;

import lombok.Builder;
import lombok.Getter;
import org.youngmonkeys.elearning.entity.EClassStatus;
import org.youngmonkeys.elearning.pagination.EClassFilter;

@Getter
@Builder
public class CourseZoneEClassFilter implements EClassFilter {
    public Long teacherId;
    public Long studentId;
    public Collection<Long> categoryIds;
    public Collection<EClassStatus> statuses;
    public Collection<String> keywords;

    public void decorateQueryStringBeforeWhere(StringBuilder queryString) {
        if (this.keywords != null) {
            queryString.append(" INNER JOIN DataIndex k ON e.id = k.dataId");
        }

        if (this.studentId != null) {
            queryString.append(
                " INNER JOIN OrderProduct a ON e.productId = a.productId " +
                    "INNER JOIN Order b ON a.orderId = b.id "
            );
        }
        if (categoryIds != null) {
            queryString.append(
                " INNER JOIN ProductCategoryProduct c ON e.productId = c.productId"
            );
        }
    }

    public String matchingCondition() {
        EzyQueryConditionBuilder answer = new EzyQueryConditionBuilder();
        if (this.statuses != null) {
            answer.append("e.status in :statuses");
        }

        if (this.teacherId != null) {
            answer.and("e.teacherId = :teacherId");
        }

        if (this.studentId != null) {
            answer.and("b.status = 'PAID'").and("b.creatorUserId = :studentId");
        }

        if (this.keywords != null) {
            answer.and("k.dataType = 'elearning_classes'").and("k.keyword IN :keywords");
        }

        if (categoryIds != null) {
            answer.and("c.categoryId IN :categoryIds");
        }
        return answer.build();
    }
}

