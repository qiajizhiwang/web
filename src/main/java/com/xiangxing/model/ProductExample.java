package com.xiangxing.model;

import java.util.ArrayList;
import java.util.List;

public class ProductExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProductExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdIsNull() {
            addCriterion("student_course_id is null");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdIsNotNull() {
            addCriterion("student_course_id is not null");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdEqualTo(String value) {
            addCriterion("student_course_id =", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdNotEqualTo(String value) {
            addCriterion("student_course_id <>", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdGreaterThan(String value) {
            addCriterion("student_course_id >", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdGreaterThanOrEqualTo(String value) {
            addCriterion("student_course_id >=", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdLessThan(String value) {
            addCriterion("student_course_id <", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdLessThanOrEqualTo(String value) {
            addCriterion("student_course_id <=", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdLike(String value) {
            addCriterion("student_course_id like", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdNotLike(String value) {
            addCriterion("student_course_id not like", value, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdIn(List<String> values) {
            addCriterion("student_course_id in", values, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdNotIn(List<String> values) {
            addCriterion("student_course_id not in", values, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdBetween(String value1, String value2) {
            addCriterion("student_course_id between", value1, value2, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria andStudentCourseIdNotBetween(String value1, String value2) {
            addCriterion("student_course_id not between", value1, value2, "studentCourseId");
            return (Criteria) this;
        }

        public Criteria and作品名称IsNull() {
            addCriterion("作品名称 is null");
            return (Criteria) this;
        }

        public Criteria and作品名称IsNotNull() {
            addCriterion("作品名称 is not null");
            return (Criteria) this;
        }

        public Criteria and作品名称EqualTo(String value) {
            addCriterion("作品名称 =", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称NotEqualTo(String value) {
            addCriterion("作品名称 <>", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称GreaterThan(String value) {
            addCriterion("作品名称 >", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称GreaterThanOrEqualTo(String value) {
            addCriterion("作品名称 >=", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称LessThan(String value) {
            addCriterion("作品名称 <", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称LessThanOrEqualTo(String value) {
            addCriterion("作品名称 <=", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称Like(String value) {
            addCriterion("作品名称 like", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称NotLike(String value) {
            addCriterion("作品名称 not like", value, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称In(List<String> values) {
            addCriterion("作品名称 in", values, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称NotIn(List<String> values) {
            addCriterion("作品名称 not in", values, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称Between(String value1, String value2) {
            addCriterion("作品名称 between", value1, value2, "作品名称");
            return (Criteria) this;
        }

        public Criteria and作品名称NotBetween(String value1, String value2) {
            addCriterion("作品名称 not between", value1, value2, "作品名称");
            return (Criteria) this;
        }

        public Criteria andProductUrlIsNull() {
            addCriterion("product_url is null");
            return (Criteria) this;
        }

        public Criteria andProductUrlIsNotNull() {
            addCriterion("product_url is not null");
            return (Criteria) this;
        }

        public Criteria andProductUrlEqualTo(String value) {
            addCriterion("product_url =", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlNotEqualTo(String value) {
            addCriterion("product_url <>", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlGreaterThan(String value) {
            addCriterion("product_url >", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlGreaterThanOrEqualTo(String value) {
            addCriterion("product_url >=", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlLessThan(String value) {
            addCriterion("product_url <", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlLessThanOrEqualTo(String value) {
            addCriterion("product_url <=", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlLike(String value) {
            addCriterion("product_url like", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlNotLike(String value) {
            addCriterion("product_url not like", value, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlIn(List<String> values) {
            addCriterion("product_url in", values, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlNotIn(List<String> values) {
            addCriterion("product_url not in", values, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlBetween(String value1, String value2) {
            addCriterion("product_url between", value1, value2, "productUrl");
            return (Criteria) this;
        }

        public Criteria andProductUrlNotBetween(String value1, String value2) {
            addCriterion("product_url not between", value1, value2, "productUrl");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}