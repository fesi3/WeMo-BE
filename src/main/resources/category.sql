-- TB_CATEGORY 테이블 생성
CREATE TABLE IF NOT EXISTS TB_CATEGORY (
                                           category_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 id',
                                           parent_id INT NULL COMMENT '상위 카테고리 id',
                                           category_name VARCHAR(255) NOT NULL COMMENT '카테고리 이름',
    FOREIGN KEY (parent_id) REFERENCES TB_CATEGORY(category_id) ON DELETE CASCADE
    );

-- 초기 데이터 삽입
INSERT INTO TB_CATEGORY (category_id, parent_id, category_name) VALUES
                                                                    (1, NULL, '달램핏'),
                                                                    (2, NULL, '워케이션'),
                                                                    (3, 1, '오피스 스트레칭'),
                                                                    (4, 1, '마인드풀니스');
