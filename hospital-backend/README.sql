-- ===================================================================
-- 医院在线挂号系统数据库说明文档
-- 数据库名称: hospital_db
-- 创建时间: 2026
-- ===================================================================

-- ===================================================================
-- 一、数据库概述
-- ===================================================================
-- 本数据库用于支持医院在线挂号系统的后端数据存储，主要功能包括：
-- 1. 用户管理：用户注册、登录、个人信息管理
-- 2. 医院管理：医院信息、科室信息、医生信息
-- 3. 挂号预约：医生排班、挂号订单、支付管理
-- 4. 电话咨询：电话咨询订单、支付管理
-- 5. 健康科普：健康文章发布与浏览
-- 6. 疾病百科：疾病信息查询与关注
-- 7. 互动功能：关注、评价、反馈、消息
-- 8. 就诊人管理：家庭成员/就诊人信息管理

-- ===================================================================
-- 二、表结构概览
-- ===================================================================

-- 1. t_user（用户表）
--    说明：存储系统注册用户的基本信息
--    主要字段：id, username, password, phone, email, real_name, gender, birthday, avatar, status
--    唯一约束：username, phone

-- 2. t_family_member（就诊成员表）
--    说明：存储用户添加的就诊人信息（家庭成员）
--    主要字段：id, user_id, name, gender, birthday, phone, id_card, relation, is_default
--    关联关系：user_id -> t_user.id

-- 3. t_hospital（医院表）
--    说明：存储医院基本信息
--    主要字段：id, name, level, address, phone, intro, image, province, city, department_count, doctor_count, follow_count, status

-- 4. t_department（科室表）
--    说明：存储医院科室信息，支持一级/二级科室层级结构
--    主要字段：id, name, description, parent_id, sort_order, status
--    层级关系：parent_id -> t_department.id（自关联）

-- 5. t_hospital_department（医院-科室关联表）
--    说明：存储医院与科室的关联关系（多对多）
--    主要字段：id, hospital_id, department_id
--    关联关系：hospital_id -> t_hospital.id, department_id -> t_department.id
--    唯一约束：(hospital_id, department_id)

-- 6. t_doctor（医生表）
--    说明：存储医生基本信息
--    主要字段：id, name, gender, title, department_id, hospital_id, avatar, phone, intro, expertise, consult_count, rating, follow_count, online_status, price, registration_price, status
--    关联关系：department_id -> t_department.id, hospital_id -> t_hospital.id

-- 7. t_schedule（医生排班表）
--    说明：存储医生的排班信息
--    主要字段：id, doctor_id, hospital_id, department_id, schedule_date, time_slot, total_count, remain_count, status
--    关联关系：doctor_id -> t_doctor.id, hospital_id -> t_hospital.id, department_id -> t_department.id

-- 8. t_appointment（挂号订单表）
--    说明：存储挂号订单信息
--    主要字段：id, order_no, user_id, doctor_id, hospital_id, patient_name, patient_phone, patient_id_card, patient_gender, patient_age, appointment_date, appointment_time, disease_desc, amount, status, pay_time
--    关联关系：user_id -> t_user.id, doctor_id -> t_doctor.id, hospital_id -> t_hospital.id
--    唯一约束：order_no

-- 9. t_consult（电话咨询订单表）
--    说明：存储电话咨询订单信息
--    主要字段：id, order_no, user_id, doctor_id, patient_name, patient_phone, disease_desc, appointment_time, duration, amount, status, pay_time
--    关联关系：user_id -> t_user.id, doctor_id -> t_doctor.id
--    唯一约束：order_no

-- 10. t_payment_flow（支付流水表）
--     说明：存储支付交易流水信息
--     主要字段：id, business_order_no, business_type, pay_method, third_party_trade_no, actual_amount, pay_status, pay_success_time, original_callback
--     关联关系：business_order_no -> t_appointment.order_no 或 t_consult.order_no

-- 11. t_article（健康科普文章表）
--     说明：存储健康科普文章信息
--     主要字段：id, title, summary, content, department_id, author, image, views, status, publish_time
--     关联关系：department_id -> t_department.id

-- 12. t_disease（疾病表）
--     说明：存储疾病百科信息
--     主要字段：id, department_id, name, description, alias, location, treatment, symptoms, treatment_period, cure_rate, examinations, follow_count
--     关联关系：department_id -> t_department.id

-- 13. t_follow（关注表）
--     说明：存储用户关注记录（支持关注医院、医生、疾病）
--     主要字段：id, user_id, follow_type, follow_id
--     关联关系：user_id -> t_user.id
--     唯一约束：(user_id, follow_type, follow_id)
--     follow_type说明：1-医院 2-医生 3-疾病

