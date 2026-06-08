CREATE TABLE IF NOT EXISTS share_users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    avatar      VARCHAR(500) DEFAULT NULL,
    bio         VARCHAR(300) DEFAULT NULL,
    deleted     TINYINT      DEFAULT 0,
    created_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS share_projects (
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,
    title          VARCHAR(200)  NOT NULL,
    description    TEXT,
    cover_url      VARCHAR(500)  DEFAULT NULL,
    project_url    VARCHAR(500)  DEFAULT NULL,
    github_url     VARCHAR(500)  DEFAULT NULL,
    user_id        BIGINT        NOT NULL,
    like_count     INT           DEFAULT 0,
    favorite_count INT           DEFAULT 0,
    view_count     INT           DEFAULT 0,
    status         TINYINT       DEFAULT 1 COMMENT '1:published 0:draft',
    deleted        TINYINT       DEFAULT 0,
    created_at     DATETIME      DEFAULT CURRENT_TIMESTAMP,
    screenshots    TEXT          DEFAULT NULL,
    updated_at     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS share_tags (
    id    BIGINT PRIMARY KEY AUTO_INCREMENT,
    name  VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) DEFAULT '#6366f1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS share_project_tags (
    project_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (project_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS share_user_likes (
    user_id    BIGINT   NOT NULL,
    project_id BIGINT   NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS share_user_favorites (
    user_id    BIGINT   NOT NULL,
    project_id BIGINT   NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- seed tags
INSERT IGNORE INTO share_tags (name, color) VALUES
  ('AI工具', '#6366f1'),
  ('开源项目', '#22c55e'),
  ('Web应用', '#f59e0b'),
  ('移动端', '#ec4899'),
  ('API服务', '#14b8a6'),
  ('数据分析', '#8b5cf6'),
  ('图像处理', '#f97316'),
  ('自然语言', '#06b6d4'),
  ('代码工具', '#84cc16'),
  ('其他', '#94a3b8');

CREATE TABLE IF NOT EXISTS share_comments (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_id  BIGINT   NOT NULL,
    user_id     BIGINT   NOT NULL,
    content     TEXT     NOT NULL,
    deleted     TINYINT  DEFAULT 0,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
