DROP DATABASE IF EXISTS kanban_DB;
CREATE DATABASE IF NOT EXISTS kanban_DB;

USE kanban_DB; 

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `taskId` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL ,
  `description`varchar(500) DEFAULT NULL ,
  `assignees` varchar(30) DEFAULT NULL,
  `createdOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`taskId`),
  UNIQUE KEY `id_UNIQUE` (`taskId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
 

DROP TABLE IF EXISTS `statuses`;

CREATE TABLE statuses (
  statusId INT NOT NULL AUTO_INCREMENT,
  statusName VARCHAR(50) NOT NULL,
  statusDescription VARCHAR(200),
  PRIMARY KEY (statusId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE tasks
ADD COLUMN statusId INT not null ,
ADD CONSTRAINT fk_tasks_statuses
FOREIGN KEY (statusId) REFERENCES statuses(statusId);

INSERT INTO statuses (statusName, statusDescription) VALUES
	('NO STATUS', 'Task No status'),
  ('TO DO', 'Task needs to be started'),
  ('DOING', 'Task is in progress'),
  ('DONE', 'Task is completed');

INSERT INTO tasks (title, description, assignees, statusId) VALUES
    ('Task 1', 'Description of Task 1', 'User 1', 1),  -- Assuming 1 is the statusId for 'TO DO'
    ('Task 2', 'Description of Task 2', 'User 2', 2),  -- Assuming 2 is the statusId for 'DOING'
    ('Task 3', 'Description of Task 3', 'User 3', 3);  -- Assuming 3 is the statusId for 'DONE'
