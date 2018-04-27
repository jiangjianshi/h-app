--2017-11-01 14:43:37
--中介公司-城市-过滤统计 整租数量
SELECT
 a.f_company_name,
 b.f_city,
 count(DISTINCT c.f_house_sell_id)
FROM
 t_house_base a,
 t_house_detail b,
 t_house_approve_record c
WHERE
 a.f_house_sell_id = b.f_house_sell_id
AND b.f_house_sell_id = c.f_house_sell_id
AND b.f_entire_rent = 1
AND c.f_error_reason LIKE '%未开通%'
GROUP BY
 a.f_company_name,
 b.f_city
ORDER BY
 a.f_company_name
 
--中介公司-城市-过滤统计 分租房间数量
SELECT
 a.f_company_name,
 b.f_city,
 count(c.f_house_sell_id)
FROM
 t_house_base a,
 t_house_detail b,
 t_room_base c
WHERE
 a.f_house_sell_id = b.f_house_sell_id
AND b.f_house_sell_id = c.f_house_sell_id
AND b.f_entire_rent = 0
AND c.f_house_sell_id in(SELECT DISTINCT
 b.f_house_sell_id
FROM
 t_house_detail a,
 t_house_approve_record b
WHERE
 a.f_house_sell_id = b.f_house_sell_id
AND a.f_entire_rent = 0
AND b.f_error_reason LIKE '%未开通%')
GROUP BY
 a.f_company_name,
 b.f_city
ORDER BY
 a.f_company_name
