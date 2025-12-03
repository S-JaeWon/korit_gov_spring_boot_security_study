SELECT * FROM db_study.board_tb;

update
	board_tb
set
	title = "커피",
	content = "아메리카노"
where
	user_id = 2;

select
	*
from
	board_tb
where 
	title like "%목3%"
order by
	user_id desc;

# 데이터 사전 
select table_name
from information_schema.tables 
where table_schema = "db_study"
order by table_name desc;
# 스키마의 테이블 조회 

# 인덱스 조회
select index_name
from information_schema.statistics
where table_schema = "db_study" and table_name = "user_tb";

# 인덱스 추가
create index idx_user_password
on user_tb(password);

select
	sum(user_id) as sum_user_id,
	avg(user_id) as avg_user_id,
	count(user_id) as count_user_id
from
	board_tb;