-- 14. t_review（评价表）
--     说明：存储用户对医生的评价信息
--     主要字段：id, order_type, order_id, user_id, doctor_id, rating, content
--     关联关系：user_id -> t_user.id, doctor_id -> t_doctor.id
--     order_type说明：1-挂号订单 2-咨询订单

-- 15. t_feedback（反馈表）
--     说明：存储用户反馈信息
--     主要字段：id, user_id, feedback_type, content, images, status, reply_content, reply_time
--     关联关系：user_id -> t_user.id
--     feedback_type说明：1-系统问题 2-服务问题 3-医生问题 4-其他问题

-- 16. t_message（消息表）
--     说明：存储系统消息/通知
--     主要字段：id, user_id, title, content, is_read
--     关联关系：user_id -> t_user.id

-- 17. t_config（系统配置表）
--     说明：存储系统配置信息
--     主要字段：id, config_key, config_value, description
--     唯一约束：config_key

-- ===================================================================
-- 三、表关系说明
-- ===================================================================

-- 1. 用户与就诊成员（一对多）
--    t_user (1) <---> (N) t_family_member
--    说明：一个用户可以添加多个就诊成员

-- 2. 医院与科室（多对多）
--    t_hospital (N) <---> (N) t_department
--    关联表：t_hospital_department
--    说明：一家医院可以开设多个科室，一个科室可以在多家医院存在

-- 3. 科室层级关系（自关联）
--    t_department (1) <---> (N) t_department
--    说明：科室表通过parent_id实现一级科室和二级科室的层级关系
--         parent_id为0表示一级科室，否则为二级科室

-- 4. 医生与科室/医院（多对一）
--    t_doctor (N) --> (1) t_department
--    t_doctor (N) --> (1) t_hospital
--    说明：一个医生属于一个科室和一家医院

-- 5. 医生与排班（一对多）
--    t_doctor (1) <---> (N) t_schedule
--    说明：一个医生可以有多个排班记录

-- 6. 用户与挂号订单（一对多）
--    t_user (1) <---> (N) t_appointment
--    说明：一个用户可以创建多个挂号订单

-- 7. 用户与咨询订单（一对多）
--    t_user (1) <---> (N) t_consult
--    说明：一个用户可以创建多个咨询订单

-- 8. 医生与挂号订单（一对多）
--    t_doctor (1) <---> (N) t_appointment
--    说明：一个医生可以接收多个挂号订单

-- 9. 医生与咨询订单（一对多）
--    t_doctor (1) <---> (N) t_consult
--    说明：一个医生可以接收多个咨询订单

-- 10. 订单与支付流水（一对多）
--     t_appointment/t_consult (1) <---> (N) t_payment_flow
--     说明：一个订单可以有多条支付流水记录（如退款等）

-- 11. 科室与文章（一对多）
--     t_department (1) <---> (N) t_article
--     说明：一个科室可以有多篇健康科普文章

-- 12. 科室与疾病（一对多）
--     t_department (1) <---> (N) t_disease
--     说明：一个科室可以有多种相关疾病

-- 13. 用户与关注（一对多）
--     t_user (1) <---> (N) t_follow
--     说明：一个用户可以关注多个医院、医生、疾病

-- 14. 用户与评价（一对多）
--     t_user (1) <---> (N) t_review
--     说明：一个用户可以发表多条评价

-- 15. 用户与反馈（一对多）
--     t_user (1) <---> (N) t_feedback
--     说明：一个用户可以提交多条反馈

-- 16. 用户与消息（一对多）
--     t_user (1) <---> (N) t_message
--     说明：一个用户可以接收多条消息

-- ===================================================================
-- 表关系图（简化版）
-- ===================================================================
/*
                          ┌─────────────┐
                          │   t_user    │
                          └──────┬──────┘
                                 │
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
        ▼                        ▼                        ▼
┌───────────────┐      ┌─────────────────┐      ┌───────────────┐
│t_family_member│      │  t_appointment  │      │   t_consult   │
└───────────────┘      └────────┬────────┘      └───────┬───────┘
                                │                       │
                                ▼                       ▼
                        ┌───────────────┐       ┌───────────────┐
                        │ t_payment_flow│       │ t_payment_flow│
                        └───────────────┘       └───────────────┘

┌─────────────┐      ┌─────────────────────┐      ┌─────────────┐
│  t_hospital │◄────►│t_hospital_department │◄────►│ t_department│
└──────┬──────┘      └─────────────────────┘      └──────┬──────┘
       │                                                  │
       │              ┌─────────────┐                     │
       └─────────────►│  t_doctor   │◄────────────────────┘
                      └──────┬──────┘
                             │
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
       ┌───────────┐  ┌───────────┐  ┌───────────┐
       │t_schedule │  │ t_review  │  │ t_follow  │
       └───────────┘  └───────────┘  └───────────┘
*/