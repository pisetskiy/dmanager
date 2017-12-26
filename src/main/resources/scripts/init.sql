CREATE TABLE IF NOT EXISTS `download` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL UNIQUE,
	`destination` TEXT NOT NULL,
	`priority`	INTEGER NOT NULL,
	`status`	TEXT NOT NULL,
	`created`	INTEGER NOT NULL,
	`message`   TEXT
);

CREATE UNIQUE INDEX IF NOT EXISTS `download_id_pk` ON `download` (
	`id`	ASC
);

CREATE UNIQUE INDEX IF NOT EXISTS `download_name` ON `download` (
	`name`	ASC
);

CREATE INDEX IF NOT EXISTS `download_status` ON `download` (
	`status` ASC
);

CREATE INDEX IF NOT EXISTS `download_priority` ON `download` (
	`priority`	ASC
);

CREATE INDEX IF NOT EXISTS `download_created` ON `download` (
	`created`	ASC
);

CREATE TABLE IF NOT EXISTS `file` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`id_download`	INTEGER NOT NULL,
	`link`	TEXT NOT NULL,
	`username` TEXT,
	`password` TEXT,
	`status`	TEXT NOT NULL,
	`name`	TEXT NOT NULL,
	`message` TEXT,
	`created` INTEGER NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS `file_id_pk` ON `file` (
	`id`	ASC
);

CREATE INDEX IF NOT EXISTS `file_id_download` ON `file` (
	`id_download`	ASC
);

CREATE INDEX IF NOT EXISTS `file_status` ON `file` (
	`status`	ASC
);

CREATE INDEX IF NOT EXISTS `file_created` ON `file` (
	`created`	ASC
);