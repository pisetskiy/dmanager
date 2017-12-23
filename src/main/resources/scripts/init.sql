CREATE TABLE IF NOT EXISTS `download` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`name`	TEXT NOT NULL UNIQUE,
	`priority`	TEXT NOT NULL,
	`status`	TEXT NOT NULL,
	`created`	INTEGER NOT NULL,
	`username`	TEXT,
	`password`	TEXT
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

CREATE TABLE IF NOT EXISTS `item` (
	`id`	INTEGER PRIMARY KEY AUTOINCREMENT,
	`id_download`	INTEGER NOT NULL,
	`link`	TEXT NOT NULL,
	`destination`	TEXT NOT NULL,
	`status`	TEXT NOT NULL,
	`name`	TEXT NOT NULL,
	`totalBytes`	INTEGER NOT NULL DEFAULT 0,
	`loadedBytes`	INTEGER NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX IF NOT EXISTS `item_id_pk` ON `item` (
	`id`	ASC
);

CREATE INDEX IF NOT EXISTS `item_id_download` ON `item` (
	`id_download`	ASC
);

CREATE INDEX IF NOT EXISTS `item_status` ON `item` (
	`status`	ASC
);