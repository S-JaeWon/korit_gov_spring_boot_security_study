# DCL (데이터 제어어) -> DB 접근 권한과 보안 제어
# GRANT 권한 부여
# REVOKE 권환 회수
# grant select, insert on [스키마].[테이블명] to [root@localhost] | root -> 사용자명, localhost -> 로컬 호스트
# grant all privileges on [스키마].* to [root@localhost] | 스키마 속 모든 테이블의 모든 권한
# revoke insert on [스키마].[테이블명] to [root@localhost] | 권한 회수
# show grants for [root@localhost] | 권한 조회
# TCL (트랜잭션 제어어)
# Transaction -> 여러 sql의 쿼리들을 하나의 묶음으로 처리
# [계좌 A -> 계좌 B 로 1000원 이체 
# 계좌 A: -1000원, 계좌 B: +1000원 
# -> 동시에 성공하거나 둘 중 하나라도 실패하면 전체 작업을 되돌려야함. 
# 
# start transaction -> 트랜잭션 시작
# commit -> 지금까지의 변경 내용 확정 저장 트랜잭션 종료 이후 롤백 불가능
# rollback -> 현재 트랜잭션의 변경 내용 취소, 마지막 커밋 전까지 
# savepoint -> 중간에 체크 포인트 설정, 이 시점으로 롤백 가능
# rollback to savepoint(이름) 지정된 체크포인트까지 rollback

INSERT INTO `db_study`.`account_tb` (`account_tb`, `balance`, `user_id`) VALUES ('1', '10000', '1'), ('2', '10000', '2');

SELECT * FROM db_study.account_tb;

start transaction;

update 
	account_tb
set
	balance = balance - 1000
where
	user_id = 1;

start transaction;
    
update 
	account_tb
set
	balance = balance + 1000
where
	user_id = 2;

rollback


